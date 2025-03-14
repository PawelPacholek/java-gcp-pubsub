package com.label_owner_service.persistence;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class PersistenceConfiguration {

  @Bean
  public MessageChannel labeledOwnerChannel() {
    return new PublishSubscribeChannel();
  }

  // Create an outbound channel adapter to send messages from the input message channel to the topic `labeledOwner`.
  @Bean
  @ServiceActivator(inputChannel = "labeledOwnerChannel")
  public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
    PubSubMessageHandler adapter = new PubSubMessageHandler(pubsubTemplate, "labeledOwner");

    adapter.setSuccessCallback(
            ((ackId, message) ->
                    System.out.println(message + " was sent via the outbound channel adapter to labeledOwner topic")));

    adapter.setFailureCallback(
            (cause, message) -> System.err.println("Error sending " + message + " due to " + cause));

    return adapter;
  }

}
