package org.apache.eventesh.connector.storage.jdbc.SQL;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Proxy;

import org.yaml.snakeyaml.Yaml;

public class StorageSQLService {

	private Object object;
	
	public StorageSQLService(String dbName) throws Exception {
		String rootPath = StorageSQLService.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		if(rootPath.endsWith("/bin/main/")) {
			rootPath = rootPath + "../../src/main/resource/";
		}
		CloudEventSQL cloudEventSQL = this.readYaml(rootPath  + dbName + "-cloudevent.yaml", CloudEventSQL.class);
		BaseSQL baseSQL = this.readYaml(rootPath  + dbName + "-base.yaml", BaseSQL.class);
		ConsumerGroupSQL consumerGroupSQL = this.readYaml(rootPath  + dbName + "-consumer-group.yaml",
				ConsumerGroupSQL.class);

		SQLServiceInvocationHandler SQLServiceHandler = new SQLServiceInvocationHandler();
		SQLServiceHandler.analysis(baseSQL);
		SQLServiceHandler.analysis(cloudEventSQL);
		SQLServiceHandler.analysis(consumerGroupSQL);
		object = Proxy.newProxyInstance(this.getClass().getClassLoader(),
				new Class[] { CloudEventSQLOperation.class, BaseSQLOperation.class, ConsumerGroupSQLOperation.class },
				SQLServiceHandler);
	}
	
	@SuppressWarnings("unchecked")
	public <T>T getObject(){
		return (T)this.object;
	}

	@SuppressWarnings("unchecked")
	private <T> T readYaml(String path, Class<?> clazz) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			String errer = String.format("file does not exist , paht %s", path);
			throw new RuntimeException(errer);
		}

		Yaml yaml = new Yaml();
		return (T) yaml.loadAs(new BufferedInputStream(new FileInputStream(file)), clazz);
	}
}
