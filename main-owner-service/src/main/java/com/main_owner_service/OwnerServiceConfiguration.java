package com.main_owner_service;

import com.main_owner_service.domain.FetchOwnerUseCase;
import com.main_owner_service.persistence.OwnerGatewayImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OwnerServiceConfiguration {

  @Bean
  public FetchOwnerUseCase fetchOwnerUseCase() {
    return new FetchOwnerUseCase(new OwnerGatewayImp());
  }

}
