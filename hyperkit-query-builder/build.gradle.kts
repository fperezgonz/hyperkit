import org.apache.tools.ant.taskdefs.condition.Os

tasks.register<Exec>("npm-install") {
    workingDir = project.projectDir
    group = "verification"

    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        commandLine("cmd", "/c", "npm", "install")
    } else {
        commandLine("npm", "install")
    }

}

tasks.register<Exec>("test") {
    workingDir = project.projectDir
    group = "verification"
    dependsOn("npm-install")

    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        commandLine("cmd", "/c", "npm", "run", "test")
    } else {
        commandLine("npm", "run", "test")
    }

}

tasks.register<DefaultTask>("check") {
    group = "verification"
    dependsOn("test")
}

tasks.register<Exec>("build") {
    workingDir = project.projectDir
    group = "build"
    dependsOn("check")

    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        commandLine("cmd", "/c", "npm", "run", "build")
    } else {
        commandLine("npm", "run", "build")
    }

}

tasks.register<Exec>("publish") {
    workingDir = project.projectDir

    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        commandLine("cmd", "/c", "npm", "publish")
    } else {
        commandLine("npm", "publish")
    }

    dependsOn("build")

}