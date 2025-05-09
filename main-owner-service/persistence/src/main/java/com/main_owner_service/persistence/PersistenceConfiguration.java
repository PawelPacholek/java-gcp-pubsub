package com.main_owner_service.persistence;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;

@Configuration
@EnableIntegration
public class PersistenceConfiguration {
/*
  @Bean
  JedisConnectionFactory jedisConnectionFactory(@Value("${redis.host}") String host, @Value("${redis.port}") int port) {
    JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
    jedisConFactory.setHostName(host);
    jedisConFactory.setPort(port);
    return jedisConFactory;
  }

  @Bean
  public RedisTemplate<Long, String> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
    RedisTemplate<Long, String> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory);
    return template;
  }

  @Bean
  public OwnerGatewayImp ownerGatewayImp(RedisTemplate<Long, String> redisClient) {
    return new OwnerGatewayImp(redisClient);
  }
*/
  @Bean
  public OwnerGatewayInMemoryImp ownerGatewayImp() {
    return new OwnerGatewayInMemoryImp();
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
                    System.out.println(message + " was sent via the outbound channel adapter to initialOwner topic")));

    adapter.setFailureCallback(
            (cause, message) -> System.err.println("Error sending " + message + " due to " + cause));

    return adapter;
  }

}
