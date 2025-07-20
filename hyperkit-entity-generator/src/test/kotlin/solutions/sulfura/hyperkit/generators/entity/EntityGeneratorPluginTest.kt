package solutions.sulfura.hyperkit.generators.entity

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.io.path.toPath

/**
 * This test verifies that the plugin generates entity files as expected from the database schema
 */
class EntityGeneratorPluginTest {

    val testProjectPath: String = "/test_project/"

    fun assertFileContentIsAsExpected(expectedFile: File, generatedFile: File) {
        val expectedFileContent = expectedFile.readText()
        val generatedFileContent = generatedFile.readText()
        Assertions.assertEquals(expectedFileContent, generatedFileContent)
    }

    @Test
    @DisplayName("Plugin should generate entity classes")
    fun pluginShouldGenerateEntityClasses() {

        // Given: A test project directory
        val testProjectFolder: File = this.javaClass.getResource(testProjectPath)!!.toURI().toPath().toFile()
        val basePackagePath = "solutions/sulfura/hyperkit/test/entities".replace('/', File.separatorChar)
        // A directory containing the expected output of the plugin execution
        val expectedOutputDir = File(testProjectFolder, "src/expected_output/java/$basePackagePath")
        val outputDir = File(testProjectFolder, "src/main/java/$basePackagePath")
        // Make sure the output directory is clean to avoid interferences with other tests
        outputDir.deleteRecursively()

        // When: The plugin is executed
        @Suppress("UnusedVariable", "unused")
        val gradleRunner = GradleRunner.create()
            .withProjectDir(testProjectFolder)
            .withPluginClasspath()
            .withArguments(
                "initDatabase",
                "generateEntities"
            )
            .build()

        // Uncomment to log the task output
//        println(gradleRunner.output)

        // Then: Entity classes should be generated as expected

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

    @Test
    @DisplayName("Plugin should generate entity classes with custom template")
    fun pluginShouldGenerateEntityClassesWithCustomTemplate() {

        // Given: A project directory for test
        val testProjectFolder: File = this.javaClass.getResource(testProjectPath)!!.toURI().toPath().toFile()
        // A path where the entity files will be generated
        val outputDir = File(testProjectFolder, "src/main/java/solutions/sulfura/hyperkit/test/entities")
        // Make sure the output directory is clean to avoid interferences with other tests
        outputDir.deleteRecursively()


        // When: The plugin is executed
        @Suppress("UnusedVariable", "unused")
        val gradleRunner = GradleRunner.create()
            .withProjectDir(testProjectFolder)
            .withPluginClasspath()
            .withArguments(
                "initDatabase",
                "generateEntities",
                // Specify a custom template
                "-Phyperkit.entityGenerator.templatePath=test-template.vm"
            )
            .build()

        // Uncomment to log the task output
//        println(gradleRunner.output)

        // Then: Entity classes should be generated with the custom template
        Assertions.assertEquals("test-template", outputDir.resolve("AppUser.java").readText())
    }
}