plugins {
    java
    id("solutions.sulfura.hyperkit-dto-generator-plugin") version "6.2.1-RELEASE"
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("solutions.sulfura:hyperkit-dto-api:6.2.0-RELEASE")
}

hyperKitDtoGenerator {
    inputPaths
}