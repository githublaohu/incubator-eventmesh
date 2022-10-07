package org.apache.eventesh.connector.storage.jdbc.SQL;

import org.apache.eventmesh.common.utils.StringReplace;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SQLServiceInvocationHandler implements InvocationHandler {
	
	private Map<String, MethodAndSQLMapper> cache = new ConcurrentHashMap<>();

	public void analysis(Object object) throws Exception {
		for(Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			MethodAndSQLMapper mapper = new MethodAndSQLMapper();
			mapper.setSql((String)field.get(object));
			mapper.setStringReplace(new StringReplace(mapper.getSql()));
			if(cache.containsKey(field.getName())) {
				// errer
				String errer = String.format("method repeat , class name %s , field name %s", object.getClass().getSimpleName(),field.getName());
				throw new RuntimeException(errer);
			}
			cache.put(field.getName(), mapper);
		}
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String argsKey;
		if (args.length == 0) {
			argsKey = "";
		}
		if (args.length == 1) {
			argsKey = (String) args[0];
		} else {
			StringBuffer stringBuffer = new StringBuffer();
			for (Object object : args) {
				stringBuffer.append(object.toString());
				stringBuffer.append("-");
			}
			argsKey = stringBuffer.toString();
		}
		MethodAndSQLMapper proxyObject = cache.get(method.getName());
		String sql = proxyObject.getCache().get(argsKey);
		if (Objects.isNull(sql)) {
			sql = proxyObject.getStringReplace().replace(args);
			proxyObject.getCache().put(argsKey, sql);
		}
		return sql;
	}

}
