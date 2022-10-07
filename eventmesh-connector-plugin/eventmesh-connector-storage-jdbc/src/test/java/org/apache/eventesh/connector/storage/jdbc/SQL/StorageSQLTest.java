package org.apache.eventesh.connector.storage.jdbc.SQL;

import org.junit.Assert;
import org.junit.Test;

public class StorageSQLTest {

    public static final String TABLE_NAME = "cloud_event_test";

    MySQLStorageSQL storageSQL = new MySQLStorageSQL();

    @Test
    public void replySelectSQLTest() {
        String sql = storageSQL.replySelectSQL(TABLE_NAME, 5);
        Assert.assertEquals("SELECT * FROM cloud_event_test WHERE cloud_event_info_id IN(?,?,?,?,?) AND cloud_event_reply_data IS NOT NULL", sql);
    }

    @Test
    public void replyResultTest() {
        String sql = storageSQL.replyResult(TABLE_NAME);
        Assert.assertEquals(sql, "UPDATE cloud_event_test SET cloud_event_reply_data = ?, cloud_event_reply_state = 'NOTHING' WHERE cloud_event_info_id = ?");
    }

    @Test
    public void updateOffsetSQLTest() {
        String sql = storageSQL.updateOffsetSQL(TABLE_NAME);
        Assert.assertEquals(sql, "UPDATE cloud_event_test SET cloud_event_state = 'SUCCESS' WHERE cloud_event_info_id = ?");
    }
}
