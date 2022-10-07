package org.apache.eventesh.connector.storage.jdbc.SQL;

import lombok.Setter;

public abstract class AbstractStorageSQL implements StorageSQL {

    @Setter
    protected String offsetField;

    protected String idFieldName = "cloud_event_info_id";

    protected String timeFieldName = "cloud_event_create_time";

    protected String insertSQL = "";

    protected String selectSQL = "";

    protected String updateOffset = "";

    @Override
    public String insertSQL(String tableName) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("INSERT INFO ")
                .append(tableName)
                .append("(cloud_event_id,cloud_event_topic,cloud_event_storage_node_adress,cloud_event_type,cloud_event_producer_group_name,cloud_event_source,cloud_event_content_type,cloud_event_tag,cloud_event_extensions,cloud_event_data) ")
                .append("VALUES(?,?,?,?,?,?,?,CAST(? as JSON),?,?)");
        return stringBuffer.toString();
    }

    @Override
    public String selectSQL(String tableName) {
        return this.queryLocationEventSQL(tableName);
    }

    public String selectConsumerGroup() {
        return "SELECT * FROM consumer_group_info";
    }

    public String insertConsumerGroup() {
        return null;
    }

    public String locationEventSQL(String tableName) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("UPDATE ")
                .append(tableName)
                .append(" SET json_set( cloud_event_consume_location , ? ,? )")
                .append(" WHERE cloud_event_info_id > ? AND json_extract(cloud_event_consume_location, ?) IS NULL LIMIT 200");
        return stringBuffer.toString();
    }

    public String queryLocationEventSQL(String tableName) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * FROM ").append(tableName).append(" WHERE cloud_event_info_id > ? AND JSON_CONTAINS_PATH(cloud_event_consume_location, 'one', ?)");
        return stringBuffer.toString();
    }

    private StringBuffer getSelectSQLById(String tableName) {
        StringBuffer sql = new StringBuffer();
        return sql.append("SELECT '").append(tableName).append("' AS tableName, ").append(idFieldName).append(" FROM ")
                .append(tableName);
    }

    // select * from tables where id > {id} and
    public String selectFirstMessageSQL(String tableName) {
        return this.getSelectSQLById(tableName).append(" ORDER BY ").append(idFieldName).append(" LIMIT 1").toString();
    }

    public String selectLastMessageSQL(String tableName) {
        return this.getSelectSQLById(tableName).append(" ORDER BY ").append(idFieldName).append(" DESC LIMIT 1").toString();
    }

    public String selectNoConsumptionMessageSQL(String tableName, Long consumerGroupId) {
        return this.getSelectSQLById(tableName)
                .append(" WHERE ")
                .append(timeFieldName).append(" > ? AND json_extract(cloud_event_consume_location, ?) IS NOT NULL LIMIT 1 ")
                .append(offsetField).append(" & ")
                .append(consumerGroupId).append(" = ").append(consumerGroupId)
                .toString();
    }

    public String selectAppointTimeMessageSQL(String tableName, String time) {
        return this.getSelectSQLById(tableName)
                .append(" WHERE ").append(timeFieldName).append(" > ? LIMIT 1")
                .toString();
    }

    @Override
    public String createDatabaseSQL(String database) {
        return String.join(" ", "CREATE DATABASE IF NOT EXISTS", database);
    }

}
