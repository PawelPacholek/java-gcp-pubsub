/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.label_owner_service.api;

import com.label_owner_service.domain.InitialOwner;
import models.PubsubBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
public class LabelOwnerController {

  @PostMapping(value = "/label-owner")
  public ResponseEntity<String> LabelOwner(@RequestBody PubsubBody body) {
    PubsubBody.Message message = body.getMessage();
    if (message == null) {
      String msg = "Bad Request: invalid Pub/Sub message format";
      System.out.println(msg);
      return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    String data = message.getData();
    String target =
        !StringUtils.isEmpty(data) ? new String(Base64.getDecoder().decode(data)) : "World";
    String msg = "Hello " + target + "!";

    //TODO Deserialize target
    InitialOwner labeledOwner = new InitialOwner(null, target, null, null, null);
    //new SaveLabeledOwnerUseCase().saveLabeledOwner(labeledOwner);

    System.out.println(msg);
    return new ResponseEntity<>(msg, HttpStatus.OK);
  }
}
