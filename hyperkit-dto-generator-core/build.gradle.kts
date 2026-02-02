import org.jreleaser.model.Active
import kotlin.io.encoding.Base64

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    id("org.jreleaser")
}

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "hyperkit-dto-generator-core"
            from(components["java"])

            pom {
                name = "HyperKit Dto generator core library"
                description =
                    "The library used by hyperkit-dto-generator-gradle-plugin and hyperkit-dto-generator-gradle-plugin to generate dtos from annotated classes"
                url = "https://gitlab.com/sulfura/hyperkit/-/tree/master/hyperkit-dto-generator-core"
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
            url = uri("https://gitlab.com/api/v4/projects/67836497/packages/maven")
            name = "GitLab"

            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication() {
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
            nexus2 {
                create("maven-central") {
                    stagingProfileId = "${project.name.get()}_${project.version.get()}"
                    url = "https://ossrh-staging-api.central.sonatype.com/service/local/"
                    username = System.getenv("SONATYPE_TOKEN_USERNAME")
                    password = System.getenv("SONATYPE_TOKEN_PASSWORD")
                    active = Active.ALWAYS
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
    api("fr.inria.gforge.spoon:spoon-core:11.3.0")
    api("org.apache.velocity:velocity-engine-core:2.4.1")
    implementation(project(":hyperkit-dto-api"))
    implementation("org.slf4j:slf4j-api:2.0.12")
}

kotlin {
    jvmToolchain(17)
}

java {
    withJavadocJar()
    withSourcesJar()
}