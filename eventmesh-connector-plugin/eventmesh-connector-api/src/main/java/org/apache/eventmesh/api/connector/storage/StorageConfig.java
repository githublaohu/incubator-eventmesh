package org.apache.eventmesh.api.connector.storage;

import lombok.Data;

@Data
public class StorageConfig {

	private long pullInterval;
	
	private long pullThresholdForQueue;
}
