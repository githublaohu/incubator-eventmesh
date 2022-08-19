package org.apache.eventmesh.runtime.connector.storage.pull;

import org.apache.eventmesh.api.connector.storage.StorageConnector;

import lombok.Data;

@Data
public class PullRequesInfo {

	private String topicName;
	
	private String consumerGroupName;
	
	private StorageConnector storageConnector;
}
