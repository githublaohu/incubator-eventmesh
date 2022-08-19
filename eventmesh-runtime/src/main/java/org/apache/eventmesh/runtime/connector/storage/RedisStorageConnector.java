package org.apache.eventmesh.runtime.connector.storage;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.apache.eventmesh.api.AbstractContext;
import org.apache.eventmesh.api.RequestReplyCallback;
import org.apache.eventmesh.api.SendCallback;
import org.apache.eventmesh.api.connector.storage.StorageConnector;
import org.apache.eventmesh.api.connector.storage.data.ConsumerGroupInfo;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.apache.eventmesh.api.connector.storage.data.TopicInfo;
import org.redisson.Redisson;

import io.cloudevents.CloudEvent;
import io.grpc.Metadata;
import lombok.Setter;

public class RedisStorageConnector implements StorageConnector{
	
	
	@Setter
	private List<PullRequest> pullRequestList;
	
	private Redisson redisson;

	@Override
	public boolean isStarted() {
		return true;
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public void start() {
		
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void init(Properties properties) throws Exception {
		// TODO Auto-generated method stub
		
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
	
	
	
}
