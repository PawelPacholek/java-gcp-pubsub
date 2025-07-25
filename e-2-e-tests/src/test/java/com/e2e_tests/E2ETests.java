package com.e2e_tests;

import com.e2e_tests.label_owner_service.LabelOwnerService;
import com.e2e_tests.main_owner_service.MainOwnerService;
import com.pubsub_emulator.PubSubEmulator;
import com.pubsub_emulator.PubSubEmulatorInitializer;
import com.redis_instance.RedisInstance;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.impl.io.DefaultClassicHttpRequestFactory;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = E2ETests.App.class)
@ExtendWith(RedisInstance.Shared.class)
@ContextConfiguration(initializers = PubSubEmulatorInitializer.class)
public class E2ETests {

  @SpringBootApplication
  public static class App {

    public static void main(String... args) {
      SpringApplication.run(App.class, args);
    }

  }

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
    RedisInstance.RedisProperties redisProperties = RedisInstance.getProperties();
    GenericContainer mainOwnerService = MainOwnerService.startContainer(emulatorProperties, redisProperties);
    GenericContainer labelOwnerService = LabelOwnerService.startContainer(emulatorProperties);
    try {
      System.out.println("Main owner service container name: " + mainOwnerService.getContainerName());
      System.out.println("Main owner service debugger mapped port: " + mainOwnerService.getMappedPort(5005));
      System.out.println("Label owner service container name: " + labelOwnerService.getContainerName());
      System.out.println("Label owner service debugger mapped port: " + labelOwnerService.getMappedPort(5006));

      String host = "http://localhost";
      int mainOwnerServicePort = mainOwnerService.getMappedPort(8080);
      Long ownerId = 7L;

      ClassicHttpRequest uploadRequest = createUploadRequest(ownerId, host, mainOwnerServicePort);
      MyResponse uploadResponse = sendRequest(uploadRequest);
      System.out.println(uploadResponse);

      sleep(10);

      ClassicHttpRequest fetchRequest = createFetchRequest(ownerId, host, mainOwnerServicePort);
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

  @Test
  public void simpleProductionTest() throws IOException {
    String url = "https://main-owner-service-275334369032.europe-central2.run.app";
    int mainOwnerServicePort = 443;
    String authorization = "Bearer " + new String(E2ETests.class.getResourceAsStream("/token.log").readAllBytes());

    Long ownerId = System.currentTimeMillis();

    ClassicHttpRequest uploadRequest = createUploadRequest(ownerId, url, mainOwnerServicePort);
    uploadRequest.setHeader("Authorization", authorization);
    MyResponse uploadResponse = sendRequest(uploadRequest);
    System.out.println(uploadResponse);

    sleep(10);

    ClassicHttpRequest fetchRequest = createFetchRequest(ownerId, url, mainOwnerServicePort);
    fetchRequest.setHeader("Authorization", authorization);
    MyResponse fetchResponse = sendRequest(fetchRequest);
    System.out.println(fetchResponse);
  }

  private ClassicHttpRequest createUploadRequest(Long ownerId, String host, int mainOwnerServicePort) {
    String body = """
        {"id":%s,"name":"name1","address":"","phone":"+48phone3","email":"email4"}""".formatted(ownerId);
    String uploadUri = "%s:%s/upload-initial-owner".formatted(host, mainOwnerServicePort);
    ClassicHttpRequest uploadRequest = DefaultClassicHttpRequestFactory.INSTANCE.newHttpRequest("POST", uploadUri);
    uploadRequest.setEntity(new StringEntity(body));
    uploadRequest.setHeader("Content-Type", "application/json");
    return uploadRequest;
  }

  private ClassicHttpRequest createFetchRequest(Long ownerId, String host, int mainOwnerServicePort) {
    String fetchUri = "%s:%s/fetch-labeled-owner/%s".formatted(host, mainOwnerServicePort, ownerId);
    return DefaultClassicHttpRequestFactory.INSTANCE.newHttpRequest("GET", fetchUri);
  }

  private void sleep(int seconds) {
    try {
      Thread.sleep(Duration.ofSeconds(seconds));
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
