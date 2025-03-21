#!/bin/bash

{
  java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -cp @/app/jib-classpath-file com.main_owner_service.run.MainOwnerServiceApplication
} || {
  sleep infinity
}
