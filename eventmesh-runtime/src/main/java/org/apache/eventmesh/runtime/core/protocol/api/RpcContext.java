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
package org.apache.eventmesh.runtime.core.protocol.api;

import org.apache.eventmesh.common.protocol.http.common.EventMeshRetCode;
import org.apache.eventmesh.common.protocol.tcp.Command;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public abstract class RpcContext {
	
	protected Object object;
	
	protected String identifying;
	
	protected EventMeshRequest request;
	
	@Getter
	protected EventMeshResponse response;

	
	@Getter
	protected Command requestCommand;
	
	@Getter
	protected Command responseCommand;
	
	protected boolean isSuccess = true;
	
	protected boolean newProtocol = false;
	
	protected Throwable throwable;
	
	@Setter
	protected Map<String,String> serviceContextData;
	
	protected void response(String message, int code) {
		
	}
	
	
	protected abstract void send();
	
	public abstract String sessionId() ;
	
	public abstract ProtocolType protocolType();
	
	public String identifying() {
		return identifying;
	}
	
	public EventMeshRequest request() {
		return request;
	}

	public void setResponseJsonBody(String body) {
	}

	public void setResponseTextBody(String body) {
	}

	public void sendResponse(Map<String, Object> responseHeaderMap, Map<String, Object> responseBodyMap) {

	}

	public void sendErrorResponse() {
		this.sendErrorResponse(null);
	}
	
	public void sendErrorResponse(Throwable throwable) {
		this.throwable = throwable;
	}
	
	public void sendErrorResponse(EventMeshRetCode retCode, Map<String, Object> responseHeaderMap,
			Object responseBodyMap) {
		this.isSuccess = false;
		this.response.setHeaders(responseHeaderMap);
		this.response.setBody(new byte[1024]);
	}
	

	public void sendErrorResponse(EventMeshRetCode retCode,String errorMessage) {
		this.isSuccess = false;
	}
	
	public Map<String, Object> requestHeadler(){
		return this.request.getHeaders();
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject() {
		return (T) this.object;
	}

}
