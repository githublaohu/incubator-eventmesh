package org.apache.eventmesh.runtime.core;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.eventmesh.api.AbstractContext;
import org.apache.eventmesh.api.EventMeshAsyncConsumeContext;
import org.apache.eventmesh.runtime.core.EventMeshEventCloudConfig.RemoveAfter;
import org.apache.eventmesh.runtime.core.EventMeshEventCloudService.CloudEventInfo;
import org.apache.eventmesh.runtime.core.plugin.MQProducerWrapper;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;

public class EventMeshEventCloudServiceTest {

	private EventMeshEventCloudService eventMeshEventCloudService;

	private EventMeshEventCloudConfig eventMeshEventCloudConfig;

	private Field cloudEventInfoMapField;
	
	private Field mqProducerWrapperFiled;
	
	@Before
	public void init() {
		eventMeshEventCloudConfig = new EventMeshEventCloudConfig();
		cloudEventInfoMapField = FieldUtils.getDeclaredField(EventMeshEventCloudService.class, "cloudEventInfoMap", true);
		mqProducerWrapperFiled = FieldUtils.getDeclaredField(EventMeshEventCloudService.class, "mqProducerWrapper", true);
		
	}

	@Test
	public void test_send_reply_request() throws Exception {
		EventMeshEventCloudService eventMeshEventCloudService = new EventMeshEventCloudService(
				eventMeshEventCloudConfig);
		MQProducerWrapper mqProducerWrapper = Mockito.mock(MQProducerWrapper.class);
		mqProducerWrapperFiled.set(eventMeshEventCloudService, mqProducerWrapper);
		
		eventMeshEventCloudService.send(null, null);
		Mockito.verify(mqProducerWrapper,Mockito.times(1)).send(Mockito.any(), Mockito.any());
		
		eventMeshEventCloudService.request(null, null, null, 1);
		Mockito.verify(mqProducerWrapper,Mockito.times(1)).request(Mockito.any(), Mockito.any(), Mockito.anyLong());
		
		eventMeshEventCloudService.reply(null, null);
		Mockito.verify(mqProducerWrapper,Mockito.times(1)).reply(Mockito.any(), Mockito.any());
	}
	
	@Test
	public void test_consumeAck_null() throws Exception {
		EventMeshEventCloudService eventMeshEventCloudService = new EventMeshEventCloudService(eventMeshEventCloudConfig);
		boolean isBoolean = eventMeshEventCloudService.consumeAck("0");
		Assert.assertFalse(isBoolean);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void test_consumeAck() throws Exception {
		EventMeshEventCloudService eventMeshEventCloudService = new EventMeshEventCloudService(eventMeshEventCloudConfig);
		
		Map<Long, CloudEventInfo> cloudEventInfoMap = (Map<Long, CloudEventInfo>) cloudEventInfoMapField.get(eventMeshEventCloudService);
		CloudEventInfo cloudEventInfo = new CloudEventInfo();
		EventMeshAsyncConsumeContext context = Mockito.mock(EventMeshAsyncConsumeContext.class);
		cloudEventInfo.setContext(context);
		AbstractContext EventMeshConsumeConcurrentlyContext = Mockito.mock(AbstractContext.class);
		Mockito.when(context.getAbstractContext()).thenReturn(EventMeshConsumeConcurrentlyContext);

		cloudEventInfo.setGroupName("test");
		cloudEventInfo.setCloudEvent(Mockito.mock(CloudEvent.class));
		
		cloudEventInfoMap.put(1L, cloudEventInfo);
		
		EventMeshConsumerManager eventMeshConsumerManager = Mockito.mock(EventMeshConsumerManager.class);
		eventMeshEventCloudService.setEventMeshConsumerManager(eventMeshConsumerManager);
		
		eventMeshEventCloudService.consumeAck("1");
		Mockito.verify(eventMeshConsumerManager,Mockito.times(1)).updateOffset(Mockito.anyString(), Mockito.any(),  Mockito.any(),  Mockito.any());
	}
	
	@Test
	public void test_consumeAck_event() {
		EventMeshEventCloudService eventMeshEventCloudService = Mockito.spy(new EventMeshEventCloudService(eventMeshEventCloudConfig));
		CloudEvent cloudEvent = Mockito.mock(CloudEvent.class);
		Mockito.when(cloudEvent.getExtension(Mockito.anyString())).thenReturn("1");
		eventMeshEventCloudService.consumeAck(cloudEvent);
		Mockito.verify(eventMeshEventCloudService,Mockito.times(1)).consumeAck(Mockito.anyString());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_removeCloudInfo_REDO() throws Exception {
		EventMeshEventCloudService eventMeshEventCloudService = Mockito.spy(new EventMeshEventCloudService(eventMeshEventCloudConfig));
		MQProducerWrapper mqProducerWrapper = Mockito.mock(MQProducerWrapper.class);
		mqProducerWrapperFiled.set(eventMeshEventCloudService, mqProducerWrapper);
		
		CloudEvent cloudEvent = Mockito.mock(CloudEvent.class);
		Mockito.when(cloudEvent.getExtension(Mockito.anyString())).thenReturn("1");
		Map<Long, CloudEventInfo> cloudEventInfoMap = (Map<Long, CloudEventInfo>) cloudEventInfoMapField.get(eventMeshEventCloudService);
		CloudEventInfo cloudEventInfo = new CloudEventInfo();
		cloudEventInfo.setCloudEvent(cloudEvent);
		cloudEventInfoMap.put(1L, cloudEventInfo);
		eventMeshEventCloudService.removeCloudInfo(cloudEvent);
		Mockito.verify(mqProducerWrapper , Mockito.times(1)).send(Mockito.any(), Mockito.any());
	}
	
	
	
	@Test
	public void test_retry_is_not_retry() {
		eventMeshEventCloudConfig.setRetry(false);
		eventMeshEventCloudConfig.setRemoveAfter(RemoveAfter.NONE);
		eventMeshEventCloudService = Mockito.spy(new EventMeshEventCloudService(eventMeshEventCloudConfig));
		CloudEvent cloudEvent = Mockito.mock(CloudEvent.class);

		Mockito.when(cloudEvent.getExtension(Mockito.anyString())).thenReturn(1L);

		eventMeshEventCloudService.retry(cloudEvent, null, null);
		Mockito.verify(cloudEvent, Mockito.times(1)).getExtension(Mockito.anyString());
		Mockito.verify(eventMeshEventCloudService, Mockito.times(1)).removeCloudInfo(Mockito.any());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test_retry_is_retrynum_zero_and_times() throws Exception {
		eventMeshEventCloudConfig.setRetryNum(0);
		EventMeshEventCloudService eventMeshEventCloudService = new EventMeshEventCloudService(
				eventMeshEventCloudConfig);
		Map<Long, CloudEventInfo> cloudEventInfoMap = (Map<Long, CloudEventInfo>) cloudEventInfoMapField.get(eventMeshEventCloudService);
		eventMeshEventCloudService = Mockito.spy(eventMeshEventCloudService);

		CloudEvent cloudEvent = CloudEventBuilder.v1().withId("1").withSource(new URI("/")).withType("123")
				.withData(new byte[12]).build();

		cloudEvent = eventMeshEventCloudService.record(cloudEvent, null);

		CloudEventInfo cloudEventInfo = cloudEventInfoMap.get(1L);
		eventMeshEventCloudService.retry(cloudEvent, null, null);
		Mockito.verify(eventMeshEventCloudService, Mockito.times(1)).removeCloudInfo(Mockito.any());

		Assert.assertEquals(cloudEventInfo.getRetry(), 0);
		
	}
}
