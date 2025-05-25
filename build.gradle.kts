import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.PublishingExtension

allprojects {
    group = "solutions.sulfura"
    version = "4.1.0-RELEASE"
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
    dependsOn(subprojects.map { it.tasks.named("publish") })
}

tasks.register("publishMavenPublicationToMavenLocal") {
    dependsOn(subprojects.map { it.tasks.named("publishMavenPublicationToMavenLocal") })
}
