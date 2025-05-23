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

import com.pubsub_emulator.PubSubEmulator;
import com.pubsub_emulator.PubSubEmulatorInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = PubSubEmulatorInitializer.class, classes = TestRunConfiguration.class)
public class UploadInitialOwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void preparePubsubEmulator() {
        PubSubEmulator.createTopicAndSubscription(
          "initialOwner",
          "label-owner-service-to-initialOwner-subscription"
        );
    }

    @AfterEach
    public void cleanPubsubEmulator() {
        PubSubEmulator.deleteTopicAndSubscription(
          "initialOwner",
          "label-owner-service-to-initialOwner-subscription"
        );
        PubSubEmulator.deleteTopicAndSubscription(
          "labeledOwner",
          "main-owner-service-to-labeledOwner-subscription"
        );
    }

    @Test
    public void happyPath() throws Exception {
        String mockBody = """
                    {"id":7,"name":"name1","address":"address2","phone":"phone3","email":"email4"}""";
        mockMvc.perform(uploadInitialOwner().contentType(MediaType.APPLICATION_JSON).content(mockBody))
                .andExpect(status().isOk());
        List<String> messages = PubSubEmulator.fetchRawMessages("label-owner-service-to-initialOwner-subscription");
        assertThat(messages).hasSize(1);
    }

    private static MockHttpServletRequestBuilder uploadInitialOwner() {
        return post("/upload-initial-owner");
    }

}
