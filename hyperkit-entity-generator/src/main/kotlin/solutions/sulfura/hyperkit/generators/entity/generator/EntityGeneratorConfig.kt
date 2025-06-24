package solutions.sulfura.hyperkit.generators.entity.generator

import kotlinx.serialization.Serializable

/**
 * Configuration properties for the entity generator.
 */
data class EntityGeneratorConfig(
    /** The JDBC URL of the database to connect to */
    val databaseUrl: String,

    /** The username to use for database connection */
    val databaseUsername: String?,

    /** The password to use for database connection */
    val databasePassword: String?,

    /** The JDBC driver class name */
    val databaseDriver: String,

    /** The path where generated entity classes will be written */
    var outputPath: String = "src/main/java",

    /** The base package for generated entity classes */
    val basePackage: String = "solutions.sulfura.hyperkit.generated.auto.entities",

    /** The path to the Velocity template for entity classes */
    val templatePath: String,

    /** The pattern to filter table names (SQL LIKE pattern) */
    val tableNamePattern: String = "%",

    /** The pattern to filter schema names (SQL LIKE pattern) */
    val schemaPattern: String = "",

    /** Mapping of table overrides */
    val tableOverrides: List<TableOverride>,
)

@Serializable
data class TableOverride(val schemaName: String, val tableName: String, val packageName: String, val className: String)