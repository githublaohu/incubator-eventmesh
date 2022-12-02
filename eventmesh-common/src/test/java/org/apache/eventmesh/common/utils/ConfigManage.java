package org.apache.eventmesh.common.utils;

import org.apache.eventmesh.common.config.Config;

import lombok.Getter;

@Config(prefix ="laohu" , field = "convertEntity" , path = "classPath://config.properties" , monitor = true)
public class ConfigManage {

	@Getter
	private ConvertEntity convertEntity;
}
