package org.apache.eventmesh.api.connector.storage.reply;

import org.apache.eventmesh.api.connector.storage.data.CloudEventInfo;

import java.util.ArrayList;
import java.util.List;

public interface ReplyOperation {

	List<CloudEventInfo>  queryCloudEvent(ReplyRequest replyRequest) throws Exception;
	
	public default List<CloudEventInfo>  queryCloudEvent(List<ReplyRequest> replyRequestList) throws Exception{
		List<CloudEventInfo> cloudEventInfoList = new ArrayList<CloudEventInfo>();
		for(ReplyRequest replyRequest : replyRequestList) {
			cloudEventInfoList.addAll(this.queryCloudEvent(replyRequest));
		}
		return cloudEventInfoList;
	}
}