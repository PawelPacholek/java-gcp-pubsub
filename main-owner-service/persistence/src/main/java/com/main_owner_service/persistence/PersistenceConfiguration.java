package com.main_owner_service.persistence;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableIntegration
public class PersistenceConfiguration {

  @Bean
  JedisConnectionFactory jedisConnectionFactory (
    @Value("${labeled.owner.host}") String host,
    @Value("${labeled.owner.port}") int port
  ) {
    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);
    JedisClientConfiguration clientConfig = JedisClientConfiguration.builder().build();
    return new JedisConnectionFactory(redisConfig, clientConfig);
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

/*
  @Bean
  public OwnerGatewayInMemoryImp ownerGatewayImp() {
    return new OwnerGatewayInMemoryImp();
  }
*/
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
