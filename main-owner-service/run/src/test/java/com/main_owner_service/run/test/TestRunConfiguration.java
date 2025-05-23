package com.main_owner_service.run.test;

import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.MessageChannel;

@TestConfiguration
public class TestRunConfiguration {

    @Bean
    @Primary
    public String initializePubSubResources(PubSubAdmin pubSubAdmin) {
        String topic = "labeledOwner";
        String subscription = "main-owner-service-to-labeledOwner-subscription";
        if (pubSubAdmin.getTopic(topic) == null)
            pubSubAdmin.createTopic(topic);
        if (pubSubAdmin.getSubscription(subscription) == null)
            pubSubAdmin.createSubscription(subscription, topic);
        return "pubSubResourcesInitialized";
    }

    @Bean
    @Primary
    @DependsOn("initializePubSubResources")
    public PubSubInboundChannelAdapter testInboundChannelAdapter(
      @Qualifier("labeledOwnerChannel") MessageChannel messageChannel,
      PubSubTemplate pubSubTemplate
    ) {
        var adapter = new PubSubInboundChannelAdapter(
          pubSubTemplate,
          "main-owner-service-to-labeledOwner-subscription"
        );
        adapter.setOutputChannel(messageChannel);
        adapter.setAckMode(AckMode.MANUAL);
        //adapter.setPayloadType(LabeledOwner.class);
        adapter.setPayloadType(String.class);
        return adapter;
    }

}
