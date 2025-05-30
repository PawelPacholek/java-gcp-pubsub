package com.main_owner_service.run.test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import com.redis_instance.RedisInstance;

public class RedisPropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    TestPropertyValues.of(RedisInstance.getProperties()).applyTo(applicationContext.getEnvironment());
  }

}
