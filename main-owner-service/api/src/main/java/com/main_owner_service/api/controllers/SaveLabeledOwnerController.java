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

package com.main_owner_service.api.controllers;

import java.util.Base64;

import com.main_owner_service.api.models.PubsubBody;
import com.main_owner_service.domain.usecases.SaveLabeledOwnerUseCase;
import com.main_owner_service.domain.models.LabeledOwner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SaveLabeledOwnerController {

    private final SaveLabeledOwnerUseCase saveLabeledOwnerUseCase;

    public SaveLabeledOwnerController(SaveLabeledOwnerUseCase saveLabeledOwnerUseCase) {
        this.saveLabeledOwnerUseCase = saveLabeledOwnerUseCase;
    }

    @PostMapping(value = "/save-labeled-owner")
    public ResponseEntity<String> saveLabeledOwner(@RequestBody PubsubBody body) {
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
        LabeledOwner labeledOwner = new LabeledOwner(null, target, null, null, null, null);
        saveLabeledOwnerUseCase.saveLabeledOwner(labeledOwner);

        System.out.println(msg);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
