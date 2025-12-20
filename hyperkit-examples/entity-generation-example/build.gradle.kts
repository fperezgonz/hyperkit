plugins {
    id("solutions.sulfura.hyperkit-entity-generator-plugin") version "6.2.0-SNAPSHOT"
    java
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    // Not required for entity generation, but the generated code uses Persistence annotations and will not compile if they are not on the classpath
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
}


// Schema creation with liquibase

val dbUrl = "jdbc:hsqldb:file:${layout.buildDirectory.get().asFile}/hsqldb/example;shutdown=true"
val dbUser = "usr"
val dbPassword = "pass"

val liquibaseRuntime by configurations.creating

dependencies {
    liquibaseRuntime("org.liquibase:liquibase-core:4.25.1")
    liquibaseRuntime("org.hsqldb:hsqldb:2.7.2")
    liquibaseRuntime("org.yaml:snakeyaml:2.2")
}


tasks.register<JavaExec>("updateDatabase") {
    group = "database"
    mainClass.set("liquibase.integration.commandline.Main")
    classpath = liquibaseRuntime
    args = listOf(
        "--changeLogFile=src/main/resources/db/changelog/db.changelog-master.sql",
        "--url=$dbUrl",
        "--username=$dbUser",
        "--password=$dbPassword",
        "--driver=org.hsqldb.jdbc.JDBCDriver",
        "update"
    )

    // Always recreate the database
    doFirst { delete(layout.buildDirectory.dir("hsqldb").get().asFile) }

}


// Hyperkit entity generator configuration

hyperKitEntityGenerator {
    databaseUrl.set(dbUrl)
    databaseUsername.set(dbUser)
    databasePassword.set(dbPassword)
    databaseDriver.set("org.hsqldb.jdbc.JDBCDriver")
    // Include only tables that start with APP_
    tableNamePattern.set("APP_%")
    outputPath.set("${projectDir}/src/main/java")
}