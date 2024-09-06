package com.main_owner_service.domain.ports;

import com.main_owner_service.domain.models.InitialOwner;

public interface InitialOwnerSender {

    void send(InitialOwner initialOwner);

}
