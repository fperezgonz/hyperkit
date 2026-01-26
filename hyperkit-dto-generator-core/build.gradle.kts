plugins {
    `kotlin-dsl`
    `maven-publish`
}

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("hyperkit-dto-generator-core") {
            artifactId = "hyperkit-dto-generator-core"
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
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
        // Staging repository to prepare deployments to Maven Central
        maven {
            name = "MavenCentralStaging"
            url = project.layout.buildDirectory.dir("staging-deploy-$version").get().asFile.toURI()
        }
    }
}

dependencies {
    api("fr.inria.gforge.spoon:spoon-core:11.3.0")
    api("org.apache.velocity:velocity-engine-core:2.4.1")
    implementation(project(":hyperkit-dto-api"))
    implementation("org.slf4j:slf4j-api:2.0.12")
}

kotlin {
    jvmToolchain(17)
}
