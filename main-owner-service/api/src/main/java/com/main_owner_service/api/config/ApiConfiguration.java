package com.main_owner_service.api.config;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.main_owner_service.domain.ports.InitialOwnerSender;
import com.main_owner_service.domain.ports.OwnerGateway;
import com.main_owner_service.domain.usecases.FetchLabeledOwnerUseCase;
import com.main_owner_service.domain.usecases.SaveLabeledOwnerUseCase;
import com.main_owner_service.domain.usecases.UploadInitialOwnerUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class ApiConfiguration {

  @Bean
  public FetchLabeledOwnerUseCase fetchOwnerUseCase(OwnerGateway ownerGateway) {
    return new FetchLabeledOwnerUseCase(ownerGateway);
  }

  @Bean
  public SaveLabeledOwnerUseCase saveLabeledOwnerUseCase(OwnerGateway ownerGateway) {
    return new SaveLabeledOwnerUseCase(ownerGateway);
  }

  @Bean
  public UploadInitialOwnerUseCase uploadOwnerUseCase(InitialOwnerSender initialOwnerSender) {
    return new UploadInitialOwnerUseCase(initialOwnerSender);
  }

  // Create a message channel for messages arriving from the subscription `sub-one`.
  @Bean
  public MessageChannel labeledOwnerChannel() {
    return new PublishSubscribeChannel();
  }

  // Create an inbound channel adapter to listen to the subscription `main-owner-service-to-labeledOwner-subscription`
  // and send messages to the input message channel.
  @Bean
  public PubSubInboundChannelAdapter inboundChannelAdapter(
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
