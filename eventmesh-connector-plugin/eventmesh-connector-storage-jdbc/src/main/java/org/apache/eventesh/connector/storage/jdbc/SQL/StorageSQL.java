package org.apache.eventesh.connector.storage.jdbc.SQL;


public interface StorageSQL {

	public String insertSQL();

	public String selectSQL();
	
	public String selectConsumerGroup();
	
	public String insertConsumerGroup();
	
	public String consumerGroupTableSQL();
	
	
	
	public String selectFastMessageSQL(String tableName);
	
	public String selectLastMessageSQL(String tableName);
	
	public String selectNoConsumptionMessageSQL(String tableName,Long consumerGroupId);
	
	public String selectAppointTimeMessageSQL(String tableName,String time);
	
	public String queryTables();

	public default String replySelectSQL(int num) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from {table} where id in(");
		for (int i = 1; i <= num; i++) {
			sql.append("?");
			if (i == num) {
				sql.append(",");
			}
		}
		sql.append(")");
		return sql.toString();
	}

	public default String updateOffsetSQL(int num) {
		StringBuffer sql = new StringBuffer();
		sql.append("update {table} set  where id in(");
		for (int i = 1; i <= num; i++) {
			sql.append("?");
			if (i == num) {
				sql.append(",");
			}
		}
		sql.append(")");
		sql.append(" and value = value + 1<<  ");
		return sql.toString();
	}
}
