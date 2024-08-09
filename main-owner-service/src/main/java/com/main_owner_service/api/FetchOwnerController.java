package com.main_owner_service.api;

import com.main_owner_service.domain.FetchOwnerUseCase;
import com.main_owner_service.domain.models.LabeledOwner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FetchOwnerController {

  private final FetchOwnerUseCase fetchOwnerUseCase;

  FetchOwnerController(FetchOwnerUseCase fetchOwnerUseCase) {
    this.fetchOwnerUseCase = fetchOwnerUseCase;
  }

  @GetMapping(value = "/fetch-owner")
  public ResponseEntity<String> fetchOwner(@RequestBody Long ownerId) {
    LabeledOwner labeledOwner = fetchOwnerUseCase.fetchLabeledOwner(ownerId);
    return new ResponseEntity<>(labeledOwner.toString(), HttpStatus.OK);
  }
}
