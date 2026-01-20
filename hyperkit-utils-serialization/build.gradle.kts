plugins {
    java
    `java-library`
    `maven-publish`
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
            artifactId = "hyperkit-utils-serialization"
            from(components["java"])

            pom {
                name = "HyperKit Serialization Utils"
                description = "Jackson modules and adapters to serialize ValueWrapper and dtos generated with Hyperkit"
                url = "https://gitlab.com/sulfura/hyperkit/-/tree/master/hyperkit-utils-serialization"
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
    }
}

dependencies {
    implementation(project(":hyperkit-dto-api"))
    implementation(project(":hyperkit-projections-dsl"))
    compileOnly("org.jspecify:jspecify:1.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(platform("org.junit:junit-bom:5.12.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.test {
    useJUnitPlatform()
}