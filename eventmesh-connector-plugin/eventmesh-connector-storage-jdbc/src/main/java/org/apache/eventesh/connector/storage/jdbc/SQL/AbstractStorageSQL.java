package org.apache.eventesh.connector.storage.jdbc.SQL;

import lombok.Setter;

public abstract class AbstractStorageSQL implements StorageSQL {

	protected static final String INSERT_SQL = "insert info {tables} values(?,?,?,?,?,?)";

	protected static final String SELECT_SQL = "select * from {tables} where ";

	@Setter
	protected String databases;

	@Setter
	protected String tableName;

	@Setter
	protected String offsetField;

	protected String idFieldName;
	
	protected String timeFieldName;

	protected String insertSQL = "";

	protected String selectSQL = "";

	protected String updateOffset = "";

	@Override
	public String insertSQL() {
		return insertSQL;
	}

	@Override
	public String selectSQL() {
		return selectSQL;
	}
	
	public String selectConsumerGroup() {
		return "select * from consumer_group_info where group_name = ?";
	}
	
	public String insertConsumerGroup() {
		return null;
	}

	private StringBuffer getSelectSQLById(String tableName) {
		StringBuffer sql = new StringBuffer();
		return sql.append("select ").append(tableName).append(" as tableName , ").append( idFieldName ).append(" from ").append(tableName);
	}
	// select * from tables where id > {id} and 
	public String selectFastMessageSQL(String tableName) {
		
		return this.getSelectSQLById(tableName).append("  order by  ").append( idFieldName).toString();
	}

	public String selectLastMessageSQL(String tableName) {
		return this.getSelectSQLById(tableName).append("  order by  ").append( idFieldName).append(" desc ").toString();
	}

	public String selectNoConsumptionMessageSQL(String tableName,Long consumerGroupId) {
		return this.getSelectSQLById(tableName).append(" where ").append(timeFieldName ).append(" < now() and ").append(offsetField).append(" & ").append( consumerGroupId ).append(" = ").append(consumerGroupId).toString();
	}

	public String selectAppointTimeMessageSQL(String tableName,String time) {
		return this.getSelectSQLById(tableName).append(" where ").append( timeFieldName ).append(" > ").append( time ).toString();
	}
}
