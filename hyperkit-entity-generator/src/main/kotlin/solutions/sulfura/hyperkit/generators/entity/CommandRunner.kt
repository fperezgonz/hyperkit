package solutions.sulfura.hyperkit.generators.entity

import org.springframework.boot.CommandLineRunner
import solutions.sulfura.hyperkit.generators.entity.generator.EntityGeneratorService

@Suppress("unused")
class CommandRunner(
    private val entityGeneratorService: EntityGeneratorService,
    private val properties: EntityGeneratorProperties
) :
    CommandLineRunner {

    override fun run(vararg args: String) {
        entityGeneratorService.generateEntities(properties.toEntityGeneratorConfig())
    }
}