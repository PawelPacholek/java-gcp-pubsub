plugins {
    application
    java
    id("org.springframework.boot") version "3.3.3"
}

repositories {
    mavenCentral()
}

group = "demo"
version = "1.0.0-SNAPSHOT"
description = "Spring Cloud GCP Pub/Sub Code Sample"
java.sourceCompatibility = JavaVersion.VERSION_21

dependencies {

    testImplementation(project(":pubsub-emulator"))
    testImplementation(project(":redis-instance"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("org.springframework.boot:spring-boot-test-autoconfigure:3.3.3")
    testImplementation("org.testcontainers:gcloud:1.20.1")
    testImplementation("com.google.cloud.tools:jib-core:0.26.0")
    testImplementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")

}

tasks.test {
    useJUnitPlatform()
}
