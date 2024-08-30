package com.label_owner_service.api.test;

import com.label_owner_service.domain.helpers.LabeledOwnerSender;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestPersistenceConfiguration {

    @Bean
    public LabeledOwnerSender testLabeledOwnerSender() {
        return System.out::println;
    }
}
