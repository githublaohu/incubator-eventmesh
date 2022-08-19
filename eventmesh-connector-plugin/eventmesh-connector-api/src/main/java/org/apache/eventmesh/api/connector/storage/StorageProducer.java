package org.apache.eventmesh.api.connector.storage;

import java.util.Properties;

import org.apache.eventmesh.api.RequestReplyCallback;
import org.apache.eventmesh.api.SendCallback;
import org.apache.eventmesh.api.producer.Producer;

import io.cloudevents.CloudEvent;

public class StorageProducer implements Producer{

	private StorageConnector storageOperation;
	
	@Override
	public boolean isStarted() {
		return storageOperation.isStarted();
	}

	@Override
	public boolean isClosed() {
		return storageOperation.isClosed();
	}

	@Override
	public void start() {
		storageOperation.start();
	}

	@Override
	public void shutdown() {
		storageOperation.shutdown();
	}

	@Override
	public void init(Properties keyValue) throws Exception {
		storageOperation.init(keyValue);
		
	}

	@Override
	public void publish(CloudEvent cloudEvent, SendCallback sendCallback) throws Exception {
		storageOperation.publish(cloudEvent, sendCallback);
	}

	@Override
	public void sendOneway(CloudEvent cloudEvent) {
		try {
			storageOperation.publish(cloudEvent, null);
		}catch(Exception e) {
			
		}
		
	}

	@Override
	public void request(CloudEvent cloudEvent, RequestReplyCallback rrCallback, long timeout) throws Exception {
		storageOperation.request(cloudEvent, rrCallback,timeout);
	}

	@Override
	public boolean reply(CloudEvent cloudEvent, SendCallback sendCallback) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void checkTopicExist(String topic) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExtFields() {
		// TODO Auto-generated method stub
		
	}

}
