package com.main_owner_service.api;

import com.main_owner_service.api.models.UploadedOwner;
import com.main_owner_service.domain.UploadOwnerUseCase;
import com.main_owner_service.domain.models.InitialOwner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UploadOwnerController {

  @PostMapping(value = "/upload-owner", consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> uploadOwner(@RequestBody UploadedOwner owner) {
    InitialOwner initialOwner = owner.toDomain();
    new UploadOwnerUseCase().uploadInitialOwner(initialOwner);
    return new ResponseEntity<>("", HttpStatus.OK);
  }
}
