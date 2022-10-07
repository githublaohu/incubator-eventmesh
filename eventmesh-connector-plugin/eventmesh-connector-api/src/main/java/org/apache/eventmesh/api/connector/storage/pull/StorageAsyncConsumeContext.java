package org.apache.eventmesh.api.connector.storage.pull;

import org.apache.eventmesh.api.EventMeshAction;
import org.apache.eventmesh.api.EventMeshAsyncConsumeContext;
import org.apache.eventmesh.api.connector.storage.StorageConnector;

import java.util.ArrayList;
import java.util.List;

import io.cloudevents.CloudEvent;
import lombok.Setter;

public class StorageAsyncConsumeContext extends EventMeshAsyncConsumeContext {

	@Setter
	private StorageConnector storageConnector;

	@Setter
	private CloudEvent cloudEvent;

	@Override
	public void commit(EventMeshAction action) {
		switch (action) {
		case CommitMessage:
			List<CloudEvent> cloudEventList = new ArrayList<>(1);
			cloudEventList.add(cloudEvent);
			storageConnector.updateOffset(cloudEventList, this.getAbstractContext());
			break;
		case ReconsumeLater:
			break;
		case ManualAck:
			break;
		default:
			break;
		}
	}

}
