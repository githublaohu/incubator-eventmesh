package org.apache.eventesh.connector.storage.jdbc.SQL;

import org.junit.Assert;
import org.junit.Test;

public class StorageSQLTest {
	
	public static final String TABLE_NAME = "cloud_event_test";

	MySQLStorageSQL storageSQL = new MySQLStorageSQL();
	
	@Test
	public void testReplySelectSQL() {
		String sql = storageSQL.replySelectSQL(TABLE_NAME, 5);
		Assert.assertEquals("select * from cloud_event_test where cloud_event_info_id in(?,?,?,?,?) and cloud_event_reply_data is not null", sql);
	}
	
	@Test
	public void testReplyResult() {
		String sql = storageSQL.replyResult(TABLE_NAME);
		Assert.assertEquals(sql , " update cloud_event_test  set  cloud_event_reply_data = ? , cloud_event_reply_state = 'NOTHING' where cloud_event_info_id = ?");
	}
	
	@Test
	public void testUpdateOffsetSQL() {
		String sql = storageSQL.updateOffsetSQL(TABLE_NAME);
		Assert.assertEquals(sql , "update cloud_event_test set cloud_event_state = 'SUCCESS'  where cloud_event_info_id = ?");
	}
}
