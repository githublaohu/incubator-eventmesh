package org.apache.eventmesh.common.utils;

import org.apache.eventmesh.common.config.ConfigInfo;

import java.util.Properties;

import org.junit.Test;

public class ConvertTest {

	private Convert convert = new Convert();
	
	@Test
	public void createObject() {
		
		ConfigInfo configInfo = new ConfigInfo();
		configInfo.setClazz(ConvertEntity.class);
		configInfo.setHump(ConfigInfo.HUPM_ROD);
		Properties properties = new Properties();
		properties.put("byte_value", "1");
		properties.put("short_value", "2");
		properties.put("int_value", "3");
		properties.put("long_value", "4");
		properties.put("float_value", "5");
		properties.put("double_value", "6");
		properties.put("char_value", "7");
		properties.put("string_value", "8");
		properties.put("list_value[1]", "9");
		properties.put("list_value[2]", "10");
		properties.put("list_value[3]", "11");
		properties.put("map_value_one", "12");
		properties.put("conver_enume", "INT");
		properties.put("super_string", "super");
		properties.put("data_value", "2022-12-12 11:11:11");
		properties.put("local_data_value", "2022-12-12");
		properties.put("local_data_time_value", "2022-12-12 11:11:11");
		convert.createObject(configInfo, properties);
	}
}
