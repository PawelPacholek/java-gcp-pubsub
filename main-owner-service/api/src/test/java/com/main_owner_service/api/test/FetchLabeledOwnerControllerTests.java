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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestPersistenceConfiguration.class)
public class FetchLabeledOwnerControllerTests {

  @Autowired private MockMvc mockMvc;

  @Test
  public void happyPath() throws Exception {
    mockMvc.perform(fetchLabeledOwner(4L))
            .andExpect(status().isOk());
            //.andExpect(content().string("LabeledOwner[id=4, name=null, address=null, phone=null, email=null, labels=null]"));
  }

  private static MockHttpServletRequestBuilder fetchLabeledOwner(Long id) {
    return get("/fetch-labeled-owner/" + id);
  }

}
