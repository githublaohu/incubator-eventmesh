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

import org.apache.eventmesh.common.protocol.SubscriptionItem;
import org.apache.eventmesh.common.protocol.http.body.client.SubscribeRequestBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.alibaba.csp.sentinel.slots.statistic.metric.ArrayMetric;

import lombok.Data;

@Data
public abstract class AbstractSession<T> implements Session{

	protected T channel;
		
    private long createTime = System.currentTimeMillis();

    private long lastHeartbeatTime = System.currentTimeMillis();

    private long isolateTime = 0;
    
    private SubscribeRequestBody subscribeRequestBody;
    
    private AtomicBoolean isClose = new AtomicBoolean(false);
    
    private Map<String , SubscriptionItem> subscriptionItemMap;
    
    
    /**
     * service
     * consumer
     * topic
     * connect
     */
    private List<ArrayMetric> arrayMetricList;
    
    private ArrayMetric arrayMetric;
    
    private Map<String,Object> header;
    
    
    
    protected SubscriptionItem getSubscriptionItem(String topic) {
    	return subscriptionItemMap.get(topic);
    }
    
    public void heartbeat() {
    	lastHeartbeatTime = System.currentTimeMillis();
    }
    
    
    public void close() {
    	this.isClose.compareAndSet(false, true);
    	
    }
    
    public boolean isClose() {
    	return this.isClose.get();
    }
    
    protected void before() {
    	for(ArrayMetric arrayMetric : arrayMetricList) {
    		arrayMetric.addPass(1);
    	}
    }
    
    protected void after() {
    	for(ArrayMetric arrayMetric : arrayMetricList) {
    		arrayMetric.addException(1);
    	}
    }
    
    protected void exception(Throwable e) {
    	for(ArrayMetric arrayMetric : arrayMetricList) {
    		arrayMetric.addException(1);
    	}
    }
    
}
