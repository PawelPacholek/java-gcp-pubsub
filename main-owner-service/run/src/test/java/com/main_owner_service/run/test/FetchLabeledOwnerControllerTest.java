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

package com.main_owner_service.run.test;

import com.main_owner_service.api.helpers.DataClassSerialization;
import com.main_owner_service.domain.models.LabeledOwner;
import com.pubsub_emulator.PubSubEmulator;
import com.pubsub_emulator.PubSubEmulatorInitializer;
import com.redis_instance.RedisInstance;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.Duration;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RedisInstance.Shared.class)
@ContextConfiguration(initializers = PubSubEmulatorInitializer.class, classes = TestRunConfiguration.class)
public class FetchLabeledOwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void cleanPubsubEmulator() {
        PubSubEmulator.deleteTopicAndSubscription(
          "labeledOwner",
          "main-owner-service-to-labeledOwner-subscription"
        );
    }

    @Test
    public void happyPath() throws Exception {
        Long ownerId = 4L;
        LabeledOwner labeledOwner =
          new LabeledOwner(ownerId, "name", "address", "phone", "email", Set.of());
        String serialized = DataClassSerialization.serialize(labeledOwner);
        PubSubEmulator.publish("labeledOwner", serialized);

        sleep(5);

        mockMvc.perform(fetchLabeledOwner(ownerId))
                .andExpect(status().isOk());
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(Duration.ofSeconds(seconds));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static MockHttpServletRequestBuilder fetchLabeledOwner(Long id) {
        return get("/fetch-labeled-owner/" + id);
    }

}
