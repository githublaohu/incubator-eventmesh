package org.apache.eventmesh.runtime.connector.storage.meta;

import java.util.List;

import org.apache.eventmesh.api.connector.storage.StorageConnector;

import lombok.Setter;

public class RouteHandler {

	@Setter
	List<StorageConnector>  storageConnector;
	
	
	private RouteSelect souteSelect;
	
	
	public StorageConnector select() {
		return souteSelect.select(storageConnector);
	}
}
