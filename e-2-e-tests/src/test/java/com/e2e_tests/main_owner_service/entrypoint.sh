#!/bin/bash

{
  java -cp @/app/jib-classpath-file com.main_owner_service.run.MainOwnerServiceApplication
} || {
  sleep infinity
}
