package com.main_owner_service.persistence;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway//(defaultRequestChannel = "initialOwnerChannel")
public interface MyGate {

  @Gateway(requestChannel = "initialOwnerChannel")
  void sendMessage(String out);

}
