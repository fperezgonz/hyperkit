import org.apache.tools.ant.taskdefs.condition.Os

tasks.register<Exec>("build") {
    workingDir = project.projectDir

    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        commandLine("cmd", "npm", "run", "build")
    } else {
        commandLine("npm", "run", "build")
    }

}

tasks.register<Exec>("publish") {
    workingDir = project.projectDir

    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        commandLine("cmd", "npm", "publish")
    } else {
        commandLine("npm", "publish")
    }

    dependsOn("build")

}