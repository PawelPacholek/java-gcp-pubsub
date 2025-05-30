package com.main_owner_service.persistence;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
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

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Duration;

@Configuration
@EnableIntegration
public class PersistenceConfiguration {

  @Bean
  JedisConnectionFactory jedisConnectionFactory (
    @Value("${session.state.datasource.host}") String host,
    @Value("${session.state.datasource.port}") int port,
    @Value("${session.state.datasource.password}") String password,
    @Value("${session.state.datasource.certificate}") String certificateAsPem
  ) throws Exception {
    // 1. Załaduj certyfikat z formatu PEM
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    ByteArrayInputStream certInputStream = new ByteArrayInputStream(certificateAsPem.getBytes(StandardCharsets.UTF_8));
    X509Certificate cert = (X509Certificate) cf.generateCertificate(certInputStream);

    // 2. Utwórz TrustStore i załaduj certyfikat
    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(null); // pusty keystore
    trustStore.setCertificateEntry("redis", cert);

    // 3. Zbuduj TrustManager z TrustStore
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);

    // 4. Zbuduj SSLContext
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tmf.getTrustManagers(), null);

    // 5. Skonfiguruj połączenie Redis z SSL
    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host, port);
    redisConfig.setPassword(password);

    JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
      .useSsl()
      .sslSocketFactory(sslContext.getSocketFactory())
      //.connectTimeout(Duration.ofSeconds(5))
      //.readTimeout(Duration.ofSeconds(5))
      //.usePooling()
      .build();

    return new JedisConnectionFactory(redisConfig, clientConfig);

    /*
    JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
    jedisConFactory.setHostName(host);
    jedisConFactory.setPort(port);
    jedisConFactory.setPassword(password);
    return jedisConFactory;
    */

  }

/*
  @Bean
  public LettuceConnectionFactory redisConnectionFactory(
    @Value("${session.state.datasource.host}") String host,
    @Value("${session.state.datasource.port}") int port,
    @Value("${session.state.datasource.password}") String password,
    @Value("${session.state.datasource.certificate}") String certificateAsPem
  ) {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
    configuration.setHostName(host);
    configuration.setPort(port);
    configuration.setPassword(password);
    TrustManagerFactory instance = TrustManagers.factoryFrom(certificateAsPem);
    ClientOptions sslOptions = ClientOptions.builder()
      .sslOptions(SslOptions.builder().trustManager(instance).build())
      .build();
    return new LettuceConnectionFactory(
      configuration,
      LettuceClientConfiguration.builder()
        .clientOptions(sslOptions)
        .useSsl()
        .disablePeerVerification()
        .build()
    );
  }
*/
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
