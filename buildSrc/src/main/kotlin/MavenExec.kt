import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class MavenExec : Exec() {

    @get:Input
    abstract val mavenGoal: Property<String>

    @TaskAction
    fun execMavenCommand() {
        val commandLineArgs = mutableListOf<String>()

        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            commandLineArgs.add("cmd")
            commandLineArgs.add("/c")
        }

        commandLineArgs.add("./mvnw")
        commandLineArgs.add(mavenGoal.get())
        commandLineArgs.addAll(args)

        commandLine(commandLineArgs)

    }
}