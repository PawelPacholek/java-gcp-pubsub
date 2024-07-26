package com.main_owner_service.domain.helpers;

import com.main_owner_service.domain.models.InitialOwner;

public interface PubsubSender {

    void send(InitialOwner initialOwner);

}
