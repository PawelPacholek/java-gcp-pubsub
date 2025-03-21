package com.pubsub_emulator;

import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

public class PubSubEmulatorInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    PubSubEmulator.startIfNeeded(
      () -> applicationContext.getBean(PubSubAdmin.class),
      () -> applicationContext.getBean(PubSubTemplate.class)
    );
    var props = PubSubEmulator.propertiesMap();
    applicationContext.getEnvironment()
      .getPropertySources()
      .addFirst(new MapPropertySource("pubsub-emulator", props));
  }

}
