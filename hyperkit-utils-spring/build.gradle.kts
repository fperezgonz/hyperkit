plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "solutions.sulfura"
version = "1.3.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":hyperkit-dto-api"))
    implementation(project(":hyperkit-projections-dsl"))
    implementation("org.jspecify:jspecify:1.0.0")
    implementation("org.hsqldb:hsqldb:2.7.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.github.perplexhub:rsql-jpa-spring-boot-starter:6.0.26")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}