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

package com.main_owner_service.api.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestPersistenceConfiguration.class)
public class UploadInitialOwnerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void happyPath() throws Exception {
        String mockBody = """
                    {"id":7,"name":"name1","address":"address2","phone":"phone3","email":"email4"}""";
        mockMvc.perform(uploadInitialOwner().contentType(MediaType.APPLICATION_JSON).content(mockBody))
                .andExpect(status().isOk());
    }

    private static MockHttpServletRequestBuilder uploadInitialOwner() {
        return post("/upload-initial-owner");
    }

}
