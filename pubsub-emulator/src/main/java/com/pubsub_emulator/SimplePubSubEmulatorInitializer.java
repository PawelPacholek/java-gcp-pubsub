package com.pubsub_emulator;

import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class SimplePubSubEmulatorInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    PubSubEmulator.startIfNeeded(
      () -> applicationContext.getBean(PubSubAdmin.class),
      () -> applicationContext.getBean(PubSubTemplate.class)
    );
  }

}
