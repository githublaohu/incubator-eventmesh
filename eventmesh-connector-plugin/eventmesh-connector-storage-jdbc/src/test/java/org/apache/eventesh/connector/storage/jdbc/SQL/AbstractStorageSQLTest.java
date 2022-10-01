package org.apache.eventesh.connector.storage.jdbc.SQL;

import org.junit.Assert;
import org.junit.Test;

public class AbstractStorageSQLTest {

	MySQLStorageSQL storageSQL = new MySQLStorageSQL();
	
	@Test
	public void testLocationEventSQL() {
		String sql = storageSQL.locationEventSQL(StorageSQLTest.TABLE_NAME);
		Assert.assertEquals(sql, "update cloud_event_test set json_set( cloud_event_consume_location , ? ,? ) where cloud_event_info_id > ? and json_extract(cloud_event_consume_location, ?) is null limit 200");
	}
	
	@Test
	public void testQueryLocationEventSQL() {
		String sql = storageSQL.queryLocationEventSQL(StorageSQLTest.TABLE_NAME);
		Assert.assertEquals(sql, "select * from cloud_event_test where cloud_event_info_id > ? and JSON_CONTAINS_PATH(cloud_event_consume_location, 'one', ?)");
	}
	
	@Test
	public void testSelectFastMessageSQL() {
		String sql = storageSQL.selectFastMessageSQL(StorageSQLTest.TABLE_NAME);
		Assert.assertEquals(sql, "select 'cloud_event_test' as tableName , cloud_event_info_id from cloud_event_test  order by  cloud_event_info_id limit 1");
	}
	
	@Test
	public void testSelectLastMessageSQL() {
		String sql = storageSQL.selectLastMessageSQL(StorageSQLTest.TABLE_NAME);
		Assert.assertEquals(sql, "select 'cloud_event_test' as tableName , cloud_event_info_id from cloud_event_test  order by  cloud_event_info_id desc  limit 1");
	}
}
