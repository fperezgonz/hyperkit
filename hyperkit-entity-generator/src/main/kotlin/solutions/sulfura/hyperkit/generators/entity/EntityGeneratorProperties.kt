package solutions.sulfura.hyperkit.generators.entity

import kotlinx.serialization.json.Json
import org.springframework.boot.context.properties.ConfigurationProperties
import solutions.sulfura.hyperkit.generators.entity.generator.EntityGeneratorConfig

/**
 * Configuration properties for the entity generator.
 * These properties are mapped from the application.properties or application.yml file.
 */
@ConfigurationProperties(prefix = "hyperkit.entity-generator")
class EntityGeneratorProperties(
    /** The JDBC URL of the database to connect to */
    val databaseUrl: String,

    /** The username to use for database connection */
    val databaseUsername: String,

    /** The password to use for database connection */
    val databasePassword: String,

    /** The JDBC driver class name */
    val databaseDriver: String,

    /** The path where generated entity classes will be written */
    var outputPath: String = "src/main/java",

    /** The base package for generated entity classes */
    val basePackage: String = "solutions.sulfura.hyperkit.generated.auto.entities",

    /** The path to the Velocity template for entity classes */
    val templatePath: String = "templates/entity.vm",

    /** The pattern to filter table names (SQL LIKE pattern) */
    val tableNamePattern: String = "%",

    /** The pattern to filter schema names (SQL LIKE pattern) */
    val schemaPattern: String = "",

    /**
     * Mapping of table overrides in JSON format
     */
    val tableOverrides: String="[]"

){

    fun toEntityGeneratorConfig(): EntityGeneratorConfig{
        return EntityGeneratorConfig(
            databaseUrl,
            databaseUsername,
            databasePassword,
            databaseDriver,
            outputPath,
            basePackage,
            templatePath,
            tableNamePattern,
            schemaPattern,
            Json.decodeFromString(tableOverrides)
        )
    }
}