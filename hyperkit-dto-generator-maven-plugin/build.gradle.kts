plugins {
    `kotlin-dsl`
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":hyperkit-dto-generator-core"))
    implementation("org.apache.maven:maven-plugin-api:3.9.6")
    implementation("org.apache.maven.plugin-tools:maven-plugin-annotations:3.11.0")
    implementation("org.apache.maven:maven-project:2.2.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.apache.maven.plugin-testing:maven-plugin-testing-harness:3.3.0")
    testImplementation("org.apache.maven:maven-core:3.9.6")
    testImplementation("org.apache.maven:maven-compat:3.9.6")
    testImplementation("org.apache.maven:maven-model:3.9.6")
    testImplementation("org.apache.maven:maven-settings:3.9.6")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
