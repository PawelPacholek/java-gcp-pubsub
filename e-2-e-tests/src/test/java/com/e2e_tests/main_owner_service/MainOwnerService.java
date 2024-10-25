package com.e2e_tests.main_owner_service;

import com.google.cloud.tools.jib.api.JibContainerBuilder;
import com.google.cloud.tools.jib.api.buildplan.Port;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.jib.JibImage;

import java.nio.file.Path;

public class MainOwnerService {

   public static GenericContainer<?> startContainer() {
       String imageName = "gcr.io/local-axle-425708-t0/main-owner-service";

       //ImageFromDockerfile mainOwnerServiceImage = new ImageFromDockerfile(imageName);
       JibImage mainOwnerServiceImage =
               new JibImage(imageName, MainOwnerService::buildJibContainer);

       GenericContainer<?> container = new GenericContainer<>(mainOwnerServiceImage)
               .withCommand("apt update && apt -y install netcat")
               .withExposedPorts(8080);
       container.start();
       return container;
   }

   private static JibContainerBuilder buildJibContainer(JibContainerBuilder builder) {
     return builder
             //.setProgramArguments()
             .setExposedPorts(Port.tcp(8080));
   }

}
