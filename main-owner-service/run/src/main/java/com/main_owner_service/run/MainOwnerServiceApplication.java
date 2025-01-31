package com.main_owner_service.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;

@IntegrationComponentScan("com.main_owner_service")
@SpringBootApplication(scanBasePackages = "com.main_owner_service")
public class MainOwnerServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(MainOwnerServiceApplication.class, args);
  }

  @Configuration
  @EnableIntegration
  public static class Config {

  }

}
