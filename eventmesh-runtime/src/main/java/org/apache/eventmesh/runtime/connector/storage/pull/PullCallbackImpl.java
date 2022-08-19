package org.apache.eventmesh.runtime.connector.storage.pull;

import java.util.List;
import java.util.concurrent.Executor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.eventmesh.api.connector.storage.PullCallback;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;

import io.cloudevents.CloudEvent;
import lombok.Setter;

public class PullCallbackImpl implements PullCallback {
	
	@Setter
	private StoragePullService storagePullService;
	
	@Setter
	private Executor executor;

	@Override
	public void onSuccess(PullRequest pullRequest, List<CloudEvent> cloudEvents) {
		try {
			if(CollectionUtils.isNotEmpty(cloudEvents)) {
				
			}
		}catch(Exception e) {
			
		}
	}

	@Override
	public void onException(PullRequest pullRequest , Exception exception) {

	}

}
