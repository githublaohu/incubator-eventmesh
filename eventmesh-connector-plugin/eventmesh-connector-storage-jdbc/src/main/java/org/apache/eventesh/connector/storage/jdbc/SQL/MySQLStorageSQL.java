package org.apache.eventesh.connector.storage.jdbc.SQL;

public class MySQLStorageSQL extends AbstractStorageSQL {

	
	
	public String selectAppointTimeMessageSQL(String tableName,String time) {
		return super.selectAppointTimeMessageSQL(tableName,time) + " limit 1 ";
	}

	@Override
	public String queryTables() {
		return "select table_schema, table_name from information_schema.tables where table_schema = ?";
	}

	@Override
	public String consumerGroupTableCreateSQL() {
		StringBuffer buffer = new StringBuffer();
		buffer
		.append("create table if not exists consumer_group(")
		.append("  `cumsumer_group_id` bigint unsigned NOT NULL AUTO_INCREMENT, ")
		.append("  `consumer_group_name` varchar(255)  NOT NULL,")
		.append("  PRIMARY KEY (`cumsumer_group_id`),")
		.append("  UNIQUE KEY (`consumer_group_name`)")
		.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
		return buffer.toString();
	}

	@Override
	public String topicTableCreateSQL(String table) {
		StringBuffer buffer = new StringBuffer();
		buffer
		.append("create table if not exists event_")
		.append(table)
		.append("(")
		.append("  `cumsumer_group_id` bigint unsigned NOT NULL AUTO_INCREMENT, ")
		.append("  `consumer_group_name` varchar(255)  NOT NULL,")
		.append("  PRIMARY KEY (`cumsumer_group_id`),")
		.append("  UNIQUE KEY (`consumer_group_name`)")
		.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
		return buffer.toString();
	}


}
