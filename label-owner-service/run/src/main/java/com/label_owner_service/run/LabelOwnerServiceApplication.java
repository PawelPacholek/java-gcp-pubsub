package com.label_owner_service.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;

@IntegrationComponentScan("com.label_owner_service")
@SpringBootApplication(scanBasePackages = "com.label_owner_service")
public class LabelOwnerServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(LabelOwnerServiceApplication.class, args);
  }
}
