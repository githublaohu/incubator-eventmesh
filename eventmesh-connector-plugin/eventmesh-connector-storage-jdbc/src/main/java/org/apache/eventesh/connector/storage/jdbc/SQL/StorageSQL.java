package org.apache.eventesh.connector.storage.jdbc.SQL;

public interface StorageSQL {

    String insertSQL(String tableName);

    String selectSQL(String tableName);

    String selectConsumerGroup();

    String insertConsumerGroup();

    String createDatabaseSQL(String databaseName);

    String topicTableCreateSQL(String table);

    String consumerGroupTableCreateSQL();

    String locationEventSQL(String tableName);

    String queryLocationEventSQL(String tableName);

    String selectFirstMessageSQL(String tableName);

    String selectLastMessageSQL(String tableName);

    String selectNoConsumptionMessageSQL(String tableName, Long consumerGroupId);

    String selectAppointTimeMessageSQL(String tableName, String time);

    String queryTables();

    default String replySelectSQL(String table, int num) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ")
                .append(table)
                .append(" WHERE cloud_event_info_id IN(");
        for (int i = 1; i <= num; i++) {
            sql.append("?");
            if (i != num) {
                sql.append(",");
            }
        }
        sql.append(")");
        sql.append(" AND cloud_event_reply_data IS NOT NULL");
        return sql.toString();
    }

    default String replyResult(String table) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ").append(table)
                .append(" SET cloud_event_reply_data = ?, cloud_event_reply_state = 'NOTHING' WHERE cloud_event_info_id = ?");
        return sql.toString();
    }

    default String updateOffsetSQL(String table) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ").append(table)
                .append(" SET cloud_event_state = 'SUCCESS' WHERE cloud_event_info_id = ?");
        return sql.toString();
    }
}
