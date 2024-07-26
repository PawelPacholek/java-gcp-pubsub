package com.main_owner_service.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.main_owner_service.domain.models.InitialOwner;

public record UploadedOwner(Long id, String name, String address, String phone, String email) {

    @JsonCreator
    public UploadedOwner {
    }

    public InitialOwner toDomain() {
        return new InitialOwner(id, name, address, phone, email);
    }
}
