plugins {
    java
    id("solutions.sulfura.hyperkit-dto-generator") version "6.2.2-RELEASE"
}


repositories {
    mavenCentral()
}

dependencies {
    implementation("solutions.sulfura:hyperkit-dto-api:6.2.2-RELEASE")
}

hyperKitDtoGenerator {
    inputPaths
}