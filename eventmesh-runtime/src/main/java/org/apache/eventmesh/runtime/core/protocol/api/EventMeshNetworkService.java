package org.apache.eventmesh.runtime.core.protocol.api;

import org.apache.eventmesh.runtime.metrics.http.HTTPMetricsServer;
import org.apache.http.protocol.HttpProcessor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import lombok.Setter;

/**
 * 统一：
 * 1. metrics
 * 2. trace
 * 3. logger
 * 4. acl
 * 5. Error
 * @author laohu
 *
 */
public class EventMeshNetworkService {

    private final Logger httpServerLogger = LoggerFactory.getLogger(this.getClass());

    private final Logger httpLogger = LoggerFactory.getLogger("Network");

    private final Map<String, EventMeshProtocolHandler> httpProcessorMap = new ConcurrentHashMap<>();

    @Setter
    private HTTPMetricsServer metrics;

    @Setter
    private HTTPTrace httpTrace;
    
    // acl

    public DefaultHttpDataFactory defaultHttpDataFactory = new DefaultHttpDataFactory(false);


    public void init() {
        httpServerLogger.info("HandlerService start ");
    }

    public void register(HttpProcessor httpProcessor, ThreadPoolExecutor threadPoolExecutor) {
        for (String path : httpProcessor.paths()) {
            this.register(path, httpProcessor, threadPoolExecutor);
        }
    }

    public void register(String path, HttpProcessor httpProcessor, ThreadPoolExecutor threadPoolExecutor) {

        if (httpProcessorMap.containsKey(path)) {
            throw new RuntimeException(String.format("HandlerService path %s repeat, repeat processor is %s ",
                path, httpProcessor.getClass().getSimpleName()));
        }
        ProcessorWrapper processorWrapper = new ProcessorWrapper();
        processorWrapper.threadPoolExecutor = threadPoolExecutor;
        if (httpProcessor instanceof AsyncHttpProcessor) {
            processorWrapper.async = (AsyncHttpProcessor) httpProcessor;
        }
        processorWrapper.httpProcessor = httpProcessor;
        processorWrapper.traceEnabled = httpProcessor.getClass().getAnnotation(EventMeshTrace.class).isEnable();
        httpProcessorMap.put(path, processorWrapper);
        httpServerLogger.info("path is {}  processor name is {}", path, httpProcessor.getClass().getSimpleName());
    }
    
    public boolean isProcessorWrapper(HttpRequest httpRequest) {
        return Objects.nonNull(this.getProcessorWrapper(httpRequest));
    }

    private ProcessorWrapper getProcessorWrapper(HttpRequest httpRequest) {
        String uri = httpRequest.uri();
        for (Entry<String, ProcessorWrapper> e : httpProcessorMap.entrySet()) {
            if (uri.startsWith(e.getKey())) {
                return e.getValue();
            }
        }
        return null;
    }

    /**
     * @param httpRequest
     */
    public void handler(RpcContext rpcContext, ThreadPoolExecutor asyncContextCompleteHandler) {

        ProcessorWrapper processorWrapper = getProcessorWrapper(httpRequest);
        if (Objects.isNull(processorWrapper)) {
            this.sendResponse(ctx, httpRequest, HttpResponseUtils.createNotFound());
            return;
        }
        TraceOperation traceOperation = httpTrace.getTraceOperation(httpRequest, ctx.channel(), processorWrapper.traceEnabled);
        try {
            HandlerSpecific handlerSpecific = new HandlerSpecific();
            handlerSpecific.request = httpRequest;
            handlerSpecific.ctx = ctx;
            handlerSpecific.traceOperation = traceOperation;
            handlerSpecific.asyncContext = new AsyncContext<>(new HttpEventWrapper(), null, asyncContextCompleteHandler);
            processorWrapper.threadPoolExecutor.execute(handlerSpecific);
        } catch (Exception e) {
            httpServerLogger.error(e.getMessage(), e);
            this.sendResponse(ctx, httpRequest, HttpResponseUtils.createInternalServerError());
        }
    }
	
}
