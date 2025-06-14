package solutions.sulfura.hyperkit.generators.entity.generator

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import solutions.sulfura.hyperkit.generators.entity.database.DatabaseMetadataCollector
import solutions.sulfura.hyperkit.generators.entity.template.TemplateProcessor

/**
 * Service for generating entity classes from database metadata.
 * This service orchestrates the entire entity generation process.
 */
@Service
class EntityGeneratorService(
    private val metadataCollector: DatabaseMetadataCollector,
    private val entityFileGenerator: EntityFileGenerator,
    private val templateProcessor: TemplateProcessor
) {

    private val logger = LoggerFactory.getLogger(EntityGeneratorService::class.java)

    fun generateEntities(config: EntityGeneratorConfig): List<GeneratedEntityData> {

        logger.info("Starting entity generation...")

        // Collect table metadata
        val tables = metadataCollector.collectTableMetadata(config)
        logger.info("Found ${tables.size} tables")

        // Generate entities
        val generatedEntities = tables.map { tableMetadata ->
            // Generate the entity class code
            val entityCode = templateProcessor.processTemplate(tableMetadata, config.templatePath)
            val generatedEntity = GeneratedEntityData(tableMetadata.className, tableMetadata.packageName, entityCode)

            return@map generatedEntity
        }

        return generatedEntities
    }

    /**
     * Generates entity classes for all tables matching the configured pattern.
     *
     * @return the number of entity classes generated
     */
    fun generateEntityFiles(config: EntityGeneratorConfig): List<GeneratedEntityData> {

        val generatedEntities = generateEntities(config)

        generatedEntities.map { generatedEntity ->
            val success = entityFileGenerator.generateEntityFile(generatedEntity, config.outputPath)
            if (success) {
                logger.info("Successfully generated entity: ${generatedEntity.entityName}")
            } else {
                logger.error("Failed to generate entity for table: ${generatedEntity.entityName}")
            }
        }

        return generatedEntities

    }
}

data class GeneratedEntityData(val entityName: String, val packageName: String, val entityCode: String)
