package org.apache.eventesh.connector.storage.jdbc.SQL;

public interface ConsumerGroupSQLOperation {

	public String createConsumerGroupSQL();
	
	public String insertConsumerGroupSQL();
	
	public String selectConsumerGroupSQL();
}
