package org.apache.eventmesh.common.config;

import org.apache.eventmesh.common.utils.Convert;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Objects;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

public interface FileLoad {

	static final PropertiesFileLoad PROPERTIES_FILE_LOAD = new PropertiesFileLoad();

	static final YamlFileLoad YAML_FILE_LOAD = new YamlFileLoad();

	public static FileLoad getFileLoad(String fileType) {
		if (Objects.equals("properties", fileType)) {
			return new PropertiesFileLoad();
		} else if (Objects.equals("yaml", fileType)) {
			return new YamlFileLoad();
		}
		return new PropertiesFileLoad();
	}

	public static PropertiesFileLoad getPropertiesFileLoad() {
		return PROPERTIES_FILE_LOAD;
	}

	public <T> T getConfig(ConfigInfo configInfo) throws Exception;

	class PropertiesFileLoad implements FileLoad {
		
		private Convert convert = new Convert();
		
		@SuppressWarnings("unchecked")
		public <T> T getConfig(ConfigInfo configInfo) throws Exception {
			Properties properties = new Properties();
			properties.load(new BufferedReader(new FileReader(configInfo.getFilePath())));
			if(Objects.isNull(configInfo.getClazz())) {
				return (T)properties;
			}
			return (T) convert.createObject(configInfo, properties);
		}
		
		@SuppressWarnings("unchecked")
		public <T> T getConfig(Properties properties , ConfigInfo configInfo) throws Exception {
			return (T) convert.createObject(configInfo, properties);
		}
		
		
	}

	class YamlFileLoad implements FileLoad {

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getConfig(ConfigInfo configInfo) throws Exception {
			Yaml yaml = new Yaml();
			return (T) yaml.loadAs(new BufferedInputStream(new FileInputStream(configInfo.getFilePath())),configInfo.getClazz());
		}

	}
}
