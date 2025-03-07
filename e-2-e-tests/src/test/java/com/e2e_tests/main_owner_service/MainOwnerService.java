package com.e2e_tests.main_owner_service;

import com.e2e_tests.pubsub_emulator.PubSubEmulator;
import com.google.cloud.tools.jib.api.JibContainerBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.jib.JibImage;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Path;

public class MainOwnerService {

    public static GenericContainer<?> startContainer(PubSubEmulator.EmulatorProperties emulatorProperties) {
        String imageName = "gcr.io/local-axle-425708-t0/main-owner-service";

        JibImage mainOwnerServiceImage =
                new JibImage(imageName, MainOwnerService::buildJibContainer);

        MountableFile entrypoint =
                MountableFile.forHostPath(Path.of("src/test/java/com/e2e_tests/main_owner_service/entrypoint.sh"));

        MountableFile credentials =
                MountableFile.forHostPath(Path.of("src/test/java/com/e2e_tests/main_owner_service/gcloud_mock_credentials.json"));
        String credentialsPath = "/app/gcloud_mock_credentials.json";

        GenericContainer<?> container = new GenericContainer<>(mainOwnerServiceImage)
                .withEnv("GOOGLE_CLOUD_PROJECT", emulatorProperties.projectId())
                .withEnv("PUBSUB_PROJECT_ID", emulatorProperties.projectId())
                .withEnv("PUBSUB_EMULATOR_HOST", emulatorProperties.emulatorEndpoint())
                .withEnv("GOOGLE_APPLICATION_CREDENTIALS", credentialsPath)
                .withCopyFileToContainer(entrypoint, "/app/entrypoint.sh")
                .withCopyFileToContainer(credentials, credentialsPath)
                .withExposedPorts(8080)
                ;
        container.start();
        return container;
    }

    private static JibContainerBuilder buildJibContainer(JibContainerBuilder builder) {
        return builder.setEntrypoint("/app/entrypoint.sh")
                //.addEnvironmentVariable()
                //.setProgramArguments()
              //  .setExposedPorts(Port.tcp(8080))
                ;
    }

}
