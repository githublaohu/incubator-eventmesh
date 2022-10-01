package org.apache.eventmesh.api.connector.storage.pull;

import org.apache.eventmesh.api.connector.storage.CloudEventUtils;
import org.apache.eventmesh.api.connector.storage.StorageConfig;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cloudevents.CloudEvent;
import lombok.Setter;

public class StoragePullService implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(StoragePullService.class);

	private final LinkedBlockingQueue<PullRequest> pullRequestQueue = new LinkedBlockingQueue<PullRequest>();

	private ScheduledExecutorService scheduledExecutor;
	
	@Setter
	private StorageConfig storageConfig;

	@Setter
	private Executor executor;

	private boolean isStopped() {
		return true;
	}

	public void executePullRequestLater(final PullRequest pullRequest, final long timeDelay) {
		if (!isStopped()) {
			this.scheduledExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					executePullRequestImmediately(pullRequest);
				}
			}, timeDelay, TimeUnit.MILLISECONDS);
		} else {
			logger.warn("PullMessageServiceScheduledThread has shutdown");
		}
	}
	
	public void executePullRequestLater(final PullRequest pullRequest) {
		this.executePullRequestLater(pullRequest, storageConfig.getPullInterval());
	}

	public void executePullRequestImmediately(final PullRequest pullRequest) {
		try {
			this.pullRequestQueue.put(pullRequest);
		} catch (InterruptedException e) {
			logger.error("executePullRequestImmediately pullRequestQueue.put", e);
		}
	}

	public void run() {
		while (!this.isStopped()) {
			try {
				final PullRequest pullRequest = this.pullRequestQueue.take();
				if (pullRequest.getStock().get() < this.storageConfig.getPullThresholdForQueue()) {
					this.executePullRequestLater(pullRequest, storageConfig.getPullInterval());
					continue;
				}
				
				executor.execute(new Runnable() {
					@Override
					public void run() {
						StoragePullService.this.doRun(pullRequest);
					}
				});

			} catch (InterruptedException ignored) {
			} catch (Exception e) {
				logger.error("executePullRequestImmediately pullRequestQueue.put", e);
			}
		}
	}
	
	private void doRun(PullRequest pullRequest) {
		try {
			List<CloudEvent> cloudEventList = pullRequest.getStorageConnector().pull(pullRequest);
			if(Objects.isNull(cloudEventList) || cloudEventList.isEmpty()) {
				logger.info("");
				return;
			}
			if (Objects.nonNull(cloudEventList) && !cloudEventList.isEmpty()) {
				if (Objects.nonNull(pullRequest.getPullRequests())) {
					this.setNextId(pullRequest, cloudEventList);
					pullRequest.getPullCallback().onSuccess(pullRequest, cloudEventList);
					
				} else {
					Map<String, List<CloudEvent>> topicCloudEvent = new HashMap<>();
					for (CloudEvent cloudEvent : cloudEventList) {
						String topic = CloudEventUtils.getTopic(cloudEvent);
						List<CloudEvent> list = topicCloudEvent.get(topic);
						if (Objects.isNull(list)) {
							list = new ArrayList<>();
							topicCloudEvent.put(topic, list);
						}
						list.add(cloudEvent);
					}
					Map<String, PullRequest> topicPullRequest = pullRequest.getTopicAndPullRequests();
					for (Entry<String, List<CloudEvent>> e : topicCloudEvent.entrySet()) {
						PullRequest newPullRequest = topicPullRequest.get(e.getKey());
						this.setNextId(newPullRequest, e.getValue());
						pullRequest.getPullCallback().onSuccess(newPullRequest, e.getValue());
					}
				}
			}
		} catch (Exception e) {
			logger.error("pull", e);
		}finally {
			this.executePullRequestLater(pullRequest, storageConfig.getPullInterval());
		}
	}
	
	private void setNextId(PullRequest pullRequest,List<CloudEvent> cloudEventList) {
		pullRequest.setNextId(CloudEventUtils.getId(cloudEventList.get(cloudEventList.size() - 1)));
	}
}
