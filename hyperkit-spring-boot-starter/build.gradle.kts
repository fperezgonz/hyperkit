plugins {
    `java-library`
    `maven-publish`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "hyperkit-spring-boot-starter"
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "Gitlab"
            url = uri("https://gitlab.com/api/v4/projects/67836497/packages/maven")
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
        mavenLocal()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Essential Spring Boot AutoConfiguration Annotation
    api("org.springframework.boot:spring-boot-autoconfigure:3.4.5")
    // Required for implementing the WebMvcConfigurer interface
    api("org.springframework:spring-webmvc:6.2.6")
    api("org.springframework.data:spring-data-jpa:3.4.5")
    api("com.fasterxml.jackson.core:jackson-databind:2.18.3")

    api(project(":hyperkit-dto-api"))
    api(project(":hyperkit-projections-dsl"))
    api(project(":hyperkit-utils-serialization"))
    api(project(":hyperkit-utils-spring"))

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.5")
    testImplementation("com.h2database:h2:2.2.224")
}

tasks.test {
    useJUnitPlatform()
}