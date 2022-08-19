package org.apache.eventmesh.runtime.connector.storage;

import org.apache.eventmesh.api.RequestReplyCallback;

import lombok.Data;

@Data
public class RequestReplyInfo {

	private Long storageId;
	
	private RequestReplyCallback requestReplyCallback;
	
	private String storageConnectorName;
}
