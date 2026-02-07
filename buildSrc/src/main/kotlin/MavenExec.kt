import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Paths

abstract class MavenExec : Exec() {

    @get:Input
    abstract val mavenGoal: Property<String>

    @TaskAction
    fun execMavenCommand() {
        val commandLineArgs = mutableListOf<String>()

        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            commandLineArgs.add("cmd")
            commandLineArgs.add("/c")
        } else {
            commandLineArgs.add("sh")
        }

        if (!Files.exists(Paths.get(workingDir.path, "mvnw"))) {
            logger.error("mvnw not found in the working directory specified for the task '${workingDir.path}'")
        }

        commandLineArgs.add("mvnw")

        if (logger.isDebugEnabled || logger.isTraceEnabled) {
            // Set Maven debug level
            commandLineArgs.add("-X")
        }

        commandLineArgs.add(mavenGoal.get())
        commandLineArgs.addAll(args)

        commandLine(commandLineArgs)

    }
}