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

package com.main_owner_service.api.listeners;

import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.main_owner_service.api.helpers.DataClassSerialization;
import com.main_owner_service.domain.models.LabeledOwner;
import com.main_owner_service.domain.usecases.SaveLabeledOwnerUseCase;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class SaveLabeledOwnerListener {

    private final SaveLabeledOwnerUseCase saveLabeledOwnerUseCase;

    public SaveLabeledOwnerListener(SaveLabeledOwnerUseCase saveLabeledOwnerUseCase) {
        this.saveLabeledOwnerUseCase = saveLabeledOwnerUseCase;
    }

    @ServiceActivator(inputChannel = "labeledOwnerChannel")
    public void messageReceiver(
      String rawLabeledOwnerMessage,
            //LabeledOwner labeledOwner,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message
    ) {
        LabeledOwner labeledOwner = parseOwner(rawLabeledOwnerMessage);
        saveLabeledOwnerUseCase.saveLabeledOwner(labeledOwner);
        message.ack();
    }

    private LabeledOwner parseOwner(String rawLabeledOwnerMessage) {
        String initialOwnerJson = rawLabeledOwnerMessage
          .replaceAll("^.*payload=\\{", "{")
          .replaceAll("}, headers=.*$", "}");
        return DataClassSerialization.deserialize(initialOwnerJson, LabeledOwner.class);
    }

/*
    @PostMapping(value = "/save-labeled-owner")
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
        LabeledOwner labeledOwner = new ObjectMapper().readValue(target, LabeledOwner.class);
        saveLabeledOwnerUseCase.saveLabeledOwner(labeledOwner);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @ExceptionHandler({IllegalArgumentException.class, JsonProcessingException.class})
    public ResponseEntity<String> handleException(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
*/
}
