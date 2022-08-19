package org.apache.eventmesh.api.connector.storage;

import java.util.List;
import java.util.Properties;

import org.apache.eventmesh.api.AbstractContext;
import org.apache.eventmesh.api.EventListener;
import org.apache.eventmesh.api.consumer.Consumer;

import io.cloudevents.CloudEvent;

public class StorageConsumer implements Consumer{

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
	public void updateOffset(List<CloudEvent> cloudEvents, AbstractContext context) {
		storageOperation.updateOffset(cloudEvents, context);
	}

	@Override
	public void subscribe(String topic) throws Exception {
		
	}

	@Override
	public void unsubscribe(String topic) {
		
	}

	@Override
	public void registerEventListener(EventListener listener) {
		
	}

}
