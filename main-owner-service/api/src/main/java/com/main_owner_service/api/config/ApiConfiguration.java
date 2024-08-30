package com.main_owner_service.api.config;

import com.main_owner_service.domain.usecases.FetchLabeledOwnerUseCase;
import com.main_owner_service.domain.usecases.SaveLabeledOwnerUseCase;
import com.main_owner_service.domain.usecases.UploadInitialOwnerUseCase;
import com.main_owner_service.domain.helpers.InitialOwnerSender;
import com.main_owner_service.domain.helpers.OwnerGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfiguration {

  @Bean
  public FetchLabeledOwnerUseCase fetchOwnerUseCase(OwnerGateway ownerGateway) {
    return new FetchLabeledOwnerUseCase(ownerGateway);
  }

  @Bean
  public SaveLabeledOwnerUseCase saveLabeledOwnerUseCase(OwnerGateway ownerGateway) {
    return new SaveLabeledOwnerUseCase(ownerGateway);
  }

  @Bean
  public UploadInitialOwnerUseCase uploadOwnerUseCase(InitialOwnerSender initialOwnerSender) {
    return new UploadInitialOwnerUseCase(initialOwnerSender);
  }

}
