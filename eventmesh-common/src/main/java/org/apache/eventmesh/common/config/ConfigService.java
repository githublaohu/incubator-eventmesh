package org.apache.eventmesh.common.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Properties;


public class ConfigService {
	
	private static final ConfigService INSTANCE = new ConfigService();

	private Properties properties = new Properties();

	private ConfigMonitorService configMonitorService = new ConfigMonitorService();
	
	private String configPath;
	
	
	public static final ConfigService getInstance() {
		return INSTANCE;
	}
	
	public ConfigService() {}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}
	
	public void getRootConfig(String path) throws Exception {
		ConfigInfo configInfo = new ConfigInfo();
		configInfo.setPath(path);
		properties = this.getConfig(configInfo);
	}

	public void getConfig(Object object, Class<?> clazz) throws Exception {
		Config[] configArray = clazz.getAnnotationsByType(Config.class);
		if (configArray == null || configArray.length == 0) {
			//TODO
			return;
		}
		for (Config config : configArray) {
			ConfigInfo configInfo = new ConfigInfo();
			configInfo.setField(config.field());
			configInfo.setPath(configInfo.getPath());
			configInfo.setPrefix(configInfo.getPrefix());
			configInfo.setObject(object);
			Field field = clazz.getDeclaredField(configInfo.getField());
			configInfo.setClazz(field.getType());
			Object configObject = this.getConfig(configInfo);
			field.set(object, configObject);
		}

	}

	public void getConfig(Object object) throws Exception {
		this.getConfig(object, object.getClass());
	}

	@SuppressWarnings("unchecked")
	public <T> T getConfig(ConfigInfo configInfo) throws Exception {
		Object object;
		if (Objects.isNull(configInfo.getPath())) {
			object = FileLoad.getPropertiesFileLoad().getConfig(properties, configInfo);
		} else {
			String path = configInfo.getPath();
			String filePath;
			if (path.startsWith("classPath://")) {
				filePath = path.substring(12);
			} else if (path.startsWith("file://")) {
				filePath = path.substring(7);
			} else {
				filePath = this.configPath + path;
			}
			File file = new File(filePath);
			if (!file.exists()) {
				throw new RuntimeException("fie is not existis");
			}
			String suffix = path.substring(path.lastIndexOf('.')+1);
			configInfo.setFilePath(filePath);
			object = FileLoad.getFileLoad(suffix).getConfig(configInfo);
		}
		if (configInfo.isMonitor()) {
			configMonitorService.monitor(configInfo);
		}
		return (T) object;
	}

}
