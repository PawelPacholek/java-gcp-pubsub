package com.main_owner_service.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.main_owner_service.api", "com.main_owner_service.persistence"})
public class MainOwnerServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(MainOwnerServiceApplication.class, args);
  }
}
