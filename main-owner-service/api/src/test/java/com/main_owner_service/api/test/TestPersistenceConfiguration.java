package com.main_owner_service.api.test;

import com.main_owner_service.domain.helpers.InitialOwnerSender;
import com.main_owner_service.domain.helpers.OwnerGateway;
import com.main_owner_service.domain.models.LabeledOwner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestPersistenceConfiguration {

    @Bean
    public OwnerGateway testOwnerGateway() {
        return new OwnerGateway() {
            @Override
            public void save(LabeledOwner labeledOwner) {
                System.out.println(labeledOwner);
            }

            @Override
            public LabeledOwner fetch(Long id) {
                return new LabeledOwner(id, null, null, null, null, null);
            }
        };
    }

    @Bean
    public InitialOwnerSender testInitialOwnerSender() {
        return initialOwner -> {
        };
    }
}
