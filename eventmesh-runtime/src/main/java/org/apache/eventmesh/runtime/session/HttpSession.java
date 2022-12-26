/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.eventmesh.runtime.session;

import org.apache.commons.lang3.StringUtils;
import org.apache.eventmesh.common.Constants;
import org.apache.eventmesh.common.protocol.ProtocolTransportObject;
import org.apache.eventmesh.common.protocol.SubscriptionType;
import org.apache.eventmesh.common.protocol.http.HttpCommand;
import org.apache.eventmesh.common.protocol.http.body.message.PushMessageRequestBody;
import org.apache.eventmesh.common.protocol.http.common.ClientRetCode;
import org.apache.eventmesh.common.protocol.http.common.ProtocolKey;
import org.apache.eventmesh.common.protocol.http.common.ProtocolVersion;
import org.apache.eventmesh.common.protocol.http.common.RequestCode;
import org.apache.eventmesh.common.utils.IPUtils;
import org.apache.eventmesh.common.utils.JsonUtils;
import org.apache.eventmesh.common.utils.RandomStringUtils;
import org.apache.eventmesh.protocol.api.ProtocolAdaptor;
import org.apache.eventmesh.protocol.api.ProtocolPluginFactory;
import org.apache.eventmesh.runtime.constants.EventMeshConstants;
import org.apache.eventmesh.runtime.core.protocol.api.EventMeshRequest;
import org.apache.eventmesh.runtime.core.protocol.api.EventMeshResponse;
import org.apache.eventmesh.runtime.core.protocol.http.push.AsyncHTTPPushRequest;
import org.apache.eventmesh.runtime.util.EventMeshUtil;
import org.apache.eventmesh.runtime.util.WebhookUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpRequest;

public class HttpSession extends AbstractSession<Channel>{

	private HttpClient httpClient;
	
	private InetSocketAddress inetSocketAddress;
	
