package org.apache.eventmesh.common.config;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;

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
		
		private String prefix;
		
		public <T> T getConfig(ConfigInfo configInfo) throws Exception {
			this.init(configInfo);
			Properties properties = new Properties();
			properties.load(new BufferedReader(new FileReader(configInfo.getFilePath())));
			if(Objects.isNull(configInfo.getClazz())) {
				return (T)properties;
			}
			return this.getConfig(properties, configInfo);
		}
		
		private void init(ConfigInfo configInfo) {
			String prefix = configInfo.getPrefix();
			if(Objects.nonNull(prefix)) {
				this.prefix = prefix.endsWith(".") ? prefix : prefix + ".";
			}
		}

		@SuppressWarnings("unchecked")
		public <T> T getConfig(Properties properties, ConfigInfo configInfo) throws Exception {
			this.init(configInfo);
			Properties newProperties = new Properties();
			Object instance = configInfo.getClazz().newInstance();
			for (Entry<Object, Object> entry : properties.entrySet()) {
				String key = (String) entry.getKey();
				String newKey = null;
				if(Objects.equals(configInfo.getHump(), ConfigInfo.HUPM_SPOT)) {
					StringBuffer stringBuffer = new StringBuffer();
					int i = 0;
					String[] array = StringUtils.split(key,".");
					for(String s : array) {
						if( i == 0) {
							stringBuffer.append(s);
						}else {
							stringBuffer.append(StringUtils.capitalize(s));
						}
						i++;
					}
					newKey = stringBuffer.toString();
				}else if (Objects.nonNull(prefix) && key.startsWith(prefix)) {
					newKey = removePrefix(key, prefix, configInfo.isRemovePrefix());
				}else {
					key = newKey;
				}
				newProperties.put(newKey, entry.getValue());
				Field field = null;
				try {
					field = configInfo.getClazz().getDeclaredField(newKey);
				}finally {
					if(Objects.isNull(field)) {
						continue;
					}
				}
				
				Class<?> clazz = field.getType();
				Object object = null;
				String value = entry.getValue().toString();
				if(int.class.equals(clazz) || Integer.class.equals(clazz)) {
					object = Integer.valueOf(value);
				}else if(long.class.equals(clazz) || Long.class.equals(clazz)) {
					object = Long.valueOf(value);
				}else if(byte.class.equals(clazz) || Byte.class.equals(clazz)) {
					object = Byte.valueOf(value);
				}else if(short.class.equals(clazz) || Short.class.equals(clazz)) {
					object = Short.valueOf(value);
				}else if(boolean.class.equals(clazz) || Boolean.class.equals(clazz)) {
					object = Boolean.valueOf(value);
				}else if(char.class.equals(clazz) || Character.class.equals(clazz)) {
					object = value.charAt(0);
				}else if(double.class.equals(clazz) || Double.class.equals(clazz)) {
					object = Double.valueOf(value);
				}else if(float.class.equals(clazz) || Float.class.equals(clazz)) {
					object = Float.valueOf(value);
				}else if(Object.class.equals(clazz)) {
					object = value;
				}else if(String.class.equals(clazz)){
					object = value;
				}else {
					// time type
				}
				
				field.set(instance, object);
			}
			return (T) instance;
		}

		
		
		private String removePrefix(String key, String prefix, boolean removePrefix) {
			return removePrefix ? key.replace(prefix, "") : key;
		}
	}

	class YamlFileLoad implements FileLoad {

		@Override
		public <T> T getConfig(ConfigInfo configInfo) throws Exception {
			//Yaml yaml = new Yaml();
			//return yaml.loadAs(new BufferedInputStream(new FileInputStream(configInfo.getFilePath())),configInfo.getClazz());
			return null;
		}

	}
}
