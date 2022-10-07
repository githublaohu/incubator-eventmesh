package org.apache.eventesh.connector.storage.jdbc.SQL;

import org.junit.Test;

public class StorageSQLServiceTest {

	@Test
	public void testStorageSQLService() throws Exception {
		StorageSQLService storageSQLService = new StorageSQLService("mysql");
		CloudEventSQLOperation cloudEventSQLOperation = storageSQLService.getObject();
		cloudEventSQLOperation.updateCloudEventOffsetSQL("test");
	}
}