	@Override
	public void downstreamMessage(Map<String, Object> header, CloudEvent event, DownstreamHandler downstreamHandler) {
		try {
			
			httpClient.connect(inetSocketAddress, new DownstreamHandler() {
				
				@Override
				public void success(EventMeshResponse eventMeshResponse) {
					downstreamHandler.success(eventMeshResponse);
					HttpSession.this.after();
				}
				
				@Override
				public void fail(EventMeshResponse eventMeshResponse) {
					downstreamHandler.fail(eventMeshResponse);
					HttpSession.this.after();
				}
				
				@Override
				public void exception(Throwable e) {
					try {
						downstreamHandler.exception(e);
						HttpSession.this.exception(e);
					}catch(Exception e1) {
						HttpSession.this.exception(e1);
					}
				}
				
				@Override
				public void downstreamSuccess() {
					
				}
			});
			
			DefaultFullHttpRequest request = new DefaultFullHttpRequest(httpVersion, method, uri);
			ChannelFuture channelFuture = this.channel.writeAndFlush(request);
			this.before();
			channelFuture.addListener(new ChannelFutureListener() {
	
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()) {
						try {
							downstreamHandler.downstreamSuccess();
							HttpSession.this.after();
						}catch(Exception e) {
							HttpSession.this.exception(e);
						}
					}else {
						try {
							downstreamHandler.exception(future.cause());
							HttpSession.this.exception(future.cause());
						}catch(Exception e) {
							HttpSession.this.exception(e);
						}
					}
				}
			});
		}catch(Exception e) {
			this.exception(e);
		}
		
	}

	
	public void http() {

        String requestCode = "";

        if (SubscriptionType.SYNC.equals(handleMsgContext.getSubscriptionItem().getType())) {
            requestCode = String.valueOf(RequestCode.HTTP_PUSH_CLIENT_SYNC.getRequestCode());
        } else {
            requestCode = String.valueOf(RequestCode.HTTP_PUSH_CLIENT_ASYNC.getRequestCode());
        }

        builder.addHeader(ProtocolKey.REQUEST_CODE, requestCode);
        builder.addHeader(ProtocolKey.LANGUAGE, Constants.LANGUAGE_JAVA);
        builder.addHeader(ProtocolKey.VERSION, ProtocolVersion.V1.getVersion());
        builder.addHeader(ProtocolKey.EventMeshInstanceKey.EVENTMESHCLUSTER,
            handleMsgContext.getEventMeshHTTPServer()
                .getEventMeshHttpConfiguration().eventMeshCluster);
        builder.addHeader(ProtocolKey.EventMeshInstanceKey.EVENTMESHIP, IPUtils.getLocalAddress());
        builder.addHeader(ProtocolKey.EventMeshInstanceKey.EVENTMESHENV,
            handleMsgContext.getEventMeshHTTPServer().getEventMeshHttpConfiguration().eventMeshEnv);
        builder.addHeader(ProtocolKey.EventMeshInstanceKey.EVENTMESHIDC,
            handleMsgContext.getEventMeshHTTPServer().getEventMeshHttpConfiguration().eventMeshIDC);

        CloudEvent event = CloudEventBuilder.from(handleMsgContext.getEvent())
            .withExtension(EventMeshConstants.REQ_EVENTMESH2C_TIMESTAMP,
                String.valueOf(System.currentTimeMillis()))
            .build();
        handleMsgContext.setEvent(event);

        String content = "";
        try {
            String protocolType = Objects.requireNonNull(event.getExtension(Constants.PROTOCOL_TYPE)).toString();

            ProtocolAdaptor<ProtocolTransportObject> protocolAdaptor = ProtocolPluginFactory.getProtocolAdaptor(protocolType);

            ProtocolTransportObject protocolTransportObject =
                protocolAdaptor.fromCloudEvent(handleMsgContext.getEvent());
            content = ((HttpCommand) protocolTransportObject).getBody().toMap().get("content").toString();
        } catch (Exception ex) {
            return;
        }

        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair(PushMessageRequestBody.CONTENT, content));
        if (StringUtils.isBlank(handleMsgContext.getBizSeqNo())) {
            body.add(new BasicNameValuePair(PushMessageRequestBody.BIZSEQNO,
                RandomStringUtils.generateNum(20)));
        } else {
            body.add(new BasicNameValuePair(PushMessageRequestBody.BIZSEQNO,
                handleMsgContext.getBizSeqNo()));
        }
        if (StringUtils.isBlank(handleMsgContext.getUniqueId())) {
            body.add(new BasicNameValuePair(PushMessageRequestBody.UNIQUEID,
                RandomStringUtils.generateNum(20)));
        } else {
            body.add(new BasicNameValuePair(PushMessageRequestBody.UNIQUEID,
                handleMsgContext.getUniqueId()));
        }

        body.add(new BasicNameValuePair(PushMessageRequestBody.RANDOMNO,
            handleMsgContext.getMsgRandomNo()));
        body.add(new BasicNameValuePair(PushMessageRequestBody.TOPIC, handleMsgContext.getTopic()));

        body.add(new BasicNameValuePair(PushMessageRequestBody.EXTFIELDS,
            JsonUtils.serialize(EventMeshUtil.getEventProp(handleMsgContext.getEvent()))));

        HttpEntity httpEntity = new UrlEncodedFormEntity(body, StandardCharsets.UTF_8);

        builder.setEntity(httpEntity);

        // for CloudEvents Webhook spec
        String urlAuthType = handleMsgContext.getConsumerGroupConfig().getConsumerGroupTopicConf()
            .get(handleMsgContext.getTopic()).getHttpAuthTypeMap().get(currPushUrl);

        WebhookUtil.setWebhookHeaders(builder, httpEntity.getContentType().getValue(), eventMeshHttpConfiguration.eventMeshWebhookOrigin,
            urlAuthType);

        eventMeshHTTPServer.metrics.getSummaryMetrics().recordPushMsg();

        this.lastPushTime = System.currentTimeMillis();

        addToWaitingMap(this);

        try {
            eventMeshHTTPServer.httpClientPool.getClient().execute(builder, new ResponseHandler<Object>() {
                @Override
                public Object handleResponse(HttpResponse response) {
                    removeWaitingMap(AsyncHTTPPushRequest.this);
                    long cost = System.currentTimeMillis() - lastPushTime;
                    eventMeshHTTPServer.metrics.getSummaryMetrics().recordHTTPPushTimeCost(cost);

                    if (processResponseStatus(response.getStatusLine().getStatusCode(), response)) {
                        // this is successful response, process response payload
                        String res = "";
                        try {
                            res = EntityUtils.toString(response.getEntity(),
                                Charset.forName(EventMeshConstants.DEFAULT_CHARSET));
                        } catch (IOException e) {
                            handleMsgContext.finish();
                            return new Object();
                        }
                        ClientRetCode result = processResponseContent(res);
                       
                        if (result == ClientRetCode.OK) {
                            
                        } else if (result == ClientRetCode.RETRY) {
                            delayRetry();
                            if (isComplete()) {
                                handleMsgContext.finish();
                            }
                        } else if (result == ClientRetCode.NOLISTEN) {
                           
                        } else if (result == ClientRetCode.FAIL) {
                            
                        }
                    } else {
                        eventMeshHTTPServer.metrics.getSummaryMetrics().recordHttpPushMsgFailed();
                        messageLogger.info(
                            "message|eventMesh2client|exception|url={}|topic={}|bizSeqNo={}"
                                + "|uniqueId={}|cost={}", currPushUrl, handleMsgContext.getTopic(),
                            handleMsgContext.getBizSeqNo(), handleMsgContext.getUniqueId(), cost);

                        if (isComplete()) {
                            handleMsgContext.finish();
                        }
                    }
                    
                }
            });

       
	}
	
	@Override
	public void downstreamMessage(EventMeshRequest request, DownstreamHandler downstreamHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void downstream(EventMeshResponse response, DownstreamHandler downstreamHandler) {
		// TODO Auto-generated method stub
		
	}

}
