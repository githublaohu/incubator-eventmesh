package org.apache.eventmesh.runtime.session;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.eventmesh.common.protocol.SubscriptionItem;
import org.apache.eventmesh.common.protocol.SubscriptionMode;
import org.apache.eventmesh.common.protocol.http.body.client.SubscribeRequestBody;
import org.apache.eventmesh.runtime.core.protocol.context.GrpcRpcContext;
import org.apache.eventmesh.runtime.core.protocol.context.TcpRpcContext;

import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.netty.channel.Channel;

public class SessionSerivceTest {

	private SessionSerivce sessionSerivce = new SessionSerivce();
	
	private Channel channle = Mockito.mock(Channel.class);
	
	private SocketAddress socketAddress = Mockito.mock(SocketAddress.class);
	
	private TcpRpcContext tcpRpcContext = new  TcpRpcContext();
	
	private GrpcRpcContext grpcRpcContext = new GrpcRpcContext();
	
	private Map<String , Session> sessionMap;
	
	private SubscribeRequestBody subscribeRequestBody;
	
	private SubscribeRequestBody twoSubscribeRequestBody;
	
	
	@SuppressWarnings("unchecked")
	@Before
	public void init() throws IllegalArgumentException, IllegalAccessException {
		Mockito.when(channle.remoteAddress()).thenReturn(socketAddress);
		Mockito.when(socketAddress.toString()).thenReturn("1").thenReturn("2").thenReturn("1").thenReturn("2");
		
		Field sessionMapField = FieldUtils.getDeclaredField(SessionSerivce.class, "sessionMap",true);
		sessionMap = (Map<String, Session>) sessionMapField.get(sessionSerivce);
		
		subscribeRequestBody = new SubscribeRequestBody();
		subscribeRequestBody.setConsumerGroup("test");
		List<SubscriptionItem> topics = new ArrayList<>();
		subscribeRequestBody.setTopics(topics);
		SubscriptionItem item = new SubscriptionItem();
		item.setTopic("1");
		item.setMode(SubscriptionMode.CLUSTERING);
		topics.add(item);
		item = new SubscriptionItem();
		item.setTopic("2");
		item.setMode(SubscriptionMode.BROADCASTING);
		topics.add(item);
		
		twoSubscribeRequestBody = new SubscribeRequestBody();
		twoSubscribeRequestBody.setConsumerGroup("test");
		topics = new ArrayList<>();
		twoSubscribeRequestBody.setTopics(topics);
		item = new SubscriptionItem();
		item.setTopic("1");
		item.setMode(SubscriptionMode.CLUSTERING);
		topics.add(item);
		item = new SubscriptionItem();
		item.setTopic("2");
		item.setMode(SubscriptionMode.BROADCASTING);
		topics.add(item);
	}
	
	@Test
	public void test_createSession() {
		tcpRpcContext.setChannle(channle);
		grpcRpcContext.setChannle(channle);
		sessionSerivce.createSession(tcpRpcContext);
		sessionSerivce.createSession(grpcRpcContext);
		
		Session session = sessionMap.get("1");
		Assert.assertEquals(session.getClass(), TcpSession.class);
		session = sessionMap.get("2");
		Assert.assertEquals(session.getClass(), GrpcSession.class);
		Assert.assertEquals(sessionMap.size(), 2);
	}
	
	@Test
	public void test_deleteSession() {
		sessionSerivce.createSession(tcpRpcContext);
		sessionSerivce.createSession(grpcRpcContext);
		Assert.assertEquals(sessionMap.size(), 2);
		sessionSerivce.deleteSession(tcpRpcContext);
		Assert.assertEquals(sessionMap.size(), 1);
		
	}
	
	@Test
	public void test_subscribe() {
		tcpRpcContext.setChannle(channle);
		grpcRpcContext.setChannle(channle);
		sessionSerivce.createSession(tcpRpcContext);
		sessionSerivce.createSession(grpcRpcContext);
		
		sessionSerivce.subscribe(tcpRpcContext, subscribeRequestBody);
		sessionSerivce.subscribe(grpcRpcContext, twoSubscribeRequestBody);
	}
	
	@Test
	public void test_unSubscribe() {
		tcpRpcContext.setChannle(channle);
		grpcRpcContext.setChannle(channle);
		sessionSerivce.createSession(tcpRpcContext);
		sessionSerivce.createSession(grpcRpcContext);
		
		sessionSerivce.subscribe(tcpRpcContext, subscribeRequestBody);
		sessionSerivce.subscribe(grpcRpcContext, twoSubscribeRequestBody);
		sessionSerivce.unSubscribe(tcpRpcContext);
		
	}
	
	
	@Test
	public void test_getSession() {
		
	}
	
}
