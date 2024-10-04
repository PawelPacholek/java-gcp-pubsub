package com.e2e_tests;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = E2ETestConfiguration.class)
public class E2ETests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void simleE2Etest() throws Exception {
        String mockBody = """
                    {"id":7,"name":"name1","address":"address2","phone":"phone3","email":"email4"}""";
        mockMvc.perform(uploadInitialOwner().contentType(MediaType.APPLICATION_JSON).content(mockBody))
                .andExpect(status().isOk());
    }

    private static MockHttpServletRequestBuilder uploadInitialOwner() {
        return post("/upload-initial-owner");
    }

}
