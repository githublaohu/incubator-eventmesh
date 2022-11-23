package org.apache.eventmesh.runtime.core.protocol.api;

import io.netty.channel.Channel;

public class HttpRpcContext extends RpcContext{

	private Channel channle;
	
	@Override
	protected EventMeshRequest getRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void sendResponse(EventMeshResponse eventMeshResponse) {
		channle.write(eventMeshResponse);
		
	}

}
