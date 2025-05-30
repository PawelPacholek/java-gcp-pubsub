package com.e2e_tests.main_owner_service;

import com.pubsub_emulator.PubSubEmulator;
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
      .withEnv("PUBSUB_PROJECT_ID", emulatorProperties.projectId())
      .withEnv("PUBSUB_EMULATOR_HOST", emulatorProperties.emulatorEndpoint().replace("localhost", "172.17.0.1"))
      .withEnv("GOOGLE_CLOUD_PROJECT", emulatorProperties.projectId())
      .withEnv("GOOGLE_APPLICATION_CREDENTIALS", credentialsPath)
      .withCopyFileToContainer(credentials, credentialsPath)
      .withCopyFileToContainer(entrypoint, "/app/entrypoint.sh")
      .withExposedPorts(8080, 5005)
      .withCreateContainerCmdModifier(cmd -> cmd.withName("MainOwnerService_container"));
    container.start();
    return container;
  }

  private static JibContainerBuilder buildJibContainer(JibContainerBuilder builder) {
    return builder.setEntrypoint("/app/entrypoint.sh");
  }

}
