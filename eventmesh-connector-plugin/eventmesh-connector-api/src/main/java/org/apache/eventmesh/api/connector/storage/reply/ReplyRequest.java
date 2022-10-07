package org.apache.eventmesh.api.connector.storage.reply;

import java.util.List;

import lombok.Data;

@Data
public class ReplyRequest {

	private String topic;
	
	private List<Long> idList;
}
