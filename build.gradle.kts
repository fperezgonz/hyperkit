import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.PublishingExtension

plugins {
    id("org.jreleaser") version "1.22.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    kotlin("jvm") version "2.2.21" apply false
    id("com.gradle.plugin-publish") version "2.0.0" apply false
}

repositories {
    mavenCentral()
}

allprojects {
    group = "solutions.sulfura"
    version = "6.2.2-SNAPSHOT"
}

subprojects {
    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            withSourcesJar()
        }
    }

    plugins.withType<MavenPublishPlugin> {
        extensions.configure<PublishingExtension> {
            publications {
                withType<MavenPublication> {
                    // Sources JAR will be automatically included because of withSourcesJar()
                }
            }
        }
    }
}

val runSubprojectPublishTasks by tasks.registering {

    dependsOn(subprojects.filter {
        it.tasks.findByName("publish") != null
    }.map {
        it.tasks.named("publish")
    })

}

val jreleaserFullRelease by tasks.registering {

    mustRunAfter(runSubprojectPublishTasks)

    dependsOn(subprojects.filter {
        it.tasks.findByName("jreleaserFullRelease") != null
    }.map {
        it.tasks.named("jreleaserFullRelease")
    })

}

val publishPlugins by tasks.registering {

    mustRunAfter(runSubprojectPublishTasks)

    val isSnapshotVersion = version.toString().endsWith("-SNAPSHOT")

    if (isSnapshotVersion) {
        logger.info("Snapshot version $version, skipping plugin publishing")
        return@registering
    }

    dependsOn(subprojects.filter {
        it.tasks.findByName("publishPlugins") != null
    }.map {
        it.tasks.named("publishPlugins")
    })

}

val publish by tasks.registering {
    dependsOn("runSubprojectPublishTasks", "publishPlugins", "jreleaserFullRelease")
}

val publishMavenPublicationToMavenLocal by tasks.registering {
    dependsOn(subprojects.filter {
        it.tasks.findByName("publishMavenPublicationToMavenLocal") != null
    }.map {
        it.tasks.named("publishMavenPublicationToMavenLocal")
    })
}

// Tasks to run dto generation maven example tasks
val compileDtoGenerationMavenExample by tasks.registering(Exec::class) {
    group = "build"
    workingDir = project.file("hyperkit-examples/dto-generation-maven-example")
    dependsOn(":hyperkit-dto-generator-maven-plugin:install")

    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        commandLine("cmd",
            "/c",
            "./mvnw",
            "compile",
            "-Dhyperkit.version=$version"
        )
    } else {
        commandLine("./mvnw",
            "compile",
            "-Dhyperkit.version=$version"
        )
    }
}

val testDtoGenerationMavenExample by tasks.registering(Exec::class) {
    group = "verification"
    workingDir = project.file("hyperkit-examples/dto-generation-maven-example")

    dependsOn(compileDtoGenerationMavenExample)

    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        commandLine("cmd",
            "/c",
            "./mvnw",
            "test",
            "-Dhyperkit.version=$version"
        )
    } else {
        commandLine("./mvnw",
            "test",
            "-Dhyperkit.version=$version"
        )
    }
}