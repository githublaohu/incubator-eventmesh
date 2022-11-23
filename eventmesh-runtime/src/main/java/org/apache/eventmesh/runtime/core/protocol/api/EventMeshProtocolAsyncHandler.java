package org.apache.eventmesh.runtime.core.protocol.api;

public interface EventMeshProtocolAsyncHandler extends EventMeshProtocolHandler{

	
	public default EventMeshResponse handler(EventMeshRequest eventMeshRequest, RpcContext rpcContext) {
		this.asyncHandler(eventMeshRequest, rpcContext);
		return null;
	}
	
	
	public void asyncHandler(EventMeshRequest eventMeshRequest, RpcContext rpcContext);
}
