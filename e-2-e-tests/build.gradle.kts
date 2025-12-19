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
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.1")
    testImplementation("org.springframework.boot:spring-boot-test-autoconfigure:3.3.3")
    testImplementation("org.testcontainers:testcontainers:2.0.2")
    testImplementation("com.google.cloud.tools:jib-core:0.28.1")
    testImplementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.1")

}

tasks.test {
    useJUnitPlatform()
}
