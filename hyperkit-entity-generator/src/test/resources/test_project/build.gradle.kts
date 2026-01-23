import java.sql.DriverManager
import java.io.FileReader
import org.h2.tools.RunScript

plugins {
    id("solutions.sulfura.hyperkit-entity-generator")
    java
}

repositories {
    mavenCentral()
}

hyperKitEntityGenerator {
    databaseUrl.set(project.property("databaseUrl").toString())
    databaseUsername.set(project.property("databaseUsername").toString())
    databasePassword.set(project.property("databasePassword").toString())
    databaseDriver.set(project.property("databaseDriver").toString())
    basePackage.set(project.property("basePackage").toString())
}

dependencies {
    implementation("com.h2database:h2:2.2.224")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
}

// Database initialization task
tasks.register("initDatabase") {
    doLast {
        val connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "")
        FileReader("${project.projectDir}/src/main/resources/schema.sql").use { reader ->
            RunScript.execute(connection, reader)
        }
        connection.close()
    }
}