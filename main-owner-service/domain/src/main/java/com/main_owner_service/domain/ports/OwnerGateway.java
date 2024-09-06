package com.main_owner_service.domain.ports;

import com.main_owner_service.domain.models.LabeledOwner;

public interface OwnerGateway {

    void save(LabeledOwner labeledOwner);

    LabeledOwner fetch(Long id);

}
