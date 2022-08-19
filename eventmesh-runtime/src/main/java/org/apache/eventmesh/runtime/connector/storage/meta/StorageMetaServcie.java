package org.apache.eventmesh.runtime.connector.storage.meta;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.eventmesh.api.connector.storage.StorageConnector;
import org.apache.eventmesh.api.connector.storage.data.ConsumerGroupInfo;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.apache.eventmesh.api.connector.storage.data.TopicInfo;
import org.apache.eventmesh.runtime.connector.storage.pull.StoragePullService;

import lombok.Setter;

public class StorageMetaServcie {

	@Setter
	private Map<String , StorageConnector>  storageConnectorMap;
	
	@Setter
	private ScheduledExecutorService  scheduledExecutor;
	
	private StoragePullService storagePullService;
	
	private AtomicBoolean pullMetaDataSign = new AtomicBoolean(false);
	
	private Map<StorageConnector, List<PullRequest>> pullRequestCache = new ConcurrentHashMap<>();
	
	public void init() {
		scheduledExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				StorageMetaServcie.this.pullMeteData();
			}
		}, 50, 1000*60, TimeUnit.MILLISECONDS);
	}
	
	
	private void pullMeteData() {
		if(!this.pullMetaDataSign.compareAndSet(false, true)) {
			
		}
		for (StorageConnector storageConnector : storageConnectorMap.values()) {
			try {
				storageConnector.queryMetaData();
				
				PullRequest pullRequest = new PullRequest();
				pullRequest.setStorageConnector(storageConnector);
				storagePullService.executePullRequestImmediately(pullRequest);
			}catch(Exception e) {
				
			}
		}
		this.pullMetaDataSign.set(false);
	}
	
	public void createTopic(TopicInfo info) {
		
	}
	
	public RouteHandler getRouteHandler(String topic) {
		return null;
	}
	
	public boolean isTopic(String topic) {
		return true;
	}
	
	public void createConsumerGroup(ConsumerGroupInfo consumerGroupInfo) {
		
	}
	
	
}