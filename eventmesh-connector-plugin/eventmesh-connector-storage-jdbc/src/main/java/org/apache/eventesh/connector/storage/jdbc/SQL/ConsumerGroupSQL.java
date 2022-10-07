package org.apache.eventesh.connector.storage.jdbc.SQL;

import lombok.Data;

@Data
public class ConsumerGroupSQL {
	
	private String createConsumerGroupSQL;
	
	private String insertConsumerGroupSQL = "insert into cumsumer_group(cumsumer_group_name) values(?)";
	
	private String selectConsumerGroupSQL = "select * from cumsumer_group";

}
