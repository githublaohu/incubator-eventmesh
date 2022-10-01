package org.apache.eventesh.connector.storage.jdbc.SQL;

import lombok.Data;

@Data
public class BaseSQL {

	private String createDatabases;
	
	private String queryConsumerGroupTableSQL;
	
	private String queryCloudEventTablesSQL;
}
