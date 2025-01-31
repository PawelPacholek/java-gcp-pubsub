#!/bin/bash

{
  java -cp @/app/jib-classpath-file com.label_owner_service.run.LabelOwnerServiceApplication
} || {
  sleep infinity
}
