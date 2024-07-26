package com.main_owner_service.domain;

import com.main_owner_service.domain.helpers.OwnerGateway;
import com.main_owner_service.domain.models.LabeledOwner;

public class SaveLabeledOwnerUseCase {

    public void saveLabeledOwner(LabeledOwner labeledOwner) {
        OwnerGateway ownerGateway = null;
        ownerGateway.save(labeledOwner);
    }

}
