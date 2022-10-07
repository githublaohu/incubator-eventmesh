package org.apache.eventesh.connector.storage.jdbc.SQL;

import org.apache.eventmesh.common.utils.StringReplace;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;


@Data
public class MethodAndSQLMapper {
	
	private String sql;

	private Map<String, String> cache = new ConcurrentHashMap<>();
	
	private StringReplace stringReplace;
}
