package org.apache.eventmesh.api.connector.storage.metadata;

import java.util.List;

import org.apache.eventmesh.api.connector.storage.StorageConnector;

public interface RouteSelect {

	
	public StorageConnector select(List<StorageConnector>  storageConnector);
}
