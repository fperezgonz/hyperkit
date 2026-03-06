plugins {
    java
    `java-library`
    id("solutions.sulfura.hyperkit-dto-generator") version "6.2.2-RELEASE"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jspecify:jspecify:1.0.0")
    implementation(project(":hyperkit-dto-api"))
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

}

hyperKitDtoGenerator {
    defaultOutputPackage = "solutions.sulfura.hyperkit.utils.test.model.dtos"
}
