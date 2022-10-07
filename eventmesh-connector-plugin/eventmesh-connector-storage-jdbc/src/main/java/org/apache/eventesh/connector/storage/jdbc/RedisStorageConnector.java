package org.apache.eventesh.connector.storage.jdbc;

import org.apache.eventmesh.api.AbstractContext;
import org.apache.eventmesh.api.RequestReplyCallback;
import org.apache.eventmesh.api.SendCallback;
import org.apache.eventmesh.api.connector.storage.StorageConnector;
import org.apache.eventmesh.api.connector.storage.data.CloudEventInfo;
import org.apache.eventmesh.api.connector.storage.data.ConsumerGroupInfo;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.apache.eventmesh.api.connector.storage.data.TopicInfo;
import org.apache.eventmesh.api.connector.storage.reply.ReplyOperation;
import org.apache.eventmesh.api.connector.storage.reply.ReplyRequest;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.redisson.Redisson;

import io.cloudevents.CloudEvent;
import io.grpc.Metadata;
import lombok.Setter;

public class RedisStorageConnector implements StorageConnector,ReplyOperation{

	
	private Redisson redisson;
	@Override
	public void start() {
		
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void init(Properties properties) throws Exception {
		
	}

	@Override
	public void publish(CloudEvent cloudEvent, SendCallback sendCallback) throws Exception {
			this.send(cloudEvent, sendCallback, null, 0);
	}

	@Override
	public void request(CloudEvent cloudEvent, RequestReplyCallback rrCallback, long timeout) throws Exception {
		this.send(cloudEvent, null, rrCallback, timeout);
		
	}
	
	private void send(CloudEvent cloudEvent,  SendCallback sendCallback,RequestReplyCallback rrCallback, long timeout){
		redisson.getQueue("").addAsync("").whenComplete((value, exception) -> {
			if(Objects.isNull(exception)) {
				if(Objects.nonNull(rrCallback)) {
					rrCallback.onException(null);
				}else {
					sendCallback.onException(null);
				}
            }else {
            	if(Objects.nonNull(sendCallback)) {
            		sendCallback.onSuccess(null);
            	}
            }
        });
	}

	@Override
	public List<CloudEvent> pull(PullRequest pullRequest) {
		// 普通消息
		
		// 定时消息
		
		// rpc消息
		
		// 整个
		List<Object> cloudEvent = redisson.getQueue(pullRequest.getTopicName()).poll(30);
		
		return null;
	}

	@Override
	public void updateOffset(List<CloudEvent> cloudEvents, AbstractContext context) {
	}

	@Override
	public List<CloudEventInfo> queryReplyCloudEvent(ReplyRequest replyRequest) throws Exception {
		// 所有
		List<Object> cloudEvent = redisson.getQueue(replyRequest.getTopic()).poll(30);
		return null;
	}

	@Override
	public boolean reply(CloudEvent cloudEvent, SendCallback sendCallback) throws Exception {
		// send nodeaddress queue
		return false;
	}
	
	
	
}
