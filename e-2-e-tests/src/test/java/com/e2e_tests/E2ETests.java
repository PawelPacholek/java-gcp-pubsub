package com.e2e_tests;

import com.e2e_tests.label_owner_service.LabelOwnerService;
import com.e2e_tests.main_owner_service.MainOwnerService;
import com.e2e_tests.pubsub_emulator.PubSubEmulator;
import com.e2e_tests.pubsub_emulator.PubSubEmulatorInitializer;
import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.apache.commons.lang3.ThreadUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.impl.io.DefaultClassicHttpRequestFactory;
import org.apache.hc.core5.http.io.entity.EntityUtils;
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
import java.time.Duration;

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

      ClassicHttpRequest uploadRequest = createUploadRequest(ownerId, mainOwnerServiceMappedPort);
      MyResponse uploadResponse = sendRequest(uploadRequest);
      System.out.println(uploadResponse);

      sleep(10);

      ClassicHttpRequest fetchRequest = createFetchRequest(8L, mainOwnerServiceMappedPort);
      MyResponse fetchResponse = sendRequest(fetchRequest);
      System.out.println(fetchResponse);
    } finally {
      mainOwnerService.stop();
      labelOwnerService.stop();
    }
  }

  private ClassicHttpRequest createUploadRequest(Long ownerId, int mainOwnerServiceMappedPort) {
    String body = """
        {"id":%s,"name":"name1","address":"address2","phone":"phone3","email":"email4"}""".formatted(ownerId);
    String uploadUri = "http://localhost:%s/upload-initial-owner".formatted(mainOwnerServiceMappedPort);
    ClassicHttpRequest uploadRequest = DefaultClassicHttpRequestFactory.INSTANCE.newHttpRequest("POST", uploadUri);
    uploadRequest.setEntity(new StringEntity(body));
    uploadRequest.setHeader("Content-Type", "application/json");
    return uploadRequest;
  }

  private ClassicHttpRequest createFetchRequest(Long ownerId, int mainOwnerServiceMappedPort) {
    String fetchUri = "http://localhost:%s/fetch-labeled-owner/%s".formatted(mainOwnerServiceMappedPort, ownerId);
    return DefaultClassicHttpRequestFactory.INSTANCE.newHttpRequest("GET", fetchUri);
  }

  private void sleep(int seconds) {
    try {
      ThreadUtils.sleep(Duration.ofSeconds(seconds));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private MyResponse sendRequest(ClassicHttpRequest apacheRequest) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      return httpClient.execute(
        apacheRequest,
        r -> new MyResponse(r.getCode(), EntityUtils.toString(r.getEntity()))
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e.getMessage(), e);
    }
  }

  private record MyResponse(int status, String body) {}

}
