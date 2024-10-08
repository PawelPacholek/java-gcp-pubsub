package com.main_owner_service.domain.usecases;

import com.main_owner_service.domain.ports.OwnerGateway;
import com.main_owner_service.domain.models.LabeledOwner;

public class SaveLabeledOwnerUseCase {

    private final OwnerGateway ownerGateway;

    public SaveLabeledOwnerUseCase(OwnerGateway ownerGateway) {
        this.ownerGateway = ownerGateway;
    }

    public void saveLabeledOwner(LabeledOwner labeledOwner) {
        ownerGateway.save(labeledOwner);
    }

}
