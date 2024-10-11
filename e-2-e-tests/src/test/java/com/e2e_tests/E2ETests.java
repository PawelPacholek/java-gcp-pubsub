package com.e2e_tests;

import com.e2e_tests.pubsub_emulator.PubSubEmulator;
import com.e2e_tests.pubsub_emulator.PubSubEmulatorInitializer;
import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = E2ETests.App.class)
@ContextConfiguration(/*classes = E2ETestConfiguration.class,*/initializers = PubSubEmulatorInitializer.class)
class E2ETests {

    @SpringBootApplication
    public static class App {

        public static void main(String... args) {
            SpringApplication.run(App.class, args);
        }

   //     @Bean
 //       PubSubTopicClient pubSubClient(PubSubTemplate template) {
   //        return new PubSubTopicClient(template, "instance", "topic");
   //     }

    }

//    @Autowired
 //   PubSubTopicClient pubSubTopicClient;
    @Autowired
    PubSubAdmin admin;
    @Autowired
    PubSubTemplate template;


    //  @Autowired
    //  private MockMvc mockMvc;
 /*   @Before
    public void preparePubsubEmulator() {
        PubSubEmulator.createTopicAndSubscription(
                "labeledOwner",
                "main-owner-service-to-labeledOwner-subscription"
        );
        PubSubEmulator.createTopicAndSubscription(
                "initialOwner",
                "label-owner-service-to-initialOwner-subscription"
        );
    }*/

    @Test
    void simleE2Etest() throws Exception {
        PubSubEmulator.createTopicAndSubscription(
                "labeledOwner",
                "main-owner-service-to-labeledOwner-subscription"
        );
        PubSubEmulator.createTopicAndSubscription(
                "initialOwner",
                "label-owner-service-to-initialOwner-subscription"
        );


        String mockBody = """
                {"id":7,"name":"name1","address":"address2","phone":"phone3","email":"email4"}""";
        //    mockMvc.perform(uploadInitialOwner().contentType(MediaType.APPLICATION_JSON).content(mockBody))
        //           .andExpect(status().isOk());
    }

    private static MockHttpServletRequestBuilder uploadInitialOwner() {
        return post("/upload-initial-owner");
    }

}
