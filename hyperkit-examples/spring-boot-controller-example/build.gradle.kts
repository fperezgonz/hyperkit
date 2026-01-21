
plugins {
    java
    application
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("solutions.sulfura.hyperkit-dto-generator-plugin") version "6.2.1-SNAPSHOT"
    id("solutions.sulfura.hyperkit-entity-generator-plugin") version "6.2.1-SNAPSHOT"
}

hyperKitEntityGenerator {
    databaseUrl="jdbc:hsqldb:hsqldb/timer;shutdown=true"
    databaseDriver="org.hsqldb.jdbcDriver"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://public-package-registry.sulfura.solutions/")
    }
    mavenLocal()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}

application {
    // Define the main class for the application.
    mainClass = "solutions.sulfura.hyperkit.examples.App"
}

//hyperKitDtoGenerator {
//    rootOutputPath = "src/main/java/"
//}

dependencies {
    implementation("solutions.sulfura:hyperkit-spring-boot-starter:6.2.1-SNAPSHOT")
    implementation("org.hsqldb:hsqldb:2.7.1")
    implementation("org.jspecify:jspecify:1.0.0")
    implementation("io.github.perplexhub:rsql-jpa-spring-boot-starter:6.0.26")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.9")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}