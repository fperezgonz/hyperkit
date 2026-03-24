plugins {
    `java-library`
    `maven-publish`
    id("io.spring.dependency-management")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:4.0.4")
    }
}

dependencies {
    implementation(project(":hyperkit-dto-api"))
    implementation(project(":hyperkit-projections-dsl"))
    implementation(project(":hyperkit-utils-standard-test-model"))
    api("org.springframework.boot:spring-boot-webmvc-test")
    api("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.jspecify:jspecify:1.0.0")
    api("org.springframework.boot:spring-boot-starter-test")
}
