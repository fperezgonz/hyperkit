plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.3.1"
}

version = "0.1.0"


gradlePlugin {
    website = "https://sulfura.cloud"
    vcsUrl = "https://sulfura.cloud"
    plugins {
        create("gen-d-spoon-annotation-processor") {
            id = "solutions.sulfura.gen-d-spoon-annotation-processor"
            displayName = "Gen-D Spoon Annotation Processor Plugin"
            description = "A plugin that generates DTO classes using gen-d"
            tags = listOf("testing", "integrationTesting", "compatibility")
            implementationClass = "solutions.sulfura.GenDAnnotationProcessorPlugin"
        }
    }
}


repositories {
    mavenCentral()
}


dependencies {
    implementation("fr.inria.gforge.spoon:spoon-core:11.2.0")
    implementation("org.apache.velocity:velocity-engine-core:2.4.1")
    implementation(project(":gen-d-api"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}