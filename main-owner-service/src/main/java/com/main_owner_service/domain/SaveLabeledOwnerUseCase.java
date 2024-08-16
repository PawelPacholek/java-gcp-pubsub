package com.main_owner_service.domain;

import com.main_owner_service.domain.helpers.OwnerGateway;
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
