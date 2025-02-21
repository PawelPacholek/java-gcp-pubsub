package com.label_owner_service.persistence;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway//(defaultRequestChannel = "labeledOwnerChannel")
public interface MyGate {

  @Gateway(requestChannel = "labeledOwnerChannel")
  void sendMessage(String out);

}
