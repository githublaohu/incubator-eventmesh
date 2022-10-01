package org.apache.eventesh.connector.storage.jdbc;

import org.apache.eventmesh.api.connector.storage.data.CloudEventInfo;
import org.apache.eventmesh.api.connector.storage.data.ConsumerGroupInfo;
import org.apache.eventmesh.api.connector.storage.data.TopicInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ResultSetTransformUtils {

	public static String transformTableName(ResultSet resultSet) throws SQLException{
		
		
		return resultSet.getString(1);
	}
	
	public static ConsumerGroupInfo transformConsumerGroup(ResultSet resultSet){
		ConsumerGroupInfo consumerGroupInfo = new ConsumerGroupInfo();
		
		return consumerGroupInfo;
	}
	
	public static List<CloudEventInfo> transformCloudEvent(ResultSet resultSet){
		
		
		return null;
	}
	
	
	public static TopicInfo transformTopicInfo(ResultSet resultSet){
		
		
		return null;
	}
}
