package org.apache.eventesh.connector.storage.jdbc.SQL;

import org.junit.Assert;
import org.junit.Test;

public class AbstractStorageSQLTest {

    public static final String DATABASE_NAME = "database";

    MySQLStorageSQL storageSQL = new MySQLStorageSQL();

    @Test
    public void insertSQLTest() {
        String sql = storageSQL.insertSQL(StorageSQLTest.TABLE_NAME);
        Assert.assertEquals(sql, "INSERT INFO cloud_event_test(cloud_event_id,cloud_event_topic,cloud_event_storage_node_adress,cloud_event_type,cloud_event_producer_group_name,cloud_event_source,cloud_event_content_type,cloud_event_tag,cloud_event_extensions,cloud_event_data) VALUES(?,?,?,?,?,?,?,CAST(? as JSON),?,?)");
    }

    @Test
    public void locationEventSQLTest() {
        String sql = storageSQL.locationEventSQL(StorageSQLTest.TABLE_NAME);
        Assert.assertEquals(sql, "UPDATE cloud_event_test SET json_set( cloud_event_consume_location , ? ,? ) WHERE cloud_event_info_id > ? AND json_extract(cloud_event_consume_location, ?) IS NULL LIMIT 200");
    }

    @Test
    public void queryLocationEventSQLTest() {
        String sql = storageSQL.queryLocationEventSQL(StorageSQLTest.TABLE_NAME);
        Assert.assertEquals(sql, "SELECT * FROM cloud_event_test WHERE cloud_event_info_id > ? AND JSON_CONTAINS_PATH(cloud_event_consume_location, 'one', ?)");
    }

    @Test
    public void selectFirstMessageSQLTest() {
        String sql = storageSQL.selectFirstMessageSQL(StorageSQLTest.TABLE_NAME);
        Assert.assertEquals(sql, "SELECT 'cloud_event_test' AS tableName, cloud_event_info_id FROM cloud_event_test ORDER BY cloud_event_info_id LIMIT 1");
    }

    @Test
    public void selectLastMessageSQLTest() {
        String sql = storageSQL.selectLastMessageSQL(StorageSQLTest.TABLE_NAME);
        Assert.assertEquals(sql, "SELECT 'cloud_event_test' AS tableName, cloud_event_info_id FROM cloud_event_test ORDER BY cloud_event_info_id DESC LIMIT 1");
    }

    @Test
    public void createDatabaseSQLTest() {
        String sql = storageSQL.createDatabaseSQL(DATABASE_NAME);
        Assert.assertEquals(sql, "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
    }

}
