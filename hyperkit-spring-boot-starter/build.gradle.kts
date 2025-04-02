plugins {
    `java-library`
    `maven-publish`
}

group = "solutions.sulfura"
version = "1.3.0-SNAPSHOT"

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
    implementation("org.springframework.boot:spring-boot-autoconfigure:3.4.4")
    // Required for implementing the WebMvcConfigurer interface
    implementation("org.springframework:spring-webmvc:6.2.5")

    api(project(":hyperkit-dto-api"))
    api(project(":hyperkit-projections-dsl"))
    api(project(":hyperkit-utils-serialization"))
    api(project(":hyperkit-utils-spring"))

}

tasks.test {
    useJUnitPlatform()
}