package com.label_owner_service.api.config;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.label_owner_service.domain.helpers.LabeledOwnerSender;
import com.label_owner_service.domain.models.InitialOwner;
import com.label_owner_service.domain.usecases.LabelOwnerUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class ApiConfiguration {

  @Bean
  public LabelOwnerUseCase labelOwnerUseCase(LabeledOwnerSender labeledOwnerSender) {
    return new LabelOwnerUseCase(labeledOwnerSender);
  }

  // Create a message channel for messages arriving from the subscription `sub-one`.
  @Bean
  public MessageChannel initialOwnerChannel() {
    return new PublishSubscribeChannel();
  }

  @Bean
  public PubSubInboundChannelAdapter inboundChannelAdapter(
          @Qualifier("initialOwnerChannel") MessageChannel messageChannel,
          PubSubTemplate pubSubTemplate
  ) {
    var adapter = new PubSubInboundChannelAdapter(
            pubSubTemplate,
            "label-owner-service-to-initialOwner-subscription"
    );
    adapter.setOutputChannel(messageChannel);
    adapter.setAckMode(AckMode.MANUAL);
    adapter.setPayloadType(InitialOwner.class);
    return adapter;
  }

}
