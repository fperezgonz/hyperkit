import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.PublishingExtension

plugins {
    id("org.jreleaser") version "1.22.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.gradle.plugin-publish") version "2.0.0" apply false
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

tasks.register("publish") {

    val isSnapshotVersion = version.toString().endsWith("-SNAPSHOT")

    dependsOn(subprojects.filter {
        it.tasks.findByName("publish") != null
    }.map {
        it.tasks.named("publish")
    })

    dependsOn(subprojects.filter {
        it.tasks.findByName("jreleaserFullRelease") != null
    }.map {
        it.tasks.named("jreleaserFullRelease")
    })

    if (!isSnapshotVersion) {

        dependsOn(subprojects.filter {
            it.tasks.findByName("publishPlugins") != null
        }.map {
            it.tasks.named("publishPlugins")
        })

    }

}

tasks.register("publishMavenPublicationToMavenLocal") {
    dependsOn(subprojects.filter {
        it.tasks.findByName("publishMavenPublicationToMavenLocal") != null
    }.map {
        it.tasks.named("publishMavenPublicationToMavenLocal")
    })
}
