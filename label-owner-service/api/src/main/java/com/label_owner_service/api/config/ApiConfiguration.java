package com.label_owner_service.api.config;

import com.label_owner_service.domain.helpers.LabeledOwnerSender;
import com.label_owner_service.domain.usecases.LabelOwnerUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfiguration {

  @Bean
  public LabelOwnerUseCase labelOwnerUseCase(LabeledOwnerSender labeledOwnerSender) {
    return new LabelOwnerUseCase(labeledOwnerSender);
  }

}
