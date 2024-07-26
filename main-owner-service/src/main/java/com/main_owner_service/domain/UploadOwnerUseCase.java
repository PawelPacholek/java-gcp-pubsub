package com.main_owner_service.domain;

import com.main_owner_service.domain.helpers.PubsubSender;
import com.main_owner_service.domain.models.InitialOwner;

public class UploadOwnerUseCase {

    public void uploadInitialOwner(InitialOwner initialOwner) {
        PubsubSender pubsubSender = null;
        pubsubSender.send(initialOwner);
    }

}
