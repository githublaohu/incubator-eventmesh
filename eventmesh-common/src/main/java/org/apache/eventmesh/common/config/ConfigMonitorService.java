package org.apache.eventmesh.common.config;

import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigMonitorService {



	private final Map<WatchKey, ConfigInfo> watchKeyPathMap = new ConcurrentHashMap<>();

	private WatchService service;

	public ConfigMonitorService() {
		try {
			service = FileSystems.getDefault().newWatchService();
			new Thread(new ConfigMonitor()).start();
		}catch(Exception e) {
			log.error(e.getMessage() , e);
		}
	}

	public void monitor(ConfigInfo configInfo) {
		try {
			WatchKey key = Paths.get(configInfo.getFilePath()).register(service, StandardWatchEventKinds.ENTRY_MODIFY);
			watchKeyPathMap.put(key, configInfo);
		} catch (Exception e) {
			log.error("getWatchService failed.", e);
		}
	}

	public class ConfigMonitor implements Runnable {
		
		@Override
		public void run() {
			while (true) {
				try {
					WatchKey key = ConfigMonitorService.this.service.take();
					for (WatchEvent<?> event : key.pollEvents()) {
						ConfigInfo configInfo = ConfigMonitorService.this.watchKeyPathMap.get(key);
						
					}
				} catch (InterruptedException e) {
					log.error("Interrupted", e);
				}

			}

		}

	}
}
