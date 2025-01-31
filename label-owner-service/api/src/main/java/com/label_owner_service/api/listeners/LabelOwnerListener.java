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

package com.label_owner_service.api.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.label_owner_service.api.models.PubsubBody;
import com.label_owner_service.domain.models.InitialOwner;
import com.label_owner_service.domain.usecases.LabelOwnerUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@Component
public class LabelOwnerListener {

    private final LabelOwnerUseCase labelOwnerUseCase;

    public LabelOwnerListener(LabelOwnerUseCase labelOwnerUseCase) {
        this.labelOwnerUseCase = labelOwnerUseCase;
    }

    @ServiceActivator(inputChannel = "initialOwnerChannel")
    public void messageReceiver(
            InitialOwner initialOwner,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message
    ) {
        labelOwnerUseCase.labelAndSendOwner(initialOwner);
        message.ack();
    }



  /*
    @PostMapping(value = "/label-owner")
    public ResponseEntity<String> saveLabeledOwner(@RequestBody PubsubBody body) throws JsonProcessingException {
        PubsubBody.Message message = body.getMessage();
        if (message == null) {
            String msg = "Bad Request: invalid Pub/Sub message format";
            System.out.println(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        String data = message.getData();
        if (data == null) {
            String msg = "Bad Request: no data found";
            System.out.println(msg);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        String target = new String(Base64.getDecoder().decode(data));
        InitialOwner labeledOwner = new ObjectMapper().readValue(target, InitialOwner.class);
        labelOwnerUseCase.labelAndSendOwner(labeledOwner);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @ExceptionHandler({IllegalArgumentException.class, JsonProcessingException.class})
    public ResponseEntity<String> handleException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
*/
}
