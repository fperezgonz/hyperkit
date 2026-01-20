plugins {
    `java-library`
    `maven-publish`
    id("io.spring.dependency-management") version "1.1.7"
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

            pom {
                name = "HyperKit Dto API"
                description = "A spring boot starter that autoconfigures Hyperkit components on Spring Boot applications"
                url = "https://gitlab.com/sulfura/hyperkit/-/tree/master/hyperkit-spring-boot-starter"
                inceptionYear = "2023"
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    }
                }
                developers {
                    developer {
                        id = "fperezgonz@gmail.com"
                        name = "Francisco José Pérez Gonzalez"
                    }
                }
                scm {
                    connection = "scm:git:git://gitlab.com/sulfura/hyperkit.git"
                    developerConnection = "scm:git:ssh://gitlab.com:sulfura/hyperkit.git"
                    url = "https://gitlab.com/sulfura/hyperkit"
                }
            }

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

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.6")
    }
}

dependencies {
    // Essential Spring Boot AutoConfiguration Annotation
    api("org.springframework.boot:spring-boot-autoconfigure")
    // Required for implementing the WebMvcConfigurer interface
    api("org.springframework:spring-webmvc")
    api("org.springframework.data:spring-data-jpa")
    api("com.fasterxml.jackson.core:jackson-databind")

    api(project(":hyperkit-dto-api"))
    api(project(":hyperkit-projections-dsl"))
    api(project(":hyperkit-utils-serialization"))
    api(project(":hyperkit-utils-spring"))

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:2.2.224")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}