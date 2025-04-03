plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.3.1"
}

publishing {
    repositories{
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

gradlePlugin {
    website = "https://sulfura.cloud"
    vcsUrl = "https://sulfura.cloud"
    plugins {
        create("hyperkit-dto-generator-plugin") {
            id = "solutions.sulfura.hyperkit-dto-generator-plugin"
            displayName = "HyperKit Dto Generator Plugin"
            description = "A plugin that generates DTO classes based on annotations"
            tags = listOf("testing", "integrationTesting", "compatibility")
            implementationClass = "solutions.sulfura.HyperKitDtoGeneratorPlugin"
        }
    }
}


repositories {
    mavenCentral()
}


dependencies {
    implementation("fr.inria.gforge.spoon:spoon-core:11.2.0")
    implementation("org.apache.velocity:velocity-engine-core:2.4.1")
    implementation(project(":hyperkit-dto-api"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}