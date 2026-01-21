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
}

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "hyperkit-utils-spring"
            from(components["java"])

            pom {
                name = "HyperKit Spring utils"
                description = "Tools to simplify the usage of Hyperkit DTOs with Spring"
                url = "https://gitlab.com/sulfura/hyperkit/-/tree/master/hyperkit-utils-spring"
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
        active = Active.SNAPSHOT

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

// Configuration used to set up mockito instrumentation to support running the tests using Gradle with JDK 21+ (https://javadoc.io/static/org.mockito/mockito-core/5.14.2/org/mockito/Mockito.html#0.3)
val mockitoAgent: Configuration by configurations.creating {
    isTransitive = false
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.6")
    }
}

dependencies {
    implementation(project(":hyperkit-dto-api"))
    implementation(project(":hyperkit-projections-dsl"))
    implementation(project(":hyperkit-utils-serialization"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.github.perplexhub:rsql-jpa-spring-boot-starter:6.0.26")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.9")
    compileOnly("org.jspecify:jspecify:1.0.0")
    testImplementation("org.hsqldb:hsqldb:2.7.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    mockitoAgent("org.mockito:mockito-core")
}

tasks.test {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
    useJUnitPlatform()
}
