package org.apache.eventesh.connector.storage.jdbc;

import java.util.Properties;

import org.junit.Test;

public class JDBCStorageConnectorTest  {

	JDBCStorageConnector connector = new JDBCStorageConnector();
	
	@Test
	public void testInit() throws Exception {
		Properties properties = new Properties();
		properties.put("url", "jdbc:mysql://127.0.0.1:3306/electron?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull");
		properties.put("username", "root");
		properties.put("password", "Ab123123@");
		properties.put("maxActive", "20");
		properties.put("maxWait", "10000");
		connector.init(properties);
	}
}
