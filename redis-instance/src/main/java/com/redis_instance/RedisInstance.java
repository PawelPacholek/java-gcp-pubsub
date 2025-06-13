package com.redis_instance;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;

public class RedisInstance {

  public static class Shared implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) {
      start();
    }

  }

  private static RedisProperties shared;

  private synchronized static void start() {
    if (isNotRunning())
      shared = instantiateRedis();
  }

  private static boolean isNotRunning() {
    return shared == null;
  }

  @SuppressWarnings("resource")
  private static RedisProperties instantiateRedis() {
    GenericContainer<?> redis = new GenericContainer<>("redis:8.0.2").withExposedPorts(6379);
    redis.start();
    return new RedisProperties(redis.getHost(), redis.getMappedPort(6379));
  }

  public static RedisProperties getProperties() {
    return shared == null ? null : shared;
  }

  public record RedisProperties(String host, int port) {}

}
