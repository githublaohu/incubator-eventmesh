package org.apache.eventesh.connector.storage.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetTransform<T> {

	public T transform(ResultSet resultSet)  throws SQLException;
}
