plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
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

gradlePlugin {
    website = "https://gitlab.com/sulfura/hyperkit/-/tree/master/hyperkit-dto-generator-plugin"
    vcsUrl = "https://gitlab.com/sulfura/hyperkit/-/tree/master/hyperkit-dto-generator-plugin"
    plugins {
        register("hyperkitDtoGenerator") {
            id = "solutions.sulfura.hyperkit-dto-generator"
            displayName = "HyperKit Dto Generator"
            description = "A plugin that generates DTO classes based on annotated classes"
            tags = listOf("hyperkit", "generator", "dto")
            implementationClass = "solutions.sulfura.HyperKitDtoGeneratorPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("fr.inria.gforge.spoon:spoon-core:11.3.0")
    implementation("org.apache.velocity:velocity-engine-core:2.4.1")
    implementation(project(":hyperkit-dto-api"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    // Remove files created by the test project
    doLast {
        delete(
            layout.buildDirectory.dir("resources/test/test_project/.gradle"),
            layout.buildDirectory.dir("resources/test/test_project/src/out")
        )
    }
}

kotlin {
    jvmToolchain(17)
}