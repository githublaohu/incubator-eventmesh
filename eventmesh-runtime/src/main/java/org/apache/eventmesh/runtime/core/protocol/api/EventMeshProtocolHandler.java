package org.apache.eventmesh.runtime.core.protocol.api;

public interface EventMeshProtocolHandler {

	public EventMeshResponse handler(EventMeshRequest eventMeshRequest, RpcContext rpcContext);
}
