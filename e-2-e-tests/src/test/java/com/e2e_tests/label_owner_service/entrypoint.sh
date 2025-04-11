#!/bin/bash

{
  java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006 -cp @/app/jib-classpath-file com.label_owner_service.run.LabelOwnerServiceApplication

} || {
  sleep infinity
}
