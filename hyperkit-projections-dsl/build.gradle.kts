plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly("org.jspecify:jspecify:1.0.0")
    implementation(project(":hyperkit-dto-api"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "hyperkit-projections-dsl"
            from(components["java"])

            pom {
                name = "HyperKit Projections DSL"
                description = "Tools for parsing and applying projections to dtos generated with hyperkit, using a simple dsl"
                url = "https://gitlab.com/sulfura/hyperkit/-/tree/master/hyperkit-projections-dsl"
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
            credentials(HttpHeaderCredentials::class.java) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withJavadocJar()
    withSourcesJar()
}