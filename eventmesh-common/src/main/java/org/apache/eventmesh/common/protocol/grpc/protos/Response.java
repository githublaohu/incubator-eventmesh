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

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: eventmesh-client.proto

package org.apache.eventmesh.common.protocol.grpc.protos;

import java.util.Objects;

import com.google.protobuf.ByteString;

/**
 * Protobuf type {@code eventmesh.common.protocol.grpc.Response}
 */
@SuppressWarnings({"all"})
public final class Response extends com.google.protobuf.GeneratedMessageV3 implements
        // @@protoc_insertion_point(message_implements:eventmesh.common.protocol.grpc.Response)
        ResponseOrBuilder {
    private static final long serialVersionUID = -6326489945644944331L;

    // Use Response.newBuilder() to construct.
    private Response(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
        super(builder);
    }

    private Response() {
        respCode_ = "";
        respMsg_ = "";
        respTime_ = "";
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
        return this.unknownFields;
    }

    private Response(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        this();
        Objects.requireNonNull(input,"CodedInputStream can not be null");
        Objects.requireNonNull(extensionRegistry,"ExtensionRegistryLite can not be null");

        int mutable_bitField0_ = 0;
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
            boolean done = false;
            while (!done) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        done = true;
                        break;
                    default: {
                        if (!parseUnknownFieldProto3(
                                input, unknownFields, extensionRegistry, tag)) {
                            done = true;
                        }
                        break;
                    }
                    case 10: {
                        respCode_ = input.readStringRequireUtf8();
                        break;
                    }
                    case 18: {
                        respMsg_ = input.readStringRequireUtf8();
                        break;
                    }
                    case 26: {
                        respTime_ = input.readStringRequireUtf8();
                        break;
                    }
                }
            }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
            throw new com.google.protobuf.InvalidProtocolBufferException(
                    e).setUnfinishedMessage(this);
        } finally {
            this.unknownFields = unknownFields.build();
            makeExtensionsImmutable();
        }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
        return EventmeshGrpc.internal_static_eventmesh_common_protocol_grpc_Response_descriptor;
    }

    protected FieldAccessorTable
    internalGetFieldAccessorTable() {
        return EventmeshGrpc.internal_static_eventmesh_common_protocol_grpc_Response_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        Response.class, Builder.class);
    }

    public static final int RESPCODE_FIELD_NUMBER = 1;
    private volatile String respCode_;

    /**
     * <code>string respCode = 1;</code>
     */
    public String getRespCode() {
        return respCode_;
    }

    /**
     * <code>string respCode = 1;</code>
     */
    public com.google.protobuf.ByteString getRespCodeBytes() {
        return ByteString.copyFromUtf8(respCode_);
    }

    public static final int RESPMSG_FIELD_NUMBER = 2;
    private volatile String respMsg_;

    /**
     * <code>string respMsg = 2;</code>
     */
    public String getRespMsg() {
        return respMsg_;
    }

    /**
     * <code>string respMsg = 2;</code>
     */
    public com.google.protobuf.ByteString getRespMsgBytes() {
        return ByteString.copyFromUtf8(respMsg_);
    }

    public static final int RESPTIME_FIELD_NUMBER = 3;
    private volatile String respTime_;

    /**
     * <code>string respTime = 3;</code>
     */
    public String getRespTime() {
        return respTime_;
    }

    /**
     * <code>string respTime = 3;</code>
     */
    public com.google.protobuf.ByteString getRespTimeBytes() {
        return ByteString.copyFromUtf8(respTime_);
    }

    private byte memoizedIsInitialized = -1;

    public final boolean isInitialized() {
        if (memoizedIsInitialized == 1) return true;
        if (memoizedIsInitialized == 0) return false;

        memoizedIsInitialized = 1;
        return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
        if (!getRespCodeBytes().isEmpty()) {
            com.google.protobuf.GeneratedMessageV3.writeString(output, 1, respCode_);
        }
        if (!getRespMsgBytes().isEmpty()) {
            com.google.protobuf.GeneratedMessageV3.writeString(output, 2, respMsg_);
        }
        if (!getRespTimeBytes().isEmpty()) {
            com.google.protobuf.GeneratedMessageV3.writeString(output, 3, respTime_);
        }
        unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) return size;

        size = 0;
        if (!getRespCodeBytes().isEmpty()) {
            size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, respCode_);
        }
        if (!getRespMsgBytes().isEmpty()) {
            size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, respMsg_);
        }
        if (!getRespTimeBytes().isEmpty()) {
            size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, respTime_);
        }
        size += unknownFields.getSerializedSize();
        memoizedSize = size;
        return size;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Response)) {
            return super.equals(obj);
        }
        Response other = (Response) obj;

        return getRespCode().equals(other.getRespCode())
                && getRespMsg().equals(other.getRespMsg())
                && getRespTime().equals(other.getRespTime())
                && unknownFields.equals(other.unknownFields);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode != 0) {
            return memoizedHashCode;
        }
        int hash = 41;
        hash = (19 * hash) + getDescriptor().hashCode();
        hash = (37 * hash) + RESPCODE_FIELD_NUMBER;
        hash = (53 * hash) + getRespCode().hashCode();
        hash = (37 * hash) + RESPMSG_FIELD_NUMBER;
        hash = (53 * hash) + getRespMsg().hashCode();
        hash = (37 * hash) + RESPTIME_FIELD_NUMBER;
        hash = (53 * hash) + getRespTime().hashCode();
        hash = (29 * hash) + unknownFields.hashCode();
        memoizedHashCode = hash;
        return hash;
    }

    public static Response parseFrom(
            java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static Response parseFrom(
            java.nio.ByteBuffer data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static Response parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static Response parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static Response parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static Response parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static Response parseFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static Response parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Response parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input);
    }

    public static Response parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static Response parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static Response parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() {
        return newBuilder();
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(Response prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE
                ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(BuilderParent parent) {
        return new Builder(parent);
    }

    /**
     * Protobuf type {@code eventmesh.common.protocol.grpc.Response}
     */
    public static final class Builder extends
            com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
            // @@protoc_insertion_point(builder_implements:eventmesh.common.protocol.grpc.Response)
            ResponseOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return EventmeshGrpc.internal_static_eventmesh_common_protocol_grpc_Response_descriptor;
        }

        protected FieldAccessorTable
        internalGetFieldAccessorTable() {
            return EventmeshGrpc.internal_static_eventmesh_common_protocol_grpc_Response_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            Response.class, Builder.class);
        }

        // Construct using org.apache.eventmesh.common.protocol.grpc.protos.Response.newBuilder()
        private Builder() {
            maybeForceBuilderInitialization();
        }

        private Builder(
                BuilderParent parent) {
            super(parent);
            maybeForceBuilderInitialization();
        }

        private void maybeForceBuilderInitialization() {
            if (com.google.protobuf.GeneratedMessageV3
                    .alwaysUseFieldBuilders) {
            }
        }

        public Builder clear() {
            super.clear();
            respCode_ = "";

            respMsg_ = "";

            respTime_ = "";

            return this;
        }

        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return EventmeshGrpc.internal_static_eventmesh_common_protocol_grpc_Response_descriptor;
        }

        public Response getDefaultInstanceForType() {
            return Response.getDefaultInstance();
        }

        public Response build() {
            Response result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        public Response buildPartial() {
            Response result = new Response(this);
            result.respCode_ = respCode_;
            result.respMsg_ = respMsg_;
            result.respTime_ = respTime_;
            onBuilt();
            return result;
        }

        public Builder clone() {
            return (Builder) super.clone();
        }

        public Builder setField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                Object value) {
            return (Builder) super.setField(field, value);
        }

        public Builder clearField(
                com.google.protobuf.Descriptors.FieldDescriptor field) {
            return (Builder) super.clearField(field);
        }

        public Builder clearOneof(
                com.google.protobuf.Descriptors.OneofDescriptor oneof) {
            return (Builder) super.clearOneof(oneof);
        }

        public Builder setRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                int index, Object value) {
            return (Builder) super.setRepeatedField(field, index, value);
        }

        public Builder addRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                Object value) {
            return (Builder) super.addRepeatedField(field, value);
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
            if (other instanceof Response) {
                return mergeFrom((Response) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(Response other) {
            if (other == Response.getDefaultInstance()) return this;
            if (!other.getRespCode().isEmpty()) {
                respCode_ = other.respCode_;
                onChanged();
            }
            if (!other.getRespMsg().isEmpty()) {
                respMsg_ = other.respMsg_;
                onChanged();
            }
            if (!other.getRespTime().isEmpty()) {
                respTime_ = other.respTime_;
                onChanged();
            }
            this.mergeUnknownFields(other.unknownFields);
            onChanged();
            return this;
        }

        public final boolean isInitialized() {
            return true;
        }

        public Builder mergeFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            Response parsedMessage = null;
            try {
                parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                parsedMessage = (Response) e.getUnfinishedMessage();
                throw e.unwrapIOException();
            } finally {
                if (parsedMessage != null) {
                    mergeFrom(parsedMessage);
                }
            }
            return this;
        }

        private String respCode_ = "";

        /**
         * <code>string respCode = 1;</code>
         */
        public String getRespCode() {
            return respCode_;
        }

        /**
         * <code>string respCode = 1;</code>
         */
        public com.google.protobuf.ByteString
        getRespCodeBytes() {
            return ByteString.copyFromUtf8(respCode_);
        }

        /**
         * <code>string respCode = 1;</code>
         */
        public Builder setRespCode(String value) {
            Objects.requireNonNull(value,"RespCode can not be null");

            respCode_ = value;
            onChanged();
            return this;
        }

        /**
         * <code>string respCode = 1;</code>
         */
        public Builder clearRespCode() {
            respCode_ = getDefaultInstance().getRespCode();
            onChanged();
            return this;
        }

        /**
         * <code>string respCode = 1;</code>
         */
        public Builder setRespCodeBytes(
                com.google.protobuf.ByteString value) {
            Objects.requireNonNull(value,"RespCodeBytes can not be null");
            checkByteStringIsUtf8(value);

            respCode_ = value.toStringUtf8();
            onChanged();
            return this;
        }

        private String respMsg_ = "";

        /**
         * <code>string respMsg = 2;</code>
         */
        public String getRespMsg() {
            return respMsg_;
        }

        /**
         * <code>string respMsg = 2;</code>
         */
        public com.google.protobuf.ByteString getRespMsgBytes() {
            return ByteString.copyFromUtf8(respMsg_);
        }

        /**
         * <code>string respMsg = 2;</code>
         */
        public Builder setRespMsg(String value) {
            Objects.requireNonNull(value,"RespMsg can not be null");

            respMsg_ = value;
            onChanged();
            return this;
        }

        /**
         * <code>string respMsg = 2;</code>
         */
        public Builder clearRespMsg() {

            respMsg_ = getDefaultInstance().getRespMsg();
            onChanged();
            return this;
        }

        /**
         * <code>string respMsg = 2;</code>
         */
        public Builder setRespMsgBytes(
                com.google.protobuf.ByteString value) {
            Objects.requireNonNull(value,"RespMsgBytes can not be null");
            checkByteStringIsUtf8(value);

            respMsg_ = value.toStringUtf8();
            onChanged();
            return this;
        }

        private String respTime_ = "";

        /**
         * <code>string respTime = 3;</code>
         */
        public String getRespTime() {
            return respTime_;
        }

        /**
         * <code>string respTime = 3;</code>
         */
        public com.google.protobuf.ByteString getRespTimeBytes() {
            return ByteString.copyFromUtf8(respTime_);
        }

        /**
         * <code>string respTime = 3;</code>
         */
        public Builder setRespTime(
                String value) {
            Objects.requireNonNull(value,"RespTime can not be null");

            respTime_ = value;
            onChanged();
            return this;
        }

        /**
         * <code>string respTime = 3;</code>
         */
        public Builder clearRespTime() {

            respTime_ = getDefaultInstance().getRespTime();
            onChanged();
            return this;
        }

        /**
         * <code>string respTime = 3;</code>
         */
        public Builder setRespTimeBytes(
                com.google.protobuf.ByteString value) {
            Objects.requireNonNull(value,"RespTimeBytes can not be null");
            checkByteStringIsUtf8(value);

            respTime_ = value.toStringUtf8();
            onChanged();
            return this;
        }

        public final Builder setUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields) {
            return super.setUnknownFieldsProto3(unknownFields);
        }

        public final Builder mergeUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields) {
            return super.mergeUnknownFields(unknownFields);
        }


        // @@protoc_insertion_point(builder_scope:eventmesh.common.protocol.grpc.Response)
    }

    // @@protoc_insertion_point(class_scope:eventmesh.common.protocol.grpc.Response)
    private static final Response DEFAULT_INSTANCE;

    static {
        DEFAULT_INSTANCE = new Response();
    }

    public static Response getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Response>
            PARSER = new com.google.protobuf.AbstractParser<Response>() {
        public Response parsePartialFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return new Response(input, extensionRegistry);
        }
    };

    public static com.google.protobuf.Parser<Response> parser() {
        return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<Response> getParserForType() {
        return PARSER;
    }

    public Response getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

}

