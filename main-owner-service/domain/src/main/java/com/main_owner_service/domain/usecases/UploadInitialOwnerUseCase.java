package com.main_owner_service.domain.usecases;

import com.main_owner_service.domain.helpers.InitialOwnerSender;
import com.main_owner_service.domain.models.InitialOwner;

public class UploadInitialOwnerUseCase {

    private final InitialOwnerSender initialOwnerSender;

    public UploadInitialOwnerUseCase(InitialOwnerSender initialOwnerSender) {
        this.initialOwnerSender = initialOwnerSender;
    }

    public void uploadInitialOwner(InitialOwner initialOwner) {
        initialOwnerSender.send(initialOwner);
    }

}
