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

package org.apache.eventmesh.runtime.core.protocol.processer;

import static org.apache.eventmesh.common.protocol.tcp.Command.RESPONSE_TO_SERVER;

import org.apache.eventmesh.api.RequestReplyCallback;
import org.apache.eventmesh.api.SendCallback;
import org.apache.eventmesh.api.SendResult;
import org.apache.eventmesh.api.exception.OnExceptionContext;
import org.apache.eventmesh.common.Constants;
import org.apache.eventmesh.common.protocol.ProtocolTransportObject;
import org.apache.eventmesh.common.protocol.tcp.Command;
import org.apache.eventmesh.common.protocol.tcp.Header;
import org.apache.eventmesh.common.protocol.tcp.OPStatus;
import org.apache.eventmesh.protocol.api.ProtocolAdaptor;
import org.apache.eventmesh.protocol.api.ProtocolPluginFactory;
import org.apache.eventmesh.runtime.configuration.EventMeshTCPConfiguration;
import org.apache.eventmesh.runtime.constants.EventMeshConstants;
import org.apache.eventmesh.runtime.core.EventMeshEventCloudService;
import org.apache.eventmesh.runtime.core.protocol.api.EventMeshProtocolHandler;
import org.apache.eventmesh.runtime.core.protocol.api.EventMeshRequest;
import org.apache.eventmesh.runtime.core.protocol.api.EventMeshResponse;
import org.apache.eventmesh.runtime.core.protocol.api.ProtocolIdentifying;
import org.apache.eventmesh.runtime.core.protocol.api.RpcContext;
import org.apache.eventmesh.runtime.util.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import lombok.Setter;

@ProtocolIdentifying(tcp = { "REQUEST_TO_SERVER", "RESPONSE_TO_SERVER", "ASYNC_MESSAGE_TO_SERVER",
		"BROADCAST_MESSAGE_TO_SERVER" })
public class MessageTransferHandler implements EventMeshProtocolHandler {

	private EventMeshEventCloudService eventMeshEventCloudService;

	private EventMeshTCPConfiguration eventMeshTCPConfiguration;

	@Override
	public EventMeshResponse handler(EventMeshRequest eventMeshRequest, RpcContext rpcContext) throws Exception {
		long taskExecuteTime = System.currentTimeMillis();
		String protocolType = eventMeshRequest.getHeaders().containsKey(Constants.PROTOCOL_TYPE) ? "EventMeshMessage"
				: (String) eventMeshRequest.getHeaders().get(Constants.PROTOCOL_TYPE);

		ProtocolAdaptor<ProtocolTransportObject> protocolAdaptor = ProtocolPluginFactory
				.getProtocolAdaptor(protocolType);

		CloudEvent event = protocolAdaptor.toCloudEvent(pkg);
		if (event == null) {
			throw new Exception("event is null");
		}

		String content = new String(event.getData().toBytes(), StandardCharsets.UTF_8);
		if (content.length() > eventMeshTCPConfiguration.eventMeshEventSize) {
			throw new Exception("event size exceeds the limit: " + eventMeshTCPConfiguration.eventMeshEventSize);
		}

		event = addTimestamp(event, rpcContext.identifying(), rpcContext.getRequestTime());

		if (rpcContext.identifying().equals("REQUEST_TO_SERVER")) {
			long timeout = EventMeshConstants.DEFAULT_TIMEOUT_IN_MILLISECONDS;
			if (event.getExtension(EventMeshConstants.PROPERTY_MESSAGE_TTL) != null) {
				timeout = Long.parseLong(
						(String) Objects.requireNonNull(event.getExtension(EventMeshConstants.PROPERTY_MESSAGE_TTL)));
			}

			MessageRequestReplyCallback messageRequestReplyCallback = new MessageRequestReplyCallback();
			messageRequestReplyCallback.event = event;
			messageRequestReplyCallback.rpcContext = rpcContext;
			eventMeshEventCloudService.request(event, rpcContext, messageRequestReplyCallback, timeout);
		} else if (rpcContext.identifying().equals("RESPONSE_TO_SERVER")) {
			MessageSendCallback messageSendCallback = new MessageSendCallback();
			messageSendCallback.event = event;
			messageSendCallback.rpcContext = rpcContext;
			eventMeshEventCloudService.send(event, messageSendCallback);
		}

	}

	private CloudEvent addTimestamp(CloudEvent event, String cmd, long sendTime) {
		if (cmd.equals("RESPONSE_TO_SERVER")) {
			event = CloudEventBuilder.from(event)
					.withExtension(EventMeshConstants.RSP_C2EVENTMESH_TIMESTAMP, String.valueOf(startTime))
					.withExtension(EventMeshConstants.RSP_EVENTMESH2MQ_TIMESTAMP, String.valueOf(sendTime))
					.withExtension(EventMeshConstants.RSP_SEND_EVENTMESH_IP,
							eventMeshTCPServer.getEventMeshTCPConfiguration().eventMeshServerIp)
					.build();
		} else {
			event = CloudEventBuilder.from(event)
					.withExtension(EventMeshConstants.REQ_C2EVENTMESH_TIMESTAMP, String.valueOf(startTime))
					.withExtension(EventMeshConstants.REQ_EVENTMESH2MQ_TIMESTAMP, String.valueOf(sendTime))
					.withExtension(EventMeshConstants.REQ_SEND_EVENTMESH_IP,
							eventMeshTCPServer.getEventMeshTCPConfiguration().eventMeshServerIp)
					.build();
		}
		return event;
	}

	private class MessageRequestReplyCallback implements RequestReplyCallback {

		private RpcContext rpcContext;

		private CloudEvent event;

		@Override
		public void onSuccess(CloudEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onException(Throwable e) {
			// TODO Auto-generated method stub

		}

	}

	@Setter
	private class MessageSendCallback implements SendCallback {

		private RpcContext rpcContext;

		private CloudEvent event;

		@Override
		public void onSuccess(SendResult sendResult) {

			msg.setHeader(new Header(replyCmd, OPStatus.SUCCESS.getCode(), OPStatus.SUCCESS.getDesc(),
					pkg.getHeader().getSeq()));
			msg.setBody(event);
			Utils.writeAndFlush(msg, startTime, taskExecuteTime, session.getContext(), session);

			rpcContext.sendResponse(responseHeaderMap, responseBodyMap);

		}

		@Override
		public void onException(OnExceptionContext context) {

			rpcContext.sendErrorResponse(retCode, errorMessage);

		}

	}

}
