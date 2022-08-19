package org.apache.eventmesh.runtime.connector.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.eventmesh.api.AbstractContext;
import org.apache.eventmesh.api.LifeCycle;
import org.apache.eventmesh.api.RequestReplyCallback;
import org.apache.eventmesh.api.SendCallback;
import org.apache.eventmesh.api.connector.storage.StorageConnector;
import org.apache.eventmesh.api.connector.storage.data.ClusterInfo;
import org.apache.eventmesh.api.connector.storage.data.ConsumerGroupInfo;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.apache.eventmesh.api.connector.storage.data.ServiceInfo;
import org.apache.eventmesh.api.connector.storage.data.TopicInfo;
import org.apache.eventmesh.api.exception.ConnectorRuntimeException;
import org.apache.eventmesh.api.exception.OnExceptionContext;
import org.apache.eventmesh.runtime.connector.storage.meta.RouteHandler;
import org.apache.eventmesh.runtime.connector.storage.meta.StorageMetaServcie;
import org.apache.eventmesh.runtime.connector.storage.pull.StoragePullService;

import io.cloudevents.CloudEvent;
import io.grpc.Metadata;
import lombok.Getter;

public class StorageConnectorService implements LifeCycle{

	private StoragePullService pullService ;
	
	private StorageMetaServcie storageMetaServcie;
	
	private Map<String,ServiceInfo>  serviceMap = new HashMap<>();
	
	private Map<String , StorageConnector>  storageConnectorMap = new HashMap<>();
	
	private Map<String/*集群名*/, List<PullRequest>> namePullRequest = new ConcurrentHashMap<>();
	
	private RequestReplyService requestReplyService;
	
	private Executor executor;
	
	private ScheduledExecutorService  scheduledExecutor;
	
	private Map<String, ClusterInfo> clusterInfo = new HashMap<>();
	
	@Getter
	private StorageConnector storageConnector = new StorageConnectorProxy();

	
	public void init(Properties properties) throws Exception {
		this.pullService = new StoragePullService();
		this.storageMetaServcie = new StorageMetaServcie();
		this.storageMetaServcie.setStorageConnectorMap(storageConnectorMap);
		this.storageMetaServcie.setScheduledExecutor(scheduledExecutor);
		// mysql1
		// mysql2
		// mysql3
	}
	
	@Override
	public boolean isStarted() {
		return true;
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public void start() {
	}
	

	@Override
	public void shutdown() {
		storageConnectorMap.values().forEach( value -> value.shutdown());
	}
	
	public  class StorageConnectorProxy implements StorageConnector{

		@Override
		public void start() {
		}
		
		@Override
		public void shutdown() {
		}

		@Override
		public void init(Properties properties) throws Exception {
		}

		@Override
		public void publish(CloudEvent cloudEvent, SendCallback sendCallback) throws Exception {
			String topic = null;
			RouteHandler routeHandler = storageMetaServcie.getRouteHandler(topic);
			if(Objects.nonNull(routeHandler)) {
				this.doPublish(routeHandler, cloudEvent, sendCallback);
			}
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						storageMetaServcie.createTopic(null);
						RouteHandler newRouteHandler = storageMetaServcie.getRouteHandler(topic);
						if(Objects.isNull(newRouteHandler)) {
							sendCallback.onException(createOnExceptionContext(null, cloudEvent));
						}
						doPublish(newRouteHandler, cloudEvent, sendCallback);
					}catch(Exception e) {
						sendCallback.onException(createOnExceptionContext(e, cloudEvent));
					}
				}
			});
		}
		
		private void doPublish(RouteHandler routeHandler,CloudEvent cloudEvent, SendCallback sendCallback) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						StorageConnector storageConnector = routeHandler.select();
						if(Objects.isNull(storageConnector)) {
							
						}
					}catch(Exception e) {
						sendCallback.onException(createOnExceptionContext(e, cloudEvent));
					}
				}
			});
		}
		
		private OnExceptionContext createOnExceptionContext(Exception e,CloudEvent cloudEvent) {
			OnExceptionContext onExceptionContext = new OnExceptionContext();
			onExceptionContext.setException(new ConnectorRuntimeException(e));
			return onExceptionContext;
		}

		

		@Override
		public void request(CloudEvent cloudEvent, RequestReplyCallback requestReplyCallback, long timeout) throws Exception {
			String topic = null;
			// 是否创建
			RouteHandler routeHandler = storageMetaServcie.getRouteHandler(topic);
			if(Objects.nonNull(routeHandler)) {
				this.doRequest(routeHandler, cloudEvent, requestReplyCallback, timeout);
				return;
			}
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						storageMetaServcie.createTopic(null);
						RouteHandler newRouteHandler = storageMetaServcie.getRouteHandler(topic);
						if(Objects.isNull(newRouteHandler)) {
							requestReplyCallback.onException(null);
						}
						doRequest(newRouteHandler, cloudEvent, requestReplyCallback, timeout);
					}catch(Exception e) {
						requestReplyCallback.onException(e);
					}
				}
			});
			
		}
		
		private void doRequest(RouteHandler routeHandler , CloudEvent cloudEvent, RequestReplyCallback requestReplyCallback, long timeout) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						StorageConnector storageConnector = routeHandler.select();
						if(Objects.isNull(storageConnector)) {
							
						}
						Long storageId = (Long)cloudEvent.getExtension("storageId");
						RequestReplyInfo requestReplyInfo = new RequestReplyInfo();
						requestReplyInfo.setStorageId(storageId);
						requestReplyInfo.setRequestReplyCallback(requestReplyCallback);
					}catch(Exception e) {
						requestReplyCallback.onException(e);
					}
				}
			});
		}

		@Override
		public void updateOffset(List<CloudEvent> cloudEvents, AbstractContext context) {
			
		}

		
		@Override
		public List<CloudEvent> pull(PullRequest pullRequest) {
			return null;
		}

		@Override
		public int createTopic(TopicInfo topicInfo) {
			return 0;
		}

		@Override
		public int createConsumerGroup(ConsumerGroupInfo consumerGroupInfo) {
			storageMetaServcie.createConsumerGroup(consumerGroupInfo);
			
			return 1;
		}

		@Override
		public Metadata queryMetaData() {
			return null;
		}

		@Override
		public int deleteCloudEvent() {
			return 0;
		}
	}

	
}
