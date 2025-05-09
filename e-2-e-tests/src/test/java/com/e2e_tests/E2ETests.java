package com.e2e_tests;

import com.e2e_tests.label_owner_service.LabelOwnerService;
import com.e2e_tests.main_owner_service.MainOwnerService;
import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.pubsub_emulator.PubSubEmulator;
import com.pubsub_emulator.PubSubEmulatorInitializer;
import org.apache.commons.lang3.ThreadUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.impl.io.DefaultClassicHttpRequestFactory;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.AfterEach;
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
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = E2ETests.App.class)
@ContextConfiguration(initializers = PubSubEmulatorInitializer.class)
public class E2ETests {

  @SpringBootApplication
  public static class App {

    public static void main(String... args) {
      SpringApplication.run(App.class, args);
    }

  }

  @Autowired
  PubSubAdmin admin;
  @Autowired
  PubSubTemplate template;

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

  @AfterEach
  public void cleanPubsubEmulator() {
    PubSubEmulator.deleteTopicAndSubscription(
      "labeledOwner",
      "main-owner-service-to-labeledOwner-subscription"
    );
    PubSubEmulator.deleteTopicAndSubscription(
      "initialOwner",
      "label-owner-service-to-initialOwner-subscription"
    );
  }

  @Test
  public void simpleEndToEndTest() {
    PubSubEmulator.EmulatorProperties emulatorProperties = PubSubEmulator.properties();
    GenericContainer mainOwnerService = MainOwnerService.startContainer(emulatorProperties);
    GenericContainer labelOwnerService = LabelOwnerService.startContainer(emulatorProperties);
    try {
      System.out.println("Main owner service container name: " + mainOwnerService.getContainerName());
      System.out.println("Main owner service debugger mapped port: " + mainOwnerService.getMappedPort(5005));
      System.out.println("Label owner service container name: " + labelOwnerService.getContainerName());
      System.out.println("Label owner service debugger mapped port: " + labelOwnerService.getMappedPort(5006));

      int mainOwnerServiceMappedPort = mainOwnerService.getMappedPort(8080);
      Long ownerId = 7L;

      ClassicHttpRequest uploadRequest = createUploadRequest(ownerId, mainOwnerServiceMappedPort);
      MyResponse uploadResponse = sendRequest(uploadRequest);
      System.out.println(uploadResponse);

      sleep(5);

      List<String> messages = PubSubEmulator.fetchRawMessages("label-owner-service-to-initialOwner-subscription");
      System.out.println("Messages size: " + messages.size());

      sleep(5);

      ClassicHttpRequest fetchRequest = createFetchRequest(ownerId, mainOwnerServiceMappedPort);
      MyResponse fetchResponse = sendRequest(fetchRequest);
      System.out.println(fetchResponse);
      System.out.println("-main-owner-service-logs-start-");
      System.out.println(mainOwnerService.getLogs());
      System.out.println("-main-owner-service-logs-end-");
      System.out.println("-label-owner-service-logs-start-");
      System.out.println(labelOwnerService.getLogs());
      System.out.println("-label-owner-service-logs-end-");
    } finally {
      mainOwnerService.stop();
      labelOwnerService.stop();
    }
  }

  private ClassicHttpRequest createUploadRequest(Long ownerId, int mainOwnerServiceMappedPort) {
    String body = """
        {"id":%s,"name":"name1","address":"","phone":"+48phone3","email":"email4"}""".formatted(ownerId);
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
