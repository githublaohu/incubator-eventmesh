package org.apache.eventmesh.runtime.core.protocol.api;

import org.apache.eventmesh.common.protocol.http.common.EventMeshRetCode;

import java.util.Map;

import io.netty.handler.codec.http.HttpResponse;

public abstract class RpcContext {

	private EventMeshRequest request;
	
	private EventMeshResponse eventMeshResponse;
	
	private Object object;

	protected abstract EventMeshRequest getRequest();

	protected abstract void sendResponse(EventMeshResponse eventMeshResponse);

	public void setResponseJsonBody(String body) {
	}

	public void setResponseTextBody(String body) {
	}

	public void sendResponse(HttpResponse response) {

	}

	public void sendResponse(Map<String, Object> responseHeaderMap, Map<String, Object> responseBodyMap) {

	}

	public void sendErrorResponse(EventMeshRetCode retCode, Map<String, Object> responseHeaderMap,
			Map<String, Object> responseBodyMap, Map<String, Object> traceMap) {

	}

	public <T> T getObject() {
		return (T) this.object;
	}

}
