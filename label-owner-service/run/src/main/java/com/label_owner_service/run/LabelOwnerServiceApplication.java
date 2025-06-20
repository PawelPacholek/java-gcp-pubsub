package com.label_owner_service.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.integration.annotation.IntegrationComponentScan;

import java.util.Map;

@IntegrationComponentScan("com.label_owner_service")
@SpringBootApplication(scanBasePackages = "com.label_owner_service")
public class LabelOwnerServiceApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(LabelOwnerServiceApplication.class);
    app.addInitializers(new MyContextInitializer());
    app.run(args);
  }

  static class MyContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      ConfigurableEnvironment env = applicationContext.getEnvironment();
      String envVar = System.getenv("PUBSUB_EMULATOR_HOST");
      if (envVar != null) {
        Map<String, Object> props = Map.of("spring.cloud.gcp.pubsub.emulator-host", envVar);
        env.getPropertySources().addFirst(new MapPropertySource("pubsub-emulator", props));
      }
    }

  }

}
