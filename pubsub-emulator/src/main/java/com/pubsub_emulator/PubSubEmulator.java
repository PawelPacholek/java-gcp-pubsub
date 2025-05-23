package com.pubsub_emulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.protobuf.AbstractMessage;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.RecursiveComparisonAssert;
import org.testcontainers.containers.PubSubEmulatorContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

public class PubSubEmulator {

  private static final String PROJECT_ID = "test-project-bla";
  private static String EMULATOR_HOST;

  private static PubSubEmulatorContainer shared;
  private static MemoizedSupplier<PubSubAdmin> admin;
  private static MemoizedSupplier<PubSubTemplate> template;

  static void startIfNeeded(Supplier<PubSubAdmin> adminSupplier, Supplier<PubSubTemplate> templateSupplier) {
    PubSubEmulator.admin = new MemoizedSupplier<>(adminSupplier);
    PubSubEmulator.template = new MemoizedSupplier<>(templateSupplier);
    if (shared != null)
      return;
    var imageName = DockerImageName.parse("gcr.io/google.com/cloudsdktool/cloud-sdk:420.0.0-emulators");
    shared = new PubSubEmulatorContainer(imageName).withLogConsumer(frame -> System.out.println(frame.getUtf8String()));
    shared.start();
    EMULATOR_HOST = requireNonNull(shared).getEmulatorEndpoint();
  }

  public static EmulatorProperties properties() {
    return new EmulatorProperties(PROJECT_ID, EMULATOR_HOST);
  }

  static Map<String, Object> propertiesMap() {
    return Map.of(
      "spring.cloud.gcp.pubsub.emulator-host", EMULATOR_HOST,
      "spring.cloud.gcp.project-id", PROJECT_ID
    );
  }

  public static <T> RecursiveComparisonAssert<?> assertSingleMessageRecursively(String subscriptionId, Class<T> mapping) {
    return assertSingleMessage(subscriptionId, mapping)
      .satisfies(message ->
        assertThat(message).extracting("request.id")
          .extracting(o -> UUID.fromString(o.toString()))
          .isNotNull()
      )
      .usingRecursiveComparison()
      .ignoringFields("request.id");
  }

  public static <T> ObjectAssert<T> assertSingleMessage(String subscriptionId, Class<T> mapping) {
    return Assertions.assertThat(fetchMessages(subscriptionId, message -> deserialize(message, mapping)))
      .singleElement();
  }

  public static <T> List<T> fetchMessages(String subscriptionId, Function<String, T> deserializer) {
    return fetchMessages(subscriptionId, (event, message) -> deserializer.apply(message));
  }

  public static <T> List<T> fetchMessages(String subscriptionId, BiFunction<String, String, T> deserializer) {
    return template.get().pullAndAck(subscriptionId, 100, true)
      .stream()
      .map(m -> deserializer.apply(m.getAttributesOrDefault("event", ""), m.getData().toStringUtf8()))
      .toList();
  }

  public static List<String> fetchRawMessages(String subscriptionId) {
    return template.get().pullAndAck(subscriptionId, 100, true)
      .stream()
      .map(AbstractMessage::toString)
      .toList();
  }

  public static void publish(String topic, Object message) {
    template.get().publish(topic, message, Map.of("errorChannel", "", "replyChannel", "nullChannel"));
  }

  public static void createTopicAndSubscription(String topicId, String subscriptionId) {
    admin.get().createTopic(topicId);
    admin.get().createSubscription(subscriptionId, topicId);
  }

  public static void deleteTopicAndSubscription(String topicId, String subscriptionId) {
    admin.get().deleteSubscription(subscriptionId);
    admin.get().deleteTopic(topicId);
  }

  private static <T> T deserialize(String message, Class<T> mapping) {
      try {
          return new ObjectMapper().readValue(message, mapping);
      } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
      }
  }

  public record EmulatorProperties(String projectId, String emulatorEndpoint) {}

}
