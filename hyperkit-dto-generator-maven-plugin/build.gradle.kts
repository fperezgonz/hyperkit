import org.apache.tools.ant.taskdefs.condition.Os

repositories {
    mavenCentral()
}

dependencies {
    project(":hyperkit-dto-generator-core")
}

val generatePomFromTemplate by tasks.registering() {
    group = "build"
    description = "Uses pom.xml.templ as a template to create the pom.xml file that will be used to run the plugin building tasks"

    val templateFile = layout.projectDirectory.file("pom.xml.templ")
    val outputFile = layout.projectDirectory.file("pom.xml")

    // Declare inputs/outputs for Gradle up-to-date checks
    inputs.file(templateFile)
    outputs.file(outputFile)

    // Template variables
    val propsProvider = providers.provider {
        mapOf(
            "hyperkit.version" to project.version.toString()
        )
    }

    val props = propsProvider.get()
    inputs.properties(props)

    doLast {

        val template = templateFile.asFile.readText(Charsets.UTF_8)

        val rendered = props.entries.fold(template) { acc, (k, v) ->
            acc.replace("{{$k}}", v)
        }

        val out = outputFile.asFile
        out.parentFile.mkdirs()
        out.writeText(rendered, Charsets.UTF_8)

    }
}

val buildWithMaven by tasks.registering(Exec::class) {
    group = "build"
    workingDir = project.projectDir

    dependsOn(generatePomFromTemplate)
    dependsOn(":hyperkit-dto-generator-core:publishMavenPublicationToMavenLocal")

    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        commandLine("cmd",
            "/c",
            "./mvnw",
            "test"
        )
    } else {
        commandLine("./mvnw",
            "test",
            "test"
        )
    }
}