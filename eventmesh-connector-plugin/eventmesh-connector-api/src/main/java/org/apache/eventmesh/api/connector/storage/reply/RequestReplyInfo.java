package org.apache.eventmesh.api.connector.storage.reply;

import org.apache.eventmesh.api.RequestReplyCallback;

import io.cloudevents.CloudEvent;

import lombok.Data;

@Data
public class RequestReplyInfo {

    private Long storageId;

    private RequestReplyCallback requestReplyCallback;

    private String storageConnectorName;

    private Long timeOut;

    private CloudEvent cloudEvent;
}
