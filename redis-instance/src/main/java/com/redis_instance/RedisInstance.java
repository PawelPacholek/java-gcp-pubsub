package com.redis_instance;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;

import java.util.Map;

public class RedisInstance {

  public static class Shared implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
      start();
    }

  }

  private static Map<String, String> shared;

  private synchronized static void start() {
    if (isNotRunning())
      shared = instantiateRedis();
  }

  private static boolean isNotRunning() {
    return shared == null;
  }

  @SuppressWarnings("resource")
  private static Map<String, String> instantiateRedis() {
    GenericContainer<?> redis = new GenericContainer<>("redis:8.0.2").withExposedPorts(6379);
    redis.start();
    return Map.of(
      "labeled.owner.host", redis.getHost(),
      "labeled.owner.port", String.valueOf(redis.getMappedPort(6379))
    );
  }

  public static Map<String, String> getProperties() {
    return shared == null ? Map.of() : shared;
  }

}
