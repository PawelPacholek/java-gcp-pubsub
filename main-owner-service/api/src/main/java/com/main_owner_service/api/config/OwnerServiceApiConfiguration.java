package com.main_owner_service.api.config;

import com.main_owner_service.domain.FetchOwnerUseCase;
import com.main_owner_service.domain.SaveLabeledOwnerUseCase;
import com.main_owner_service.domain.UploadOwnerUseCase;
import com.main_owner_service.domain.helpers.InitialOwnerSender;
import com.main_owner_service.domain.helpers.OwnerGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OwnerServiceApiConfiguration {

  @Bean
  public FetchOwnerUseCase fetchOwnerUseCase(OwnerGateway ownerGateway) {
    return new FetchOwnerUseCase(ownerGateway);
  }

  @Bean
  public SaveLabeledOwnerUseCase saveLabeledOwnerUseCase(OwnerGateway ownerGateway) {
    return new SaveLabeledOwnerUseCase(ownerGateway);
  }

  @Bean
  public UploadOwnerUseCase uploadOwnerUseCase(InitialOwnerSender initialOwnerSender) {
    return new UploadOwnerUseCase(initialOwnerSender);
  }

}
