module java.gcp.pubsub.label.owner.service.persistence.main {
    requires java.gcp.pubsub.label.owner.service.domain.main;
    requires com.google.api.apicommon;
    requires com.google.common;
    requires gax;
    requires google.cloud.pubsub;
    requires proto.google.cloud.pubsub.v1;
    requires protobuf.java;
    requires spring.context;
}