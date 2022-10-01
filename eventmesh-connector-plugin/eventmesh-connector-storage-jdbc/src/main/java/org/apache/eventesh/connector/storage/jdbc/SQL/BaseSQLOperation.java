package org.apache.eventesh.connector.storage.jdbc.SQL;

public interface BaseSQLOperation {

	public String createDatabases();
	
	public String queryConsumerGroupTableSQL();
	
	public String queryCloudEventTablesSQL();
}
