open module java.gcp.pubsub.label.owner.service.domain.main {
    exports com.label_owner_service.domain.usecases to java.gcp.pubsub.label.owner.service.api.main;
    exports com.label_owner_service.domain.helpers to java.gcp.pubsub.label.owner.service.api.main, java.gcp.pubsub.label.owner.service.persistence.main;
    exports com.label_owner_service.domain.models to java.gcp.pubsub.label.owner.service.api.main, java.gcp.pubsub.label.owner.service.persistence.main;
}