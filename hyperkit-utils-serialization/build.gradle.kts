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