package com.main_owner_service.persistence;

import com.main_owner_service.domain.models.InitialOwner;
import com.main_owner_service.domain.ports.InitialOwnerSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.main_owner_service.persistence.DataClassSerialization.serialize;

@Component
public class PubsubInitialOwnerSender implements InitialOwnerSender {

    private final MyGate gateway;

    @Autowired
    public PubsubInitialOwnerSender(MyGate gateway) {
        this.gateway = gateway;
    }

    @Override
    public void send(InitialOwner initialOwner) {
        gateway.sendMessage(serialize(initialOwner));
    }

}
