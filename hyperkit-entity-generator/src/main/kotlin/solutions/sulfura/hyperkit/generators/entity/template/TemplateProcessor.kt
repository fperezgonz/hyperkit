package solutions.sulfura.hyperkit.generators.entity.template

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.springframework.stereotype.Component
import solutions.sulfura.hyperkit.generators.entity.database.TableMetadata
import java.io.StringWriter
import java.util.*

/**
 * Processes Velocity templates for entity generation.
 * This component is responsible for merging the template with the metadata
 * to generate the entity class code.
 */
@Component
class TemplateProcessor() {

    private val velocityEngine: VelocityEngine = initVelocityEngine()

    /**
     * Initializes the Velocity engine.
     *
     * @return the initialized Velocity engine
     */
    private fun initVelocityEngine(): VelocityEngine {
        val velocityEngine = VelocityEngine()
        val props = Properties()

        props.setProperty("resource.loaders", "file, class")
        velocityEngine.setProperty(
            "resource.loader.file.class",
            "org.apache.velocity.runtime.resource.loader.FileResourceLoader"
        )
        props.setProperty(
            "resource.loader.class.class",
            "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
        )
        // Empty path allows absolute paths
        props.setProperty("resource.loader.file.path", "")

        // Configure Velocity
        props.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute")
        props.setProperty("resource.default_encoding", "UTF-8")
        props.setProperty("output.encoding", "UTF-8")

        velocityEngine.init(props)
        return velocityEngine
    }

    /**
     * Processes a template with the given table metadata.
     *
     * @param tableMetadata the table metadata
     * @return the generated entity class code
     */
    fun processTemplate(tableMetadata: TableMetadata, templatePath: String): String {

        val context = createVelocityContext(tableMetadata)
        val template = velocityEngine.getTemplate(templatePath)
        val writer = StringWriter()

        template.merge(context, writer)
        return writer.toString()
    }

    /**
     * Creates a Velocity context with the table metadata.
     *
     * @param tableMetadata the table metadata
     * @return the Velocity context
     */
    private fun createVelocityContext(tableMetadata: TableMetadata): VelocityContext {

        val context = VelocityContext()

        // Add table metadata
        context.put("packageName", tableMetadata.packageName)
        context.put("className", tableMetadata.className)
        context.put("tableName", tableMetadata.tableName)
        context.put("schema", tableMetadata.schema)

        // Add columns and foreign keys
        context.put("columns", tableMetadata.columns)
        context.put("foreignKeys", tableMetadata.foreignKeys)
        context.put("primaryKeyFields", tableMetadata.primaryKeyColumns)

        // Add imports
        context.put("imports", generateImports(tableMetadata))

        return context
    }

    /**
     * Generates the import statements for the entity class.
     *
     * @param tableMetadata the table metadata
     * @return the list of import statements
     */
    private fun generateImports(tableMetadata: TableMetadata): List<String> {
        val imports = mutableListOf<String>()

        // JPA imports in specific order
        imports.add("jakarta.persistence.Entity")
        imports.add("jakarta.persistence.Table")
        imports.add("jakarta.persistence.Column")

        // Add imports for the primary key
        if (tableMetadata.primaryKeyColumns.isNotEmpty()) {

            imports.add("jakarta.persistence.Id")

            // Add import for GeneratedValue if any primary key is auto-increment
            if (tableMetadata.primaryKeyColumns.any { it.autoIncrement }) {

                imports.add("jakarta.persistence.GeneratedValue")
                imports.add("jakarta.persistence.GenerationType")

            }

        }

        // Add imports for foreign keys in a specific order
        if (tableMetadata.foreignKeys.isNotEmpty()) {
            // Check for OneToOne relationships
            val hasOneToOne = tableMetadata.foreignKeys.any { it.relationshipType.contains("ONE_TO_ONE") }
            val hasManyToOne = tableMetadata.foreignKeys.any { it.relationshipType.contains("MANY_TO_ONE") }

            if (hasOneToOne) {
                imports.add("jakarta.persistence.OneToOne")
            }
            if (hasManyToOne) {
                imports.add("jakarta.persistence.ManyToOne")
            }
            // Always include JoinColumn for foreign keys
            imports.add("jakarta.persistence.JoinColumn")
        }

        // Add imports for Java types in a specific order
        val javaTypes = tableMetadata.columns.map { it.javaType }.toSet()

        if (javaTypes.contains("BigDecimal")) {
            imports.add("java.math.BigDecimal")
        }

        if (javaTypes.contains("LocalDate")) {
            imports.add("java.time.LocalDate")
        }

        if (javaTypes.contains("LocalTime")) {
            imports.add("java.time.LocalTime")
        }

        if (javaTypes.contains("LocalDateTime")) {
            imports.add("java.time.LocalDateTime")
        }

        if (javaTypes.contains("OffsetDateTime")) {
            imports.add("java.time.OffsetDateTime")
        }

        if (javaTypes.contains("Blob")) {
            imports.add("java.sql.Blob")
        }

        if (javaTypes.contains("Clob")) {
            imports.add("java.sql.Clob")
        }

        if (javaTypes.contains("Array")) {
            imports.add("java.sql.Array")
        }

        // Add import for Objects (used in equals and hashCode)
        imports.add("java.util.Objects")

        return imports
    }
}
