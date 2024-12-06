package com.e2e_tests.main_owner_service;

import com.google.cloud.tools.jib.api.JibContainerBuilder;
import com.google.cloud.tools.jib.api.buildplan.Port;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.jib.JibImage;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainOwnerService {

    public static GenericContainer<?> startContainer() {
        String imageName = "gcr.io/local-axle-425708-t0/main-owner-service";

        //ImageFromDockerfile mainOwnerServiceImage = new ImageFromDockerfile(imageName);
        JibImage mainOwnerServiceImage =
                new JibImage(imageName, MainOwnerService::buildJibContainer);


        GenericContainer<?> container = new GenericContainer<>(mainOwnerServiceImage)
                //.withCommand("sleep infinity")
                .withEnv("GOOGLE_CLOUD_PROJECT", "test-project-bla")
                //.withEnv("GOOGLE_APPLICATION_CREDENTIALS", "path_to_file")
               // .withCopyFileToContainer(MountableFile.forHostPath(Path.of("./entrypoint.sh")), "/app/entrypoint.sh")
            //    .withExposedPorts(8080)
                ;
        container.start();
        return container;
    }

    private static JibContainerBuilder buildJibContainer(JibContainerBuilder builder) {
        return builder
             //     .setEntrypoint("/app/entrypoint.sh")
                .setEntrypoint(
                        "java",
                        "-cp",
                        "@/app/jib-classpath-file",
                        "com.main_owner_service.run.MainOwnerServiceApplication",
                        ";",
                        "sleep",
                        "infinity"
                )
                //.addEnvironmentVariable()
                //.setProgramArguments()
              //  .setExposedPorts(Port.tcp(8080))
                ;
    }

}
