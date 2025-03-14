package com.label_owner_service.persistence;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import com.label_owner_service.domain.helpers.LabeledOwnerSender;
import com.label_owner_service.domain.models.LabeledOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.label_owner_service.persistence.DataClassSerialization.serialize;

@Component
public class PubsubLabeledOwnerSender implements LabeledOwnerSender {

    private static final String PROJECT_ID = "local-axle-425708-t0";
    private static final String TOPIC_ID = "initialOwner";

    private final MyGate gateway;

    @Autowired
    public PubsubLabeledOwnerSender(MyGate gateway) {
        this.gateway = gateway;
    }

    @Override
    public void send(LabeledOwner labeledOwner) {
        //   Message message = new Message() {
        //   }
        //  initialOwnerChannel.send(message);l

        gateway.sendMessage(serialize(labeledOwner));


/*
    @Override
    public void send(LabeledOwner labeledOwner) {
        TopicName topicName = TopicName.of(PROJECT_ID, TOPIC_ID);
        Publisher publisher = null;

        try {
            // Create a publisher instance with default settings bound to the topic
            publisher = Publisher.newBuilder(topicName).build();

            String message = labeledOwner.toString();

            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            // Once published, returns a server-assigned message id (unique within the topic)
            ApiFuture<String> future = publisher.publish(pubsubMessage);

            // Add an asynchronous callback to handle success / failure
            ApiFutures.addCallback(
                    future,
                    new ApiFutureCallback<>() {

                        @Override
                        public void onFailure(Throwable throwable) {
                            if (throwable instanceof ApiException apiException) {
                                // details on the API exception
                                System.out.println(apiException.getStatusCode().getCode());
                                System.out.println(apiException.isRetryable());
                            }
                            System.out.println("Error publishing message : " + message);
                        }

                        @Override
                        public void onSuccess(String messageId) {
                            // Once published, returns server-assigned message ids (unique within the topic)
                            System.out.println("Published message ID: " + messageId);
                        }
                    },
                    MoreExecutors.directExecutor());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (publisher != null) {
                // When finished with the publisher, shutdown to free up resources.
                publisher.shutdown();
                try {
                    publisher.awaitTermination(1, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }*/
    }
}
