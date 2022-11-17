package org.apache.eventmesh.common.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigInfo {
	
	public static final String HUPM_SPOT = "spot";
	
	public static final String HUPM_ROD = "rod";

	private String path;
	
	private String field;
	
	private String prefix;
	
	private Class<?> clazz;
	
	private Object object;
	
	private String filePath;
	
	private boolean removePrefix;
	
	private boolean monitor;
	
	private String hump;
}
