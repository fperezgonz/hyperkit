package solutions.sulfura.hyperkit.generators.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import solutions.sulfura.hyperkit.generators.entity.generator.EntityGeneratorService
import java.io.File
import java.nio.file.Path
import kotlin.io.path.pathString
import kotlin.io.path.toPath

/**
 * Integration test for the entity generator.
 * This test verifies that the entity generator correctly generates entity classes
 * from a database schema.
 */
@SpringBootTest
class EntityGeneratorIntegrationTest {

    @Autowired
    private lateinit var entityGeneratorService: EntityGeneratorService

    @Autowired
    private lateinit var properties: EntityGeneratorProperties

    private fun assertFileContentIsAsExpected(expectedFile: File, generatedFile: File) {

        val expectedFileContent = expectedFile.readText()
        val generatedFileContent = generatedFile.readText()
        Assertions.assertEquals(expectedFileContent, generatedFileContent)

    }

    @Test
    @DisplayName("Should generate entity classes from H2 database schema")
    fun shouldGenerateEntityClassesFromH2Database(@TempDir tempDir: Path) {
        // Given: A test project directory
        val testProjectFolder: File = this.javaClass.getResource("/test_project/")!!.toURI().toPath().toFile()
        val basePackagePath = "solutions/sulfura/hyperkit/test/entities"
        // A directory containing the expected output of the plugin execution
        val expectedOutputDir = File(testProjectFolder, "src/expected_output/java/$basePackagePath")

        // Given: A temporary directory for output
        properties.outputPath = tempDir.pathString + "/src/main/java/"
        val outputDir = File("${properties.outputPath}$basePackagePath")

        // When: The entity generator is run
        val generatedEntities = entityGeneratorService.generateEntityFiles(properties.toEntityGeneratorConfig())

        // Then: Entity classes should be generated
        assertThat(generatedEntities.size).isGreaterThan(0)

        // User
        assertFileContentIsAsExpected(expectedOutputDir.resolve("AppUser.java"), outputDir.resolve("AppUser.java"))
        // Product
        assertFileContentIsAsExpected(expectedOutputDir.resolve("Product.java"), outputDir.resolve("Product.java"))
        // Orders
        assertFileContentIsAsExpected(expectedOutputDir.resolve("Orders.java"), outputDir.resolve("Orders.java"))
        // OrderItem
        assertFileContentIsAsExpected(expectedOutputDir.resolve("OrderItem.java"), outputDir.resolve("OrderItem.java"))
        // Category
        assertFileContentIsAsExpected(expectedOutputDir.resolve("Category.java"), outputDir.resolve("Category.java"))
        // ProductCategory
        assertFileContentIsAsExpected(
            expectedOutputDir.resolve("ProductCategory.java"),
            outputDir.resolve("ProductCategory.java")
        )
    }
}
