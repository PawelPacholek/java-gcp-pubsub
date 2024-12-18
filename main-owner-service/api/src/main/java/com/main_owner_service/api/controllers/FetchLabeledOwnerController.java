package com.main_owner_service.api.controllers;

import com.main_owner_service.domain.usecases.FetchLabeledOwnerUseCase;
import com.main_owner_service.domain.models.LabeledOwner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FetchLabeledOwnerController {

  private final FetchLabeledOwnerUseCase fetchOwnerUseCase;

  FetchLabeledOwnerController(FetchLabeledOwnerUseCase fetchOwnerUseCase) {
    this.fetchOwnerUseCase = fetchOwnerUseCase;
  }

  @GetMapping(value = "/fetch-labeled-owner/{id}")
  public ResponseEntity<String> fetchOwner(@PathVariable("id") Long ownerId) {
    LabeledOwner labeledOwner = fetchOwnerUseCase.fetchLabeledOwner(ownerId);
    return new ResponseEntity<>(labeledOwner.toString(), HttpStatus.OK);
  }
}
