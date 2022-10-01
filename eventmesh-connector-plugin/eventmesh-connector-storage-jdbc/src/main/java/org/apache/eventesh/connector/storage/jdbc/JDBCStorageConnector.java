package org.apache.eventesh.connector.storage.jdbc;

import org.apache.eventmesh.api.AbstractContext;
import org.apache.eventmesh.api.RequestReplyCallback;
import org.apache.eventmesh.api.SendCallback;
import org.apache.eventmesh.api.connector.storage.CloudEventUtils;
import org.apache.eventmesh.api.connector.storage.StorageConnector;
import org.apache.eventmesh.api.connector.storage.StorageConnectorInfo;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.cloudevents.CloudEvent;

/**
 * @author laohu
 *
 */
public class JDBCStorageConnector extends AbstractJDBCStorageConnectorMetadata implements StorageConnector {

	private String getTableName(CloudEvent cloudEvent) {
		return cloudEvent.getType();
	}

	@Override
	public void publish(CloudEvent cloudEvent, SendCallback sendCallback) throws Exception {
		String topic = this.getTableName(cloudEvent);
		String sql = this.cloudEventSQLOperation.insertCloudEventSQL(topic);
		List<Object> parameterList = new ArrayList<>();
		this.execute(sql, parameterList);
	}

	@Override
	public void request(CloudEvent cloudEvent, RequestReplyCallback rrCallback, long timeout) throws Exception {
		String topic = this.getTableName(cloudEvent);
		String sql = this.cloudEventSQLOperation.insertCloudEventSQL(topic);
		List<Object> parameterList = new ArrayList<>();
		this.execute(sql, parameterList);
	}

	@Override
	public List<CloudEvent> pull(PullRequest pullRequest) throws Exception {
		String locationEventSQL = this.cloudEventSQLOperation.locationEventSQL(pullRequest.getTopicName());
		//TODO 1. consumerGroup  2.  example_id 3. id 4.consumerGroup
		List<Object> parameter = new ArrayList<>();
		
		parameter.add(pullRequest.getConsumerGroupName());
		parameter.add(pullRequest.getProcessSign());
		parameter.add(pullRequest.getNextId());
		parameter.add(pullRequest.getConsumerGroupName());
		
		long num = this.execute(locationEventSQL, parameter);
		if(num == 0) {
			return null;
		}
		String queryLocationEventSQL = this.cloudEventSQLOperation.queryLocationEventSQL(pullRequest.getTopicName());
		parameter.clear();
		parameter.add(pullRequest.getNextId());
		parameter.add(pullRequest.getProcessSign());
		this.query(queryLocationEventSQL,parameter, ResultSetTransformUtils::transformCloudEvent);
		return null;
	}

	@Override
	public void updateOffset(List<CloudEvent> cloudEvents, AbstractContext context) {
		List<Object> parameterList = new ArrayList<>(cloudEvents.size());
		for (CloudEvent cloudEvent : cloudEvents) {
			try {
				String topic = this.getTableName(cloudEvent);
				String sql = this.cloudEventSQLOperation.updateCloudEventOffsetSQL(topic);
				parameterList.add(cloudEvent.getExtension("cloudEventInfoId"));
				long i = this.execute(sql, parameterList);
				if (i != cloudEvents.size()) {
					messageLogger.warn("");
				}
				parameterList.clear();
			} catch (Exception e) {
				messageLogger.error(e.getMessage(), e);
			}
		}
	}


	@Override
	public boolean reply(CloudEvent cloudEvent, SendCallback sendCallback) throws Exception {
		String sql = this.cloudEventSQLOperation.updateCloudEventReplySQL(CloudEventUtils.getTopic(cloudEvent));
		List<Object> parameterList = new ArrayList<>();
		parameterList.add(CloudEventUtils.serializeReplyData(cloudEvent));
		parameterList.add(CloudEventUtils.getId(cloudEvent));
		return this.execute(sql, parameterList) == 1;
	}


	@Override
	public void start() {
		
	}

	@Override
	public void shutdown() {
		druidDataSource.close();
	}
}
