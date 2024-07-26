package com.main_owner_service.domain;

import com.main_owner_service.domain.helpers.OwnerGateway;
import com.main_owner_service.domain.models.LabeledOwner;

public class FetchOwnerUseCase {

    public LabeledOwner fetchLabeledOwner(Long id) {
        OwnerGateway ownerGateway = null;
        return ownerGateway.fetch(id);
    }

}
