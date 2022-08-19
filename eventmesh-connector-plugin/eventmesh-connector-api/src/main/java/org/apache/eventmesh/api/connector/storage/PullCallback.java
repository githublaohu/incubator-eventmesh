package org.apache.eventmesh.api.connector.storage;

import java.util.List;

import org.apache.eventmesh.api.connector.storage.data.PullRequest;

import io.cloudevents.CloudEvent;

public interface PullCallback {

	
    void onSuccess(PullRequest pullRequest,List<CloudEvent> cloudEvents);

    void onException(PullRequest pullRequest, Exception exception);
}
