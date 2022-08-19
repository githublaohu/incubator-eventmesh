package org.apache.eventmesh.runtime.connector.storage.pull;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.eventmesh.api.connector.storage.PullCallback;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoragePullService implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(StoragePullService.class);
	
	private final LinkedBlockingQueue<PullRequest> pullRequestQueue = new LinkedBlockingQueue<PullRequest>(); 
	
	private ScheduledExecutorService  scheduledExecutor; 
	
	private PullCallback pullCallback;
	
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
                PullRequest pullRequest = this.pullRequestQueue.take();
                pullRequest.getStorageConnector().pull(pullRequest);
            } catch (InterruptedException ignored) {
            } catch (Exception e) {
            	logger.error("executePullRequestImmediately pullRequestQueue.put", e);
            }
        }
	}
}
