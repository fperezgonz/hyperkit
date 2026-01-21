plugins {
    java
    id("solutions.sulfura.hyperkit-dto-generator-plugin") version "6.2.1-SNAPSHOT"
}


repositories {
    maven {
        url = uri("https://public-package-registry.sulfura.solutions/")
    }
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("solutions.sulfura:hyperkit-spring-boot-starter:6.2.1-SNAPSHOT")
}

hyperKitDtoGenerator {
    inputPaths
}