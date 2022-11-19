package org.apache.eventmesh.common.config;

import org.apache.eventmesh.common.ThreadPoolFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigMonitorService {

	private static final long TIME_INTERVAL = 30 * 1000L;

	private final List<ConfigInfo> configInfoList = new ArrayList<>();

	private ScheduledExecutorService configLoader = ThreadPoolFactory
			.createSingleScheduledExecutor("eventMesh-configLoader-");

	{
		configLoader.scheduleAtFixedRate(this::load, TIME_INTERVAL, TIME_INTERVAL, TimeUnit.MILLISECONDS);
	}

	public void monitor(ConfigInfo configInfo) {
		configInfoList.add(configInfo);
	}

	public void load() {
		for (ConfigInfo configInfo : configInfoList) {
			try {
				Object object = ConfigService.getInstance().getConfig(configInfo);
				if (configInfo.getObject().equals(object)) {
					continue;
				}
				configInfo.getObjectField().set(configInfo.getInstance(), object);
				configInfo.setObject(object);
				log.info("config connent : {}", object);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

}
