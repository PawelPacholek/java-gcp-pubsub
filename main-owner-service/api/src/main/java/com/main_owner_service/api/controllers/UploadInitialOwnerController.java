package com.main_owner_service.api.controllers;

import com.main_owner_service.api.models.UploadedOwner;
import com.main_owner_service.domain.usecases.UploadInitialOwnerUseCase;
import com.main_owner_service.domain.models.InitialOwner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UploadInitialOwnerController {

    private static final Logger logger = LoggerFactory.getLogger("event logger");

    private final UploadInitialOwnerUseCase uploadOwnerUseCase;

    public UploadInitialOwnerController(UploadInitialOwnerUseCase uploadOwnerUseCase) {
        this.uploadOwnerUseCase = uploadOwnerUseCase;
    }

    @PostMapping(value = "/upload-initial-owner", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadOwner(@RequestBody UploadedOwner owner) {
        InitialOwner initialOwner = owner.toDomain();
        uploadOwnerUseCase.uploadInitialOwner(initialOwner);
        logger.debug("---- owner upload completed ----");
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
