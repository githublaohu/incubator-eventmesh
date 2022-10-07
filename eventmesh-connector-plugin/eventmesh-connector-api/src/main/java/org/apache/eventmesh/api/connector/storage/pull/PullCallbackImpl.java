package org.apache.eventmesh.api.connector.storage.pull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.eventmesh.api.EventListener;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.apache.eventmesh.api.connector.storage.pull.PullCallback;

import java.util.List;
import java.util.concurrent.Executor;

import io.cloudevents.CloudEvent;
import lombok.Setter;

public class PullCallbackImpl implements PullCallback {
	
	
	@Setter
	private EventListener eventListener;
	
	@Setter
	private Executor executor;

	@Override
	public void onSuccess(PullRequest pullRequest, List<CloudEvent> cloudEvents) {
		try {
			if(CollectionUtils.isEmpty(cloudEvents)) {
				return;
			}
			pullRequest.getStock().getAndUpdate( value ->  value + cloudEvents.size());
			for(CloudEvent cloudEvent : cloudEvents) {
				executor.execute(new Runnable() {
					public void run() {
						try {
							StorageAbstractContext abstractContext = new StorageAbstractContext();
							StorageAsyncConsumeContext context = new StorageAsyncConsumeContext();
							context.setAbstractContext(abstractContext);
							context.setStorageConnector(pullRequest.getStorageConnector());
							eventListener.consume(cloudEvent, context);
							pullRequest.getStock().decrementAndGet();
						}catch(Exception e) {
							
						}
					}
				});
				
			}
			
		}catch(Exception e) {
			
		}
	}
}
