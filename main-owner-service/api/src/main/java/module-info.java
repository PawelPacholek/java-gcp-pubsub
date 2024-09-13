open module java.gcp.pubsub.main.owner.service.api.main {
    requires java.gcp.pubsub.main.owner.service.domain.main;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.lang3;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    requires spring.integration.core;
    requires spring.messaging;
    requires jdk.unsupported;
    requires spring.cloud.gcp.pubsub;
    uses com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
}