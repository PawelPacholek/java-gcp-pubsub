package com.e2e_tests.main_owner_service;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Path;

public class MainOwnerService {

   public void startContainer() {
       ImageFromDockerfile mainOwnerServiceImage = new ImageFromDockerfile("test-main-owner-service");
       GenericContainer<?> container = new GenericContainer<>(mainOwnerServiceImage);
   }

}
