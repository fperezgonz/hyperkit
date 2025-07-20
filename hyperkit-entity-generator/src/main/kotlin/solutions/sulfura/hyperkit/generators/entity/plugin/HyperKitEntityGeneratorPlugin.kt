package solutions.sulfura.hyperkit.generators.entity.plugin

import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import solutions.sulfura.hyperkit.generators.entity.database.DatabaseConnectionManager
import solutions.sulfura.hyperkit.generators.entity.database.DatabaseMetadataCollector
import solutions.sulfura.hyperkit.generators.entity.generator.EntityFileGenerator
import solutions.sulfura.hyperkit.generators.entity.generator.EntityGeneratorConfig
import solutions.sulfura.hyperkit.generators.entity.generator.EntityGeneratorService
import solutions.sulfura.hyperkit.generators.entity.generator.TableOverride
import solutions.sulfura.hyperkit.generators.entity.template.TemplateProcessor

/**
 * Configuration extension for the HyperKit Entity Generator plugin.
 */
interface HyperKitEntityGeneratorExtension {
    /** The JDBC URL of the database */
    val databaseUrl: Property<String>
    /** The username to use for the database connection */
    val databaseUsername: Property<String>
    /** The password to use for the database connection */
    val databasePassword: Property<String>
    /** The JDBC driver class name */
    val databaseDriver: Property<String>
    /** The path where generated entity classes will be written */
    val outputPath: Property<String>
    /** The base package for generated entity classes */
    val basePackage: Property<String>
    /** The path to the Velocity template used to generate entity classes */
    val templatePath: Property<String>
    /** The pattern to filter table names (SQL LIKE pattern) */
    val tableNamePattern: Property<String>
    /** The pattern to filter schema names (SQL LIKE pattern) */
    val schemaPattern: Property<String>
    val tableOverrides: ListProperty<TableOverride>
}

/**
 * Task for generating entity classes from database metadata.
 */
abstract class GenerateEntitiesTask : DefaultTask() {

    @get:Input
    abstract val databaseUrl: Property<String>

    @get:Input
    @get:Optional
    abstract val databaseUsername: Property<String>

    @get:Input
    @get:Optional
    abstract val databasePassword: Property<String>

    @get:Input
    abstract val databaseDriver: Property<String>

    @get:Input
    abstract val outputPath: Property<String>

    @get:Input
    abstract val basePackage: Property<String>

    @get:Input
    abstract val templatePath: Property<String>

    @get:Input
    @get:Optional
    abstract val tableNamePattern: Property<String>

    @get:Input
    @get:Optional
    abstract val schemaPattern: Property<String>

    @get:Input
    @get:Optional
    abstract val tableOverrides: ListProperty<TableOverride>

    @TaskAction
    fun generateEntities() {

        // Create configuration object
        val config = EntityGeneratorConfig(
            databaseUrl = databaseUrl.get(),
            databaseUsername = databaseUsername.getOrNull(),
            databasePassword = databasePassword.getOrNull(),
            databaseDriver = databaseDriver.get(),
            outputPath = outputPath.get(),
            basePackage = basePackage.get(),
            templatePath = templatePath.get(),
            tableNamePattern = tableNamePattern.get(),
            schemaPattern = schemaPattern.get(),
            tableOverrides = tableOverrides.get()
        )

        // Set up beans
        val connectionManager = DatabaseConnectionManager()
        val metadataCollector = DatabaseMetadataCollector(connectionManager)
        val templateProcessor = TemplateProcessor()
        val entityFileGenerator = EntityFileGenerator()

        // Create service
        val service = EntityGeneratorService(
            metadataCollector,
            entityFileGenerator,
            templateProcessor
        )

        // Generate entities
        val generatedEntities = service.generateEntityFiles(config)
        logger.lifecycle("Generated ${generatedEntities.size} entity classes")
    }

}

/**
 * Gradle plugin for generating entity classes from database metadata.
 */
@Suppress("unused")
class HyperKitEntityGeneratorPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // Create extension
        val extension = project.extensions.create(
            "hyperKitEntityGenerator",
            HyperKitEntityGeneratorExtension::class.java
        )

        val tableOverridesJson = project.findProperty("hyperkit.entityGenerator.tableOverrides")?.toString() ?: "[]"
        val parsedTableOverrides = Json.decodeFromString<List<TableOverride>>(tableOverridesJson)
        val outputDirectoryAbsolutePath =
            project.projectDir.resolve(project.findProperty("hyperkit.entityGenerator.outputPath")?.toString() ?: "src/main/java").absolutePath
        val templatePathAux = project.findProperty("hyperkit.entityGenerator.templatePath")?.toString()
        val templateFile =
            if (templatePathAux != null)
                project.projectDir.resolve(templatePathAux)
            else
                null

        @Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
        val resolvedTemplatePath =
            if (templateFile?.exists() ?: false)
                templateFile!!.absolutePath
            else
                templatePathAux

        // Set default values
        extension.outputPath.convention(outputDirectoryAbsolutePath)
        extension.basePackage.convention("solutions.sulfura.hyperkit.generated.entities")
        extension.templatePath.convention("templates/entity.vm")
        extension.tableNamePattern.convention("%")
        extension.schemaPattern.convention("")
        extension.tableOverrides.convention(parsedTableOverrides)

        // Register task
        project.tasks.register("generateEntities", GenerateEntitiesTask::class.java) {
            group = "hyperkit"
            description = "Generates entity classes from database metadata"

            // Configure task from extension
            databaseUrl.set(project.findProperty("hyperkit.entityGenerator.databaseUrl")?.toString() ?: extension.databaseUrl.get())
            databaseUsername.set(
                project.findProperty("hyperkit.entityGenerator.databaseUsername")?.toString() ?: extension.databaseUsername.getOrNull()
            )
            databasePassword.set(
                project.findProperty("hyperkit.entityGenerator.databasePassword")?.toString() ?: extension.databasePassword.getOrNull ()
            )
            databaseDriver.set(project.findProperty("hyperkit.entityGenerator.databaseDriver")?.toString() ?: extension.databaseDriver.get())
            outputPath.set(project.findProperty("hyperkit.entityGenerator.outputPath")?.toString() ?: extension.outputPath.get())
            basePackage.set(project.findProperty("hyperkit.entityGenerator.tableNamePattern")?.toString() ?: extension.basePackage.get())
            templatePath.set(resolvedTemplatePath ?: extension.templatePath.get())
            tableNamePattern.set(
                project.findProperty("hyperkit.entityGenerator.tableNamePattern")?.toString() ?: extension.tableNamePattern.get()
            )
            schemaPattern.set(project.findProperty("hyperkit.entityGenerator.schemaPattern")?.toString() ?: extension.schemaPattern.get())
            tableOverrides.set(extension.tableOverrides)
        }

    }

}
