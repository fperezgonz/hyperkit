import java.sql.DriverManager
import java.io.FileReader
import org.h2.tools.RunScript

plugins {
    id("solutions.sulfura.hyperkit-entity-generator-plugin")
    java
}

repositories {
    mavenCentral()
}

hyperKitEntityGenerator {
    databaseUrl.set("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
    databaseUsername.set("sa")
    databasePassword.set("")
    databaseDriver.set("org.h2.Driver")
    basePackage.set("solutions.sulfura.hyperkit.test.entities")
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