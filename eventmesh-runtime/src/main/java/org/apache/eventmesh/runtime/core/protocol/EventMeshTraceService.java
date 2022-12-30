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
package org.apache.eventmesh.runtime.core.protocol;

import org.apache.eventmesh.runtime.core.protocol.api.RpcContext;
import org.apache.eventmesh.runtime.trace.TraceUtils;
import org.apache.eventmesh.trace.api.common.EventMeshTraceConstants;

import java.util.Map;

import javax.annotation.Nullable;

import io.cloudevents.CloudEvent;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class EventMeshTraceService {

	 public boolean useTrace;

	    public EventMeshTraceService(boolean useTrace) {
	        this.useTrace = useTrace;
	    }

	    public TraceOperation getTraceOperation(RpcContext rpcContext) {

	        Span span = TraceUtils.prepareServerSpan(rpcContext.requestHeadler(), EventMeshTraceConstants.TRACE_UPSTREAM_EVENTMESH_SERVER_SPAN,
	            false);
	        return new TraceOperation(span, null, useTrace);
	    }

	    @AllArgsConstructor
	    @Getter
	    public class TraceOperation {

	        private Span span;

	        private TraceOperation childTraceOperation;

	        private boolean traceEnabled;

	        public void endTrace(Object ce) {
	            if (!EventMeshTraceService.this.useTrace) {
	                return;
	            }
	            if (childTraceOperation != null) {
	                childTraceOperation.endTrace(ce);
	            }
	            try (Scope ignored = span.makeCurrent()) {
	                TraceUtils.finishSpan(span, (CloudEvent)ce);
	            }
	        }

	        public void exceptionTrace(@Nullable Throwable ex, Map<String, Object> map) {
	            if (!EventMeshTraceService.this.useTrace) {
	                return;
	            }
	            if (childTraceOperation != null) {
	                childTraceOperation.exceptionTrace(ex, map);
	            }
	            try (Scope ignored = span.makeCurrent()) {
	                TraceUtils.finishSpanWithException(span, map, ex.getMessage(), ex);
	            }
	        }

	        public void endLatestTrace(CloudEvent ce) {
	            if (childTraceOperation != null) {
	                TraceOperation traceOperation = this.childTraceOperation.getChildTraceOperation();
	                this.childTraceOperation.setChildTraceOperation(null);

	                childTraceOperation.endTrace(ce);
	                this.childTraceOperation = traceOperation;
	            }
	        }

	        public void exceptionLatestTrace(@Nullable Throwable ex, Map<String, Object> traceMap) {
	            if (childTraceOperation != null) {
	                TraceOperation traceOperation = this.childTraceOperation.getChildTraceOperation();
	                this.childTraceOperation.setChildTraceOperation(null);

	                childTraceOperation.exceptionTrace(ex, traceMap);
	                this.childTraceOperation = traceOperation;
	            }
	        }

	        public TraceOperation createClientTraceOperation(Map<String, Object> map, String spanName, boolean isSpanFinishInOtherThread) {
	            TraceOperation traceOperation = new TraceOperation(TraceUtils.prepareClientSpan(map, spanName, isSpanFinishInOtherThread),
	                null, this.traceEnabled);
	            this.setChildTraceOperation(traceOperation);
	            return traceOperation;
	        }

	        public void setChildTraceOperation(TraceOperation traceOperation) {
	            if (childTraceOperation != null) {
	                childTraceOperation.setChildTraceOperation(traceOperation);
	            }
	            this.childTraceOperation = traceOperation;
	        }

	    }
}
