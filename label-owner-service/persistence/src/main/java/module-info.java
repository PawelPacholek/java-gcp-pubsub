module java.gcp.pubsub.label.owner.service.persistence.main {
    requires java.gcp.pubsub.label.owner.service.domain.main;
    requires com.google.api.apicommon;
    requires com.google.common;
    requires gax;
    requires google.cloud.pubsub;
    requires proto.google.cloud.pubsub.v1;
    requires protobuf.java;
    requires spring.cloud.gcp.autoconfigure;
    requires spring.context;
    requires spring.integration.core;
    requires spring.cloud.gcp.pubsub;
    requires spring.messaging;
    requires spring.beans;
    requires spring.boot.autoconfigure;
}