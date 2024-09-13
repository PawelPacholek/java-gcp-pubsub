open module java.gcp.pubsub.main.owner.service.domain.main {
    exports com.main_owner_service.domain.usecases to java.gcp.pubsub.main.owner.service.api.main;
    exports com.main_owner_service.domain.ports to java.gcp.pubsub.main.owner.service.persistence.main, java.gcp.pubsub.main.owner.service.api.main;
    exports com.main_owner_service.domain.models to java.gcp.pubsub.main.owner.service.persistence.main, java.gcp.pubsub.main.owner.service.api.main;
}
