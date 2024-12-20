package com.e2e_tests;

import com.e2e_tests.main_owner_service.MainOwnerService;
import com.e2e_tests.pubsub_emulator.PubSubEmulator;
import com.e2e_tests.pubsub_emulator.PubSubEmulatorInitializer;
import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.impl.io.DefaultClassicHttpRequestFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.http.impl.client.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

import static com.google.api.client.http.HttpMethods.POST;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
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
  @Before
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

    try {

      int mappedPort = mainOwnerService.getMappedPort(8080);

      String body = """
        {"id":7,"name":"name1","address":"address2","phone":"phone3","email":"email4"}""";
      String uri = "http://localhost:%s/upload-initial-owner".formatted(mappedPort);
      ClassicHttpRequest apacheRequest = DefaultClassicHttpRequestFactory.INSTANCE.newHttpRequest("post", uri);
      apacheRequest.setEntity(new StringEntity(body));

      try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
        var response = httpClient.execute(apacheRequest, r -> r);
        System.out.println(response);
      } catch (IOException e) {
        throw new UncheckedIOException(e.getMessage(), e);
      }

    } finally {
      mainOwnerService.stop();
    }
  }

}
