plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    id("org.springframework.boot") version "3.4.6"
    id("io.spring.dependency-management") version "1.1.7"
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.3.1"
}

repositories {
    mavenCentral()
}

// Prevent logger conflicts
configurations.all {
    exclude(group = "ch.qos.logback")
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

gradlePlugin {
    website = "https://sulfura.solutions"
    plugins {
        create("hyperkit-entity-generator-plugin") {
            id = "solutions.sulfura.hyperkit-entity-generator-plugin"
            displayName = "HyperKit Entity Generator Plugin"
            description = "A plugin that generates entity classes from database metadata"
            tags = listOf("entity", "jpa", "database")
            implementationClass = "solutions.sulfura.hyperkit.generators.entity.plugin.HyperKitEntityGeneratorPlugin"
        }
    }
}

publishing {
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
    }
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter:3.4.6")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:3.4.6")
    implementation("org.springframework.boot:spring-boot-configuration-processor:3.4.6")

    // JPA
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Velocity
    implementation("org.apache.velocity:velocity-engine-core:2.3")

    // Database drivers
    implementation("com.h2database:h2:2.3.232")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.2")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.hsqldb:hsqldb:2.7.2")

    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
//    testLogging.showStandardStreams = true
    useJUnitPlatform()
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    eachFile {
        if (path.contains("kotlin-stdlib")) {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
    }
}

// Remove entity source files generated in previous test runs to avoid false positives
tasks.named<Copy>("processTestResources") {
    outputs.upToDateWhen { false }
    doFirst {
        delete("${layout.buildDirectory}/resources/test")
    }
}

kotlin {
    jvmToolchain(21)
}
