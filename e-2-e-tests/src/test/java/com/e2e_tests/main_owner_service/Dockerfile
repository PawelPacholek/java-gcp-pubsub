# Prepare ubi9-micro base image with installed java
ARG MICRODIR=/microdir

# Using clean ubi9-micro
FROM redhat/ubi9-micro AS BASE

# Copy ubi9-micro to directory inside ubi9 and install java for ubi9-micro using ubi9
FROM redhat/ubi9 AS BUILD
ARG MICRODIR
RUN mkdir /microdir
COPY --from=BASE / ${MICRODIR}
RUN yum install --installroot ${MICRODIR} -y java-21-openjdk-headless &&\
  yum clean all --installroot ${MICRODIR}

# Create fresh image and copy contents of ubi9-micro with installed java
FROM scratch AS IMAGE
ARG MICRODIR
COPY --from=BUILD ${MICRODIR}/ .

# Deploy pdf-service
COPY build/libs/pdf-service.jar /deployments/pdf-service.jar
RUN chown 1001 /deployments/pdf-service.jar
RUN chmod 400 /deployments/pdf-service.jar
RUN mkdir /deployments/logs
RUN chown 1001 /deployments/logs
RUN chmod 700 /deployments/logs
COPY entrypoint.sh /deployments/entrypoint.sh
RUN chown 1001 /deployments/entrypoint.sh
RUN chmod 700 /deployments/entrypoint.sh

USER 1001:1001
EXPOSE 8080/tcp
EXPOSE 8088/tcp
ENV DEPLOYMENT_PROFILE=production

ENTRYPOINT ["/deployments/entrypoint.sh"]