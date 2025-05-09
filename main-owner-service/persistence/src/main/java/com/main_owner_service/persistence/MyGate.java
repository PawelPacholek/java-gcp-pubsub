package com.main_owner_service.persistence;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface MyGate {

  @Gateway(requestChannel = "initialOwnerChannel")
  void sendMessage(String out);

}
