import org.jreleaser.model.Active
import kotlin.io.encoding.Base64

plugins {
    `java-library`
    `maven-publish`
    id("org.jreleaser") version "1.22.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}

publishing {

    publications {
        create<MavenPublication>("maven") {
            artifactId = "hyperkit-dto-api"
            from(components["java"])

            pom {
                name = "HyperKit Dto API"
                description = "The dto model api used by Hyperkit projects"
                url = "https://gitlab.com/sulfura/hyperkit/-/tree/master/hyperkit-dto-api"
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

//    release {
//        enabled = false
//        // Prevent the "release.gitlab.token must not be blank" error
//        gitlab {
//
//            skipRelease = true
//            token = "no-token"
//        }
//    }

    signing {
        active = Active.SNAPSHOT

        val isSecretKeySet = System.getenv("MAVEN_SIGNING_SECRET_KEY_B64") != null
        val isPublicKeySet = System.getenv("MAVEN_SIGNING_PUBLIC_KEY_B64") != null

        if (!isSecretKeySet) {
            logger.warn("MAVEN_SIGNING_SECRET_KEY_B64 environment variable is not set. Some JReleaser tasks will fail")
            return@signing
        }
        if (!isPublicKeySet) {
            logger.warn("MAVEN_SIGNING_PUBLIC_KEY_B64 environment variable is not set. Some JReleaser tasks will fail")
            return@signing
        }

        pgp {
            armored = true
            secretKey = String(Base64.decode(System.getenv("MAVEN_SIGNING_SECRET_KEY_B64")))
            publicKey = String(Base64.decode(System.getenv("MAVEN_SIGNING_PUBLIC_KEY_B64")))
            passphrase = System.getenv("MAVEN_SIGNING_SECRET_KEY_PASSPHRASE")
        }
    }

    deploy {
        maven {
            nexus2 {
                create("snapshot-deploy") {
                    url = "https://ossrh-staging-api.central.sonatype.com/service/local/"
                    username = System.getenv("SONATYPE_TOKEN_USERNAME")
                    password = System.getenv("SONATYPE_TOKEN_PASSWORD")
                    gitRootSearch = true
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

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    withJavadocJar()
    withSourcesJar()
}
