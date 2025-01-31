package com.e2e_tests;

import com.e2e_tests.label_owner_service.LabelOwnerService;
import com.e2e_tests.main_owner_service.MainOwnerService;
import com.e2e_tests.pubsub_emulator.PubSubEmulator;
import com.e2e_tests.pubsub_emulator.PubSubEmulatorInitializer;
import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.impl.io.DefaultClassicHttpRequestFactory;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.io.UncheckedIOException;

//@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = E2ETests.App.class)
@ContextConfiguration(/*classes = E2ETestConfiguration.class, */initializers = PubSubEmulatorInitializer.class)
public class E2ETests {

  @SpringBootApplication
  public static class App {

    public static void main(String... args) {
      SpringApplication.run(App.class, args);
    }

    //     @Bean
    //   PubSubTopicClient pubSubClient(PubSubTemplate template) {
    //        return new PubSubTopicClient(template, "instance", "topic");
    //   }

  }

  //  @Autowired
  //  PubSubTopicClient pubSubTopicClient;
  @Autowired
  PubSubAdmin admin;
  @Autowired
  PubSubTemplate template;


  //  @Autowired
  //  private MockMvc mockMvc;
  @BeforeEach
  public void preparePubsubEmulator() {
    PubSubEmulator.createTopicAndSubscription(
      "labeledOwner",
      "main-owner-service-to-labeledOwner-subscription"
    );
    PubSubEmulator.createTopicAndSubscription(
      "initialOwner",
      "label-owner-service-to-initialOwner-subscription"
    );
  }

  @Test
  public void simpleEndToEndTest() {

    GenericContainer mainOwnerService = MainOwnerService.startContainer();
    GenericContainer labelOwnerService = LabelOwnerService.startContainer();

    try {

      int mainOwnerServiceMappedPort = mainOwnerService.getMappedPort(8080);

      Long ownerId = 7L;

      String body = """
        {"id":%s,"name":"name1","address":"address2","phone":"phone3","email":"email4"}""".formatted(ownerId);
      String uploadUri = "http://localhost:%s/upload-initial-owner".formatted(mainOwnerServiceMappedPort);
      ClassicHttpRequest uploadRequest = DefaultClassicHttpRequestFactory.INSTANCE.newHttpRequest("POST", uploadUri);
      uploadRequest.setEntity(new StringEntity(body));
      uploadRequest.setHeader("Content-Type", "application/json");

      ClassicHttpResponse uploadResponse = sendRequest(uploadRequest);
      System.out.println(uploadResponse);

      String fetchUri = "http://localhost:%s/fetch-labeled-owner/%s".formatted(mainOwnerServiceMappedPort, ownerId);
      ClassicHttpRequest fetchRequest = DefaultClassicHttpRequestFactory.INSTANCE.newHttpRequest("GET", fetchUri);

      ClassicHttpResponse fetchResponse = sendRequest(fetchRequest);
      System.out.println(fetchResponse);
    } finally {
      mainOwnerService.stop();
      labelOwnerService.stop();
    }
  }

  private ClassicHttpResponse sendRequest(ClassicHttpRequest apacheRequest) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      return httpClient.execute(apacheRequest, r -> r);
    } catch (IOException e) {
      throw new UncheckedIOException(e.getMessage(), e);
    }
  }


}
