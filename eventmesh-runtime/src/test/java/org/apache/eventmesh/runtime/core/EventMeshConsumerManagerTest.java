package org.apache.eventmesh.runtime.core;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.eventmesh.common.protocol.SubscriptionMode;
import org.apache.eventmesh.runtime.core.EventMeshConsumerManager.ConsumerManagerWrapper;
import org.apache.eventmesh.runtime.core.plugin.MQConsumerWrapper;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class EventMeshConsumerManagerTest {

	private EventMeshConsumerManager eventMeshConsumerManager = new EventMeshConsumerManager();

	private Map<String/* consumer group */ , ConsumerManagerWrapper> consumerMap;

	private String consumerGroup = "consumerGroup";

	private Field consumerMapField;

	@Before
	public void init() throws Exception {

		consumerMapField = FieldUtils.getDeclaredField(EventMeshConsumerManager.class, "consumerMap", true);
		consumerMap = (Map<String, ConsumerManagerWrapper>) consumerMapField.get(eventMeshConsumerManager);
	}

	@Test
	public void updateOffset_wrapper_null() {
		Boolean isBoolean = eventMeshConsumerManager.updateOffset(consumerGroup, SubscriptionMode.BROADCASTING, null,
				null);
		Assert.assertFalse(isBoolean);
	}

	@Test
	public void updateOffset_consumer_null() {
		consumerMap.put(this.consumerGroup, new ConsumerManagerWrapper());
		Boolean isBoolean = eventMeshConsumerManager.updateOffset(consumerGroup, SubscriptionMode.BROADCASTING, null,
				null);
		Assert.assertFalse(isBoolean);

		isBoolean = eventMeshConsumerManager.updateOffset(consumerGroup, SubscriptionMode.CLUSTERING, null, null);
		Assert.assertFalse(isBoolean);
	}

	@Test
	public void updateOffset_consumer() {
		ConsumerManagerWrapper consumerManagerWrapper = new ConsumerManagerWrapper();
		MQConsumerWrapper persistentMqConsumer = Mockito.mock(MQConsumerWrapper.class);
		consumerManagerWrapper.setPersistentMqConsumer(persistentMqConsumer);
		MQConsumerWrapper broadcastMqConsumer = Mockito.mock(MQConsumerWrapper.class);
		consumerManagerWrapper.setBroadcastMqConsumer(broadcastMqConsumer);
		consumerMap.put(this.consumerGroup, consumerManagerWrapper);
		
		Boolean isBoolean =  eventMeshConsumerManager.updateOffset(consumerGroup, SubscriptionMode.BROADCASTING, null, null);
		Assert.assertTrue(isBoolean);
		Mockito.verify(broadcastMqConsumer, Mockito.times(1)).updateOffset(Mockito.any(), Mockito.any());
		
		isBoolean = eventMeshConsumerManager.updateOffset(consumerGroup, SubscriptionMode.CLUSTERING, null, null);
		Assert.assertTrue(isBoolean);
		Mockito.verify(persistentMqConsumer, Mockito.times(1)).updateOffset(Mockito.any(), Mockito.any());
	}

}
