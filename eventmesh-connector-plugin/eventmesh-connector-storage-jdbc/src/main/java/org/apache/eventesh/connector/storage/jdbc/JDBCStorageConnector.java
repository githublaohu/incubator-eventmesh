package org.apache.eventesh.connector.storage.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.eventesh.connector.storage.jdbc.SQL.StorageSQL;
import org.apache.eventmesh.api.AbstractContext;
import org.apache.eventmesh.api.RequestReplyCallback;
import org.apache.eventmesh.api.SendCallback;
import org.apache.eventmesh.api.SendResult;
import org.apache.eventmesh.api.connector.storage.StorageConnector;
import org.apache.eventmesh.api.connector.storage.data.ConsumerGroupInfo;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.apache.eventmesh.api.connector.storage.data.TopicInfo;
import org.apache.eventmesh.api.exception.ConnectorRuntimeException;
import org.apache.eventmesh.api.exception.OnExceptionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import io.cloudevents.CloudEvent;
import io.grpc.Metadata;
import lombok.Data;

/**
 * 不支持多库多表，会造成非常高的复杂度，
 * @author laohu
 *
 */
public class JDBCStorageConnector implements StorageConnector {

	private Logger messageLogger = LoggerFactory.getLogger("message");

	private DruidDataSource druidDataSource;

	private Map<Long, ReplyInfo> replyMap = new ConcurrentHashMap<>();
	
	private Set<String> topicSet = new HashSet<>();

	private ConsumerGroupInfo consumerGroupInfo;

	private StorageSQL storageSQL;
	
	private Map<String,Object>  tablesMetaMap = new ConcurrentHashMap<>();
	
	
	private int execute(String sql, List<Object> parameter) throws Exception {
		try (DruidPooledConnection pooledConnection = druidDataSource.getConnection();PreparedStatement preparedStatement = pooledConnection.prepareStatement(sql)) {
			this.setObject(preparedStatement, parameter);
			ResultSet resulSet = preparedStatement.getGeneratedKeys();
			return preparedStatement.executeUpdate();
		}
	}
	
	private void setObject(PreparedStatement preparedStatement , List<Object> parameter) throws SQLException {
		if(Objects.isNull(parameter)|| parameter.isEmpty()) {
			return;
		}
		int index = 0 ;
		for(Object object : parameter) {
			preparedStatement.setObject(index++, object);
		}
	}
	
	
	private List<CloudEvent> createCloudEvent(ResultSet result) throws SQLException{
		List<CloudEvent> cloudEventList = new ArrayList<>();
		while(result.next()) {
			CloudEvent cloudEvent = null;
			
			cloudEventList.add(cloudEvent);
		}
		return cloudEventList;
	}
	
	private List<CloudEvent> queryCloudEvent(String sql, List<Object> parameter ) throws SQLException{
		try (DruidPooledConnection pooledConnection = druidDataSource.getConnection();PreparedStatement preparedStatement = pooledConnection.prepareStatement(sql)) {
			this.setObject(preparedStatement, parameter);
			try(ResultSet result = preparedStatement.executeQuery()){
				return this.createCloudEvent(result);
			}
		}
		
	}
	
	private ResultSet query(String sql, List<Object> parameter ) throws SQLException{
		try (DruidPooledConnection pooledConnection = druidDataSource.getConnection();PreparedStatement preparedStatement = pooledConnection.prepareStatement(sql)) {
			this.setObject(preparedStatement, parameter);
			return preparedStatement.executeQuery();
		}
	}


	@Override
	public void start() {
	}

	@Override
	public void shutdown() {

	}
	

	public void reply() {
		if (replyMap.isEmpty()) {
			return;
		}
		List<Object> list = new ArrayList<>();

		long time = System.currentTimeMillis();
		for (Entry<Long, ReplyInfo> entry : replyMap.entrySet()) {
			if (entry.getValue().timeOut > time) {
				list.add(entry.getKey());
			} else {
				replyMap.remove(entry.getKey());
				// 超时
				messageLogger.warn("");
				RuntimeException runtimeException = new RuntimeException();
				entry.getValue().requestReplyCallback.onException(runtimeException);
			}
		}
		// 分不同的表
		String sql = this.storageSQL.replySelectSQL(list.size());
		try  {
			List<CloudEvent> cloudEventList = this.queryCloudEvent(sql,list);
			for(CloudEvent cloudEvent : cloudEventList) {
				cloudEvent.getId();
				ReplyInfo replyInfo = replyMap.remove(Long.valueOf(cloudEvent.getId()));
				if(Objects.isNull(replyInfo)) {
					
				}
				replyInfo.requestReplyCallback.onSuccess(cloudEvent);
			}
		} catch (Exception e) {

		}

	}

