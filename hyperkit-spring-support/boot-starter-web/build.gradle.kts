import org.jreleaser.model.Active
import kotlin.io.encoding.Base64

plugins {
    `java-library`
    `maven-publish`
    id("org.jreleaser")
    id("io.spring.dependency-management")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "hyperkit-boot-starter-web"
            from(components["java"])

            pom {
                name = "HyperKit Boot Starter Web"
                description =
                    "A spring boot starter that autoconfigures Hyperkit web components on Spring Boot applications"
                url = "https://gitlab.com/sulfura/hyperkit/-/tree/master/hyperkit-spring-support/boot-starter-web"
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

        if (System.getenv("CI_JOB_TOKEN") == null) {
            logger.warn("No CI job token found, skipping Gitlab publication")
        } else {
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
        }

        // Staging repository to prepare deployments to Maven Central
        maven {
            name = "MavenCentralStaging"
            url = project.layout.buildDirectory.dir("staging-deploy-$version").get().asFile.toURI()
        }
    }
}

jreleaser {

    project {
        authors = listOf("Francisco José Pérez González")
        license = "Apache-2.0"
        links {
            homepage = "https://gitlab.com/sulfura/hyperkit"
        }
    }

    // Prevent the "release.gitlab.token must not be blank" error
    yolo = true
    gitRootSearch = true

    signing {
        active = Active.ALWAYS

        val isSecretKeySet = System.getenv("MAVEN_SIGNING_SECRET_KEY_B64") != null
        val isPublicKeySet = System.getenv("MAVEN_SIGNING_PUBLIC_KEY_B64") != null

        pgp {
            armored = true
            secretKey = if (!isSecretKeySet) {
                null
            } else {
                String(Base64.decode(System.getenv("MAVEN_SIGNING_SECRET_KEY_B64")))
            }
            publicKey = if (!isPublicKeySet) {
                null
            } else {
                String(Base64.decode(System.getenv("MAVEN_SIGNING_PUBLIC_KEY_B64")))
            }
            passphrase = System.getenv("MAVEN_SIGNING_SECRET_KEY_PASSPHRASE")
        }
    }

    deploy {
        maven {
            mavenCentral {
                create("release-deploy") {
                    deploymentId = "${project.name.get()}_${project.version.get()}"
                    active = Active.RELEASE
                    url = "https://central.sonatype.com/api/v1/publisher"
                    username = System.getenv("SONATYPE_TOKEN_USERNAME")
                    password = System.getenv("SONATYPE_TOKEN_PASSWORD")
                    stagingRepository("build/staging-deploy-$version")

                }
            }
        }
    }

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
    // Essential Spring Boot AutoConfiguration Annotation
    api("org.springframework.boot:spring-boot-autoconfigure")
    // Required for implementing the WebMvcConfigurer interface
    api("org.springframework:spring-webmvc")
    api("org.springframework.data:spring-data-jpa")

    api(project(":hyperkit-dto-api"))
    api(project(":hyperkit-projections-dsl"))
    api(project(":hyperkit-spring-support:web"))
    api(project(":hyperkit-spring-support:message-converter-jackson3"))
    api(project(":hyperkit-utils:serialization:jackson3"))

    // Test dependencies
    testImplementation(project(":hyperkit-utils-standard-test-model"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-webmvc-test")
    testImplementation("org.hsqldb:hsqldb:2.7.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}