#!/bin/bash

if [ -f "/certs/server.key.password" ]; then
    export SERVER_KEY_PASSWORD=$(< /certs/server.key.password)
fi

if [[ -z "${CUSTOM_JAVA_OPTS}" ]]; then
  JAVA_OPTS="-Xms192m -Xmx192m -XX:CompressedClassSpaceSize=64m"
else
  JAVA_OPTS="${CUSTOM_JAVA_OPTS}"
fi

java -Djdk.lang.Process.launchMechanism=vfork \
     -Dspring.profiles.active=${DEPLOYMENT_PROFILE} \
     ${JAVA_OPTS} \
     -jar /deployments/pdf-service.jar
