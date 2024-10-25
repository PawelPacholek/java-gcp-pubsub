package com.e2e_tests.main_owner_service;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.jib.JibImage;

import java.nio.file.Path;

public class MainOwnerService {

   public static GenericContainer<?> startContainer() {
       String imageName = "gcr.io/local-axle-425708-t0/main-owner-service";

       //ImageFromDockerfile mainOwnerServiceImage = new ImageFromDockerfile(imageName);
       JibImage mainOwnerServiceImage = new JibImage(
               imageName,
               jibContainerBuilder -> {
                   return jibContainerBuilder;
                 //  return jibContainerBuilder.setEntrypoint("echo", "Hello World");
               }
       );

       GenericContainer<?> container = new GenericContainer<>(mainOwnerServiceImage)
               .withExposedPorts(8080);
       container.start();
       return container;
   }

}
