package com.main_owner_service.api.test;

import com.main_owner_service.domain.helpers.InitialOwnerSender;
import com.main_owner_service.domain.helpers.OwnerGateway;
import com.main_owner_service.domain.models.LabeledOwner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestPersistenceConfiguration {

    @Bean
    @Primary
    public OwnerGateway testOwnerGateway() {
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
    @Primary
    public InitialOwnerSender testInitialOwnerSender() {
        return initialOwner -> {
        };
    }
}
