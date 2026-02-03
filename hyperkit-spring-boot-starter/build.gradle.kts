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
            nexus2 {
                create("snapshot-deploy") {
                    url = "https://ossrh-staging-api.central.sonatype.com/service/local/"
                    username = System.getenv("SONATYPE_TOKEN_USERNAME")
                    password = System.getenv("SONATYPE_TOKEN_PASSWORD")
                    active = Active.SNAPSHOT
                    snapshotUrl = "https://central.sonatype.com/repository/maven-snapshots/"
                    applyMavenCentralRules = true
                    snapshotSupported = true
                    closeRepository = true
                    releaseRepository = true

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