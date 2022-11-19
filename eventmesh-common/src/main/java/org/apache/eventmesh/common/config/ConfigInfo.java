package org.apache.eventmesh.common.config;

import java.lang.reflect.Field;

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
	
	Field objectField;
	
	Object instance;
	
	protected void  setObjectField(Field objectField) {
		this.objectField = objectField;
	}
	
	protected Field getObjectField() {
		return this.objectField;
	}
	
	protected void  setInstance(Object instance) {
		this.instance = instance;
	}
	
	protected Object getInstance() {
		return this.instance;
	}
	
}
