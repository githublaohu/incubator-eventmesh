package org.apache.eventesh.connector.storage.jdbc.SQL;

public class MySQLStorageSQL extends AbstractStorageSQL {

	
	private void initialization() {
		StringBuffer insertSQL = new StringBuffer();
		insertSQL.append("inset info ");
		if(true) {
			insertSQL.append(this.databases).append(".");
		}
		insertSQL.append(this.tableName);
		
	}
	
	public String selectAppointTimeMessageSQL(String tableName,String time) {
		return super.selectAppointTimeMessageSQL(tableName,time) + " limit 1 ";
	}

	@Override
	public String queryTables() {
		return "select tables_name from information_schema.tables where table_schema = ?";
	}
	
}
