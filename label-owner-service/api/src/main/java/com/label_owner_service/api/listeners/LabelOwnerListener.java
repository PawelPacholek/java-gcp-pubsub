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

import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.label_owner_service.api.helpers.DataClassSerialization;
import com.label_owner_service.domain.models.InitialOwner;
import com.label_owner_service.domain.usecases.LabelOwnerUseCase;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Repository;

@Repository
public class LabelOwnerListener {

    private final LabelOwnerUseCase labelOwnerUseCase;

    public LabelOwnerListener(LabelOwnerUseCase labelOwnerUseCase) {
        this.labelOwnerUseCase = labelOwnerUseCase;
    }

    @ServiceActivator(inputChannel = "initialOwnerChannel")
    public void messageReceiver(
      String rawInitialOwnerMessage,
            //InitialOwner initialOwner,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message
    ) {
        InitialOwner initialOwner = parseOwner(rawInitialOwnerMessage);
        labelOwnerUseCase.labelAndSendOwner(initialOwner);
        message.ack();
    }

    private InitialOwner parseOwner(String rawInitialOwnerMessage) {
      String initialOwnerJson = rawInitialOwnerMessage
        .replaceAll("^.*payload=\\{", "{")
        .replaceAll("}, headers=.*$", "}");
      return DataClassSerialization.deserialize(initialOwnerJson, InitialOwner.class);
    }

}
