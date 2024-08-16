package com.main_owner_service.domain.usecases;

import com.main_owner_service.domain.helpers.InitialOwnerSender;
import com.main_owner_service.domain.models.InitialOwner;

public class UploadOwnerUseCase {

    private final InitialOwnerSender initialOwnerSender;

    public UploadOwnerUseCase(InitialOwnerSender initialOwnerSender) {
        this.initialOwnerSender = initialOwnerSender;
    }

    public void uploadInitialOwner(InitialOwner initialOwner) {
        initialOwnerSender.send(initialOwner);
    }

}
