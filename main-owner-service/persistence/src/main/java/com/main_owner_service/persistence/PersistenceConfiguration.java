package com.main_owner_service.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfiguration {

  @Bean
  public OwnerGatewayImp ownerGatewayImp() {
    return new OwnerGatewayImp();
  }

  @Bean
  public PubsubInitialOwnerSender pubsubInitialOwnerSender() {
    return new PubsubInitialOwnerSender();
  }

}