	@Override
	public void init(Properties properties) throws Exception {
		druidDataSource = new DruidDataSource();
		druidDataSource.setUrl(null);
		druidDataSource.init();
		int tableInfoInt = 0;
		// 得到所有表
		List<Object> tables = new ArrayList<>();
		ResultSet resultSet = this.query(this.storageSQL.queryTables(),tables);
		while(resultSet.next()) {
			String value = resultSet.getString(0);
			if(value.startsWith("cloud_event")) {
				topicSet.add(value);
			}else if(Objects.equals("consumer_group_info", value)) {
				tableInfoInt |= 1;
			}else if(Objects.equals("", value)) {
				
			}else if(Objects.equals("", value)) {
				
			}else if(Objects.equals("", value)) {
				
			}
		}
		
		resultSet.close();
		
		if( (tableInfoInt & 1) != 1) {
			this.execute(this.storageSQL.consumerGroupTableSQL(), null);
		}else {
			List<Object> consumerGroupSelectParameter = new ArrayList<>();
			// 查询 消费组
			resultSet = this.query(this.storageSQL.selectConsumerGroup(),consumerGroupSelectParameter);
			
			resultSet.close();
		}
		
		if(Objects.isNull(this.consumerGroupInfo)) {
			// 创建消费组
			this.createConsumerGroup(consumerGroupInfo);
			List<Object> consumerGroupSelectParameter = new ArrayList<>();
			resultSet = this.query(this.storageSQL.selectConsumerGroup(),consumerGroupSelectParameter);
			
			resultSet.close();
		}
		
		
		List<Object> parameter = new ArrayList<>();
		StringBuffer sqlsb = new StringBuffer() ;
		int index = 0;
		for(Object object : tables) {
			String sql = this.storageSQL.selectLastMessageSQL((String)object);
			sqlsb.append(sql);
			if(index <= tables.size()) {
				sqlsb.append("union all");
			}
			parameter.add(object);
			parameter.add(object);
		}
		resultSet = this.query(sqlsb.toString(), parameter);
		while(resultSet.next()) {
			String tablesName = resultSet.getString(0);
			Long id = resultSet.getLong(1);
			TablesMeta tablesMeta = new TablesMeta();
			tablesMeta.setCurrentId(id);
			tablesMetaMap.put(tablesName, id);
		}

		resultSet.close();
	}

	@Override
	public void publish(CloudEvent cloudEvent, SendCallback sendCallback) throws Exception {
		String sql = this.storageSQL.insertSQL();
		List<Object> parameterList = new ArrayList<>();
		try  {
			this.execute(sql, parameterList);
			SendResult sendResult = new SendResult();
			sendCallback.onSuccess(sendResult);
		} catch (Exception e) {
			messageLogger.error(e.getMessage(), e);
			OnExceptionContext onExceptionContext = new OnExceptionContext();
			ConnectorRuntimeException connectorRuntimeException = new ConnectorRuntimeException(e);
			onExceptionContext.setException(connectorRuntimeException);
			sendCallback.onException(onExceptionContext);
		}
	}

	@Override
	public void request(CloudEvent cloudEvent, RequestReplyCallback rrCallback, long timeout) throws Exception {
		String sql = this.storageSQL.insertSQL();
		List<Object> parameterList = new ArrayList<>();
		try  {
			this.execute(sql, parameterList);
			ReplyInfo replyInfo = new ReplyInfo();
			replyInfo.timeOut = System.currentTimeMillis() + timeout;
			replyInfo.requestReplyCallback = rrCallback;
			replyInfo.cloudEvent = cloudEvent;
			replyMap.put(0L, replyInfo);
		} catch (Exception e) {
			messageLogger.error(e.getMessage(), e);
			rrCallback.onException(e);
		}
	}

	@Override
	public List<CloudEvent> pull(PullRequest pullRequest) {
		// 先修改
		// 在查询
		return null;
	}

	@Override
	public void updateOffset(List<CloudEvent> cloudEvents, AbstractContext context) {
		String sql = this.storageSQL.updateOffsetSQL(cloudEvents.size());
		List<Object> parameterList = new ArrayList<>(cloudEvents.size());
		for(CloudEvent cloudEvent : cloudEvents) {
			parameterList.add(cloudEvent.getId());
		}
		try {
			int i = this.execute(sql, parameterList);
			if(i != cloudEvents.size()) {
				messageLogger.warn("");
			}
		} catch (Exception e) {
			messageLogger.error(e.getMessage(), e);
		}
	}


	@Override
	public Metadata queryMetaData() {
		return null;
	}

	@Override
	public int deleteCloudEvent() {
		return 0;
	}

	@Override
	public int createTopic(TopicInfo topicInfo) {
		return 0;
	}

	@Override
	public int createConsumerGroup(ConsumerGroupInfo consumerGroupInfo) {
		return 0;
	}

	@Data
	static class ReplyInfo {

		private Long id;

		private Long timeOut;
		
		private RequestReplyCallback requestReplyCallback;
		
		private CloudEvent cloudEvent;
	}
	
	@Data
	static class TablesMeta{
		
		private Long currentId;
	}

}
