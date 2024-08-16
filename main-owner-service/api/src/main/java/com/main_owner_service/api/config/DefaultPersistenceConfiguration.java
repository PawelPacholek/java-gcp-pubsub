package com.main_owner_service.api.config;

import com.main_owner_service.domain.helpers.InitialOwnerSender;
import com.main_owner_service.domain.helpers.OwnerGateway;
import com.main_owner_service.domain.models.LabeledOwner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultPersistenceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OwnerGateway defaultOwnerGateway() {
        return new OwnerGateway() {
            @Override
            public void save(LabeledOwner labeledOwner) {

            }

            @Override
            public LabeledOwner fetch(Long id) {
                return null;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public InitialOwnerSender defaultInitialOwnerSender() {
        return initialOwner -> {
        };
    }
}
