package com.label_owner_service.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

  @Bean
  public PubsubLabeledOwnerSender pubsubLabeledOwnerSender() {
    return new PubsubLabeledOwnerSender();
  }

}
