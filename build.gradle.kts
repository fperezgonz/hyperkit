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

fun findTasksInSubprojectsByName(name: String): List<TaskProvider<*>> {
    return subprojects.filter {
        it.tasks.findByName(name) != null
    }.map {
        it.tasks.named(name)
    }
}

fun createTaskReferencesInIncludedBuildsByName(name: String): List<TaskReference> {
    return gradle.includedBuilds.map {
        it.task(name)
    }
}

val runSubprojectPublishTasks by tasks.registering {
    dependsOn()
    dependsOn(findTasksInSubprojectsByName("publish"))
}

val jreleaserFullRelease by tasks.registering {
    mustRunAfter(runSubprojectPublishTasks)
    dependsOn(findTasksInSubprojectsByName("jreleaserFullRelease"))
}

val publishPlugins by tasks.registering {

    mustRunAfter(runSubprojectPublishTasks)

    val isSnapshotVersion = version.toString().endsWith("-SNAPSHOT")

    if (isSnapshotVersion) {
        logger.info("Snapshot version $version, skipping plugin publishing")
        return@registering
    }

    dependsOn(findTasksInSubprojectsByName("publishPlugins"))

}

val publish by tasks.registering {
    dependsOn("runSubprojectPublishTasks", "publishPlugins", "jreleaserFullRelease")
}

val publishMavenPublicationToMavenLocal by tasks.registering {
    dependsOn(findTasksInSubprojectsByName("publishMavenPublicationToMavenLocal"))
}

// Tasks to run "dto-generation-maven-example" tasks

val cleanDtoGenerationMavenExample by tasks.registering(MavenExec::class) {
    group = "build"
    dependsOn(":hyperkit-dto-generator-maven-plugin:generatePomFromTemplate")
    workingDir = project.file("hyperkit-examples/dto-generation-maven-example")
    mavenGoal = "clean"
    args("-Dhyperkit.version=$version")
}

val compileDtoGenerationMavenExample by tasks.registering(MavenExec::class) {
    group = "build"
    workingDir = project.file("hyperkit-examples/dto-generation-maven-example")
    mavenGoal = "compile"
    args("-Dhyperkit.version=$version")
    dependsOn(":hyperkit-dto-generator-maven-plugin:install")
}

val testDtoGenerationMavenExample by tasks.registering(MavenExec::class) {
    group = "verification"
    workingDir = project.file("hyperkit-examples/dto-generation-maven-example")
    mavenGoal = "test"
    args("-Dhyperkit.version=$version")
    dependsOn(compileDtoGenerationMavenExample)
}

val clean by tasks.registering(DefaultTask::class) {
    group = "build"
    dependsOn(createTaskReferencesInIncludedBuildsByName(":clean"))
    dependsOn(findTasksInSubprojectsByName("clean"))
    dependsOn(cleanDtoGenerationMavenExample)
}

val test by tasks.registering(Test::class) {
    group = "verification"
    dependsOn(createTaskReferencesInIncludedBuildsByName(":test"))
    dependsOn(findTasksInSubprojectsByName("test"))
    dependsOn(testDtoGenerationMavenExample)
}

val check by tasks.registering(Test::class) {
    group = "verification"
    dependsOn(createTaskReferencesInIncludedBuildsByName(":check"))
    dependsOn(findTasksInSubprojectsByName("check"))
    dependsOn(testDtoGenerationMavenExample)
}