package com.main_owner_service.run.test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import com.redis_instance.RedisInstance;

import java.util.Map;

public class RedisPropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    RedisInstance.RedisProperties redisProperties = RedisInstance.getProperties();
    var properties = Map.of(
      "labeled.owner.host", redisProperties.host(),
      "labeled.owner.port", String.valueOf(redisProperties.port())
    );
    TestPropertyValues.of(properties).applyTo(applicationContext.getEnvironment());
  }

}
