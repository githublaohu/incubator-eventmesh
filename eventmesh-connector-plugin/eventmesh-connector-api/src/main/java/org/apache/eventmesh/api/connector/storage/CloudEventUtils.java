package org.apache.eventmesh.api.connector.storage;

import org.apache.eventmesh.api.connector.storage.data.CloudEventInfo;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.impl.BaseCloudEvent;
import io.cloudevents.core.v03.CloudEventV03;
import io.cloudevents.core.v1.CloudEventV1;

public class CloudEventUtils {

	private static Field CLOUD_EVENT_EXTENSIONS_FIELD;

	static {
		try {
			CLOUD_EVENT_EXTENSIONS_FIELD = BaseCloudEvent.class.getField("extensions");
			CLOUD_EVENT_EXTENSIONS_FIELD.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {

		}
	}

	@SuppressWarnings("unchecked")
	public static CloudEvent setValue(CloudEvent cloudEvent, String key, Object value) {
		if (Objects.nonNull(CLOUD_EVENT_EXTENSIONS_FIELD)
				&& (cloudEvent instanceof CloudEventV1 || cloudEvent instanceof CloudEventV03)) {
			try {
				Map<String, Object> extensions = (Map<String, Object>) CLOUD_EVENT_EXTENSIONS_FIELD.get(cloudEvent);
				extensions.put(key, value);
				return cloudEvent;
			} catch (Exception e) {

			}
		}
		return null;
	}
	
	public static String getNodeAdress(CloudEvent cloudEvent) {
		return (String)cloudEvent.getExtension(Constant.NODE_ADDRESS);
	}
	
	public static String getTopic(CloudEvent cloudEvent) {
		return null;
	}
	
	public static String getId(CloudEvent cloudEvent) {
		return null;
	}
	
	public static CloudEvent createCloudEvent(CloudEventInfo cloudEventInfo) {
		
		return null;
	}
	
	public static CloudEvent createReplyDataEvent(CloudEventInfo cloudEventInfo) {
		
		return null;
	}
	
	public static String getCloudEventMessageId(CloudEventInfo cloudEventInfo) {
		
		return null;
	}
	
	public static String serializeReplyData(CloudEvent cloudEvent) {
		return null;
	}
	
	public static String deserializeReplyData(CloudEventInfo cloudEventInfo) {
		return null;
	}

}
