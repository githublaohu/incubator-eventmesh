package org.apache.eventmesh.runtime.core.protocol.api;

public interface EventMeshObjectProtocolHandler<T> extends EventMeshProtocolHandler{

	@SuppressWarnings("unchecked")
	public default EventMeshResponse handler(EventMeshRequest eventMeshRequest, RpcContext rpcContext) {
		return this.handler((T)rpcContext.getObject(), rpcContext);
	}
	
	public EventMeshResponse handler(T eventMeshRequest, RpcContext rpcContext);
}
