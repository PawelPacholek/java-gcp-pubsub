package com.main_owner_service.api.controllers;

import com.main_owner_service.api.helpers.DataClassSerialization;
import com.main_owner_service.domain.usecases.FetchLabeledOwnerUseCase;
import com.main_owner_service.domain.models.LabeledOwner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
public class FetchLabeledOwnerController {

  private final FetchLabeledOwnerUseCase fetchOwnerUseCase;

  FetchLabeledOwnerController(FetchLabeledOwnerUseCase fetchOwnerUseCase) {
    this.fetchOwnerUseCase = fetchOwnerUseCase;
  }

  @GetMapping(value = "/fetch-labeled-owner/{id}")
  public ResponseEntity<String> fetchOwner(@PathVariable("id") Long ownerId) {
    LabeledOwner labeledOwner = fetchOwnerUseCase.fetchLabeledOwner(ownerId);
    if (labeledOwner == null)
      throw new IllegalArgumentException("LabeledOwner not found");
    return new ResponseEntity<>(DataClassSerialization.serialize(labeledOwner), HttpStatus.OK);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  static ResponseEntity<String> handleMalformedBody(IllegalArgumentException exception) {
    return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

}
