import org.jreleaser.model.Active
import kotlin.io.encoding.Base64

plugins {
    java
    `java-library`
    `maven-publish`
    id("org.jreleaser")
    id("me.champeau.jmh") version "0.7.3"
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "hyperkit-utils-serialization-jackson2"
            from(components["java"])

            pom {
                name = "HyperKit Serialization Utils"
                description = "Jackson modules and adapters to serialize ValueWrapper and dtos generated with Hyperkit"
                url = "https://gitlab.com/sulfura/hyperkit/-/tree/master/hyperkit-utils/serialization/jackson2"
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

dependencies {
    implementation(project(":hyperkit-dto-api"))
    implementation(project(":hyperkit-projections-dsl"))
    compileOnly("org.jspecify:jspecify:1.0.0")
    api("com.fasterxml.jackson.core:jackson-databind:2.21.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(project(":hyperkit-utils:serialization:specification"))
    testImplementation(project(":hyperkit-utils-standard-test-model"))
    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
}

jmh {
    jmhVersion.set("1.37")
    duplicateClassesStrategy.set(DuplicatesStrategy.EXCLUDE)
}

tasks.test {
    useJUnitPlatform()
}