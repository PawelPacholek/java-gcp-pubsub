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

package com.label_owner_service.api.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestPersistenceConfiguration.class)
public class LabelOwnerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addEmptyBody() throws Exception {
        mockMvc.perform(labelOwner()).andExpect(status().isBadRequest());
    }

    @Test
    public void addNoMessage() throws Exception {
        String mockBody = "{}";
        mockMvc
                .perform(labelOwner().contentType(MediaType.APPLICATION_JSON).content(mockBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addInvalidMimetype() throws Exception {
        String mockBody = "{\"message\":{\"data\":\"dGVzdA==\","
                + "\"attributes\":{},\"messageId\":\"91010751788941\""
                + ",\"publishTime\":\"2017-09-25T23:16:42.302Z\"}}";
        mockMvc
                .perform(labelOwner().contentType(MediaType.TEXT_HTML).content(mockBody))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void addMinimalBody() throws Exception {
        String mockBody = "{\"message\":{}}";
        mockMvc
                .perform(labelOwner().contentType(MediaType.APPLICATION_JSON).content(mockBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addInvalidData() throws Exception {
        String mockBody = "{\"message\":{\"data\":\"eyJpZCI6NywibmFtZSI6Im5hb\"}}";
        mockMvc
                .perform(labelOwner().contentType(MediaType.APPLICATION_JSON).content(mockBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addInvalidOwner() throws Exception {
        String mockBody = "{\"message\":{\"data\":\"eyJpZCI9NywibmFtZSI6Im5hbWUxIiwiYWRkcmVzcyI6ImFkZHJlc3MyIiwicGhvbmUiOiJwaG9uZTMiLCJlbWFpbCI6ImVtYWlsNCIsImxhYmVscyI6W119\"}}";
        mockMvc
                .perform(labelOwner().contentType(MediaType.APPLICATION_JSON).content(mockBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unexpected character ('=' (code 61)): was expecting a colon to separate field name and value\n" +
                        " at [Source: (String)\"{\"id\"=7,\"name\":\"name1\",\"address\":\"address2\",\"phone\":\"phone3\",\"email\":\"email4\",\"labels\":[]}\"; line: 1, column: 7]"));
    }

    @Test
    public void addFullBody() throws Exception {
        String mockBody = "{\"message\":{\"data\":\"eyJpZCI6NywibmFtZSI6Im5hbWUxIiwiYWRkcmVzcyI6ImFkZHJlc3MyIiwicGhvbmUiOiJwaG9uZTMiLCJlbWFpbCI6ImVtYWlsNCJ9\","
                + "\"attributes\":{},\"messageId\":\"91010751788941\""
                + ",\"publishTime\":\"2017-09-25T23:16:42.302Z\"}}";
        mockMvc
                .perform(labelOwner().contentType(MediaType.APPLICATION_JSON).content(mockBody))
                .andExpect(status().isOk());
    }

    private static MockHttpServletRequestBuilder labelOwner() {
        return post("/label-owner");
    }

}
