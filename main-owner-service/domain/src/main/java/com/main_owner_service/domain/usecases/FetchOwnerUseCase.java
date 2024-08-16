package com.main_owner_service.domain.usecases;

import com.main_owner_service.domain.helpers.OwnerGateway;
import com.main_owner_service.domain.models.LabeledOwner;

public class FetchOwnerUseCase {

    private final OwnerGateway ownerGateway;

    public FetchOwnerUseCase(OwnerGateway ownerGateway) {
        this.ownerGateway = ownerGateway;
    }

    public LabeledOwner fetchLabeledOwner(Long id) {
        return ownerGateway.fetch(id);
    }

}
