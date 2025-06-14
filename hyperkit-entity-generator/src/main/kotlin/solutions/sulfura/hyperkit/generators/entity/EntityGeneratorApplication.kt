package solutions.sulfura.hyperkit.generators.entity

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

/**
 * Main application class for the HyperKit Entity Generator.
 * This class serves as the entry point for running the entity generator as a standalone application.
 */
@SpringBootApplication
@EnableConfigurationProperties(EntityGeneratorProperties::class)
class EntityGeneratorApplication

/**
 * Main function to start the application.
 */
fun main(args: Array<String>) {
    runApplication<EntityGeneratorApplication>(*args)
}