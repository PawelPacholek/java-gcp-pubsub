package com.main_owner_service.persistence;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
public class PersistenceConfiguration {

  @Autowired
  private MyGate gateway;

  @Bean
  public OwnerGatewayImp ownerGatewayImp() {
    return new OwnerGatewayImp();
  }

  @Bean
  public PubsubInitialOwnerSender pubsubInitialOwnerSender(
      //    @Qualifier("initialOwnerChannel") MessageChannel initialOwnerChannel
       MyGate gateway
  ) {
    //return new PubsubInitialOwnerSender(initialOwnerChannel);
    return new PubsubInitialOwnerSender(gateway);
  }

  @MessagingGateway(defaultRequestChannel = "initialOwnerChannel")
  public interface MyGate {
    void send(String out);
  }

  @Bean
  public MessageChannel initialOwnerChannel() {
    return new PublishSubscribeChannel();
  }

  // Create an outbound channel adapter to send messages from the input message channel to the topic `initialOwner`.
  @Bean
  @ServiceActivator(inputChannel = "initialOwnerChannel")
  public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
    PubSubMessageHandler adapter = new PubSubMessageHandler(pubsubTemplate, "initialOwner");

    adapter.setSuccessCallback(
            ((ackId, message) ->
                    System.out.println("Message was sent via the outbound channel adapter to topic-two!")));

    adapter.setFailureCallback(
            (cause, message) -> System.err.println("Error sending " + message + " due to " + cause));

    return adapter;
  }

}
