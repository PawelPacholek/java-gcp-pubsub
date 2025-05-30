package com.redis_instance;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.Callable;

import static java.nio.file.attribute.PosixFilePermissions.asFileAttribute;
import static java.nio.file.attribute.PosixFilePermissions.fromString;
import static java.util.Objects.requireNonNull;

public class RedisInstance {

  public static class Shared implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
      start();
    }

  }

  private static Map<String, String> shared;

  private synchronized static void start() {
    if (isAlreadyRunning())
      return;
    shared = instantiateRedis();
  }

  private static boolean isAlreadyRunning() {
    return shared != null;
  }

  @SuppressWarnings("resource")
  private static Map<String, String> instantiateRedis() {
    String tempDirectory = createTempDir();
    copyFromResourcesTo("/redis/redis.crt", tempDirectory);
    copyFromResourcesTo("/redis/redis.key.log", tempDirectory);
    copyFromResourcesTo("/redis/redisCA.crt", tempDirectory);
    GenericContainer<?> redis = new GenericContainer<>("bitnami/redis:7.4.2")
      .withFileSystemBind(tempDirectory, "/certs")
      .withEnv("REDIS_PASSWORD", "password")
      .withEnv("REDIS_TLS_ENABLED", "yes")
      .withEnv("REDIS_TLS_AUTH_CLIENTS", "no")
      .withEnv("REDIS_TLS_PORT", "6379")
      .withEnv("REDIS_TLS_CERT_FILE", "/certs/redis.crt")
      .withEnv("REDIS_TLS_KEY_FILE", "/certs/redis.key")
      .withEnv("REDIS_TLS_CA_FILE", "/certs/redisCA.crt")
      .withExposedPorts(6379);
    redis.start();
    return Map.of(
      "session.state.datasource.host", redis.getHost(),
      "session.state.datasource.port", String.valueOf(redis.getMappedPort(6379)),
      "session.state.datasource.password", "password",
      "session.state.datasource.certificate", InputStreams.asString(RedisInstance.class.getResourceAsStream("/redis/redisCA.crt"))
    );
  }

  private static String createTempDir() {
    return unchecked(() -> Files.createTempDirectory("kx-redis", asFileAttribute(fromString("rwxrwxrwx")))).toString();
  }

  private static void copyFromResourcesTo(String resourcePath, String tempDirectory) {
    unchecked(() -> {
      Path filePath = Path.of(tempDirectory + textAfter(resourcePath, "/redis"));
      Files.createFile(
        filePath,
        asFileAttribute(fromString("rwxrwxrwx"))
      );
      Files.write(
        filePath,
        InputStreams.asByteArray(requireNonNull(RedisInstance.class.getResourceAsStream(resourcePath)))
      );
      return null;
    });
  }

  static Map<String, String> getProperties() {
    return shared == null ? Map.of() : shared;
  }

  private static <T> T unchecked(Callable<T> callable) {
    try {
      return callable.call();
    } catch (RuntimeException e) {
      throw e;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String textAfter(String text, String front) {
    int beginIndex = text.indexOf(front) + front.length();
    int endIndex = text.length();
    return text.contains(front) ? text.substring(beginIndex, endIndex) : null;
  }

}
