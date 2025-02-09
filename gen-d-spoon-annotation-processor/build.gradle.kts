plugins {
    `kotlin-dsl`
    publishing
    id("com.gradle.plugin-publish") version "1.3.1"
}

version = "0.1.0"

publishing {
    repositories {
        mavenLocal()
    }
}


gradlePlugin {
    website = "https://sulfura.cloud"
    vcsUrl = "https://sulfura.cloud"
    plugins {
        publishing{
            publications {

            }
            repositories {
                mavenLocal()
            }
        }
        create("gen-d-spoon-annotation-processor") {
            id = "solutions.sulfura.gen-d-spoon-annotation-processor"
            displayName = "Plugin for compatibility testing of Gradle plugins"
            description = "A plugin that helps you test your plugin against a variety of Gradle versions"
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
    implementation(project(":gen-d-api"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}