package com.label_owner_service.persistence;

import com.label_owner_service.domain.helpers.LabeledOwnerSender;
import com.label_owner_service.domain.models.LabeledOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.label_owner_service.persistence.DataClassSerialization.serialize;

@Component
public class PubsubLabeledOwnerSender implements LabeledOwnerSender {

    private final MyGate gateway;

    @Autowired
    public PubsubLabeledOwnerSender(MyGate gateway) {
        this.gateway = gateway;
    }

    @Override
    public void send(LabeledOwner labeledOwner) {
        gateway.sendMessage(serialize(labeledOwner));
    }

}
