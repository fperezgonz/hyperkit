import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.io.path.toPath

@TestInstance(PER_CLASS)
class DtoGeneratorPluginCustomTemplateTest {

    val testDtoPackagePath: String = "solutions/sulfura/hyperkit/dtos/"
    val testProjectFolder: File = this.javaClass.getResource(testProjectPath)!!.toURI().toPath().toFile()

    /**
     * Tests that the plugin can use a custom template for DTO generation.
     * This test verifies that when a custom template path is provided,
     * the plugin uses that template instead of the default one.
     */
    @Test
    fun customTemplateTest() {
        // Given: A test project directory with a custom template
        val outputDir = File(testProjectFolder, "${outputSourcesPath}java/${testDtoPackagePath}")
        
        // Clean the output directory to avoid interference with previous tests
        outputDir.deleteRecursively()
        outputDir.mkdirs()

        // When: The plugin is executed with a custom template path
        val gradleRunner = GradleRunner.create()
        gradleRunner.withProjectDir(testProjectFolder)
            .withArguments(":clean", ":generateDtos", "-Phyperkit.dtoGenerator.templatePath=test-template.vm", "--info")
            .withPluginClasspath()
        val gradleBuild = gradleRunner.build()
        val outcome = gradleBuild.task(":generateDtos")!!.outcome.name

        if ("SUCCESS" != outcome) {
            println(gradleBuild.output)
            Assertions.fail<Any>("Failed to generate DTOs with custom template")
        }

        // Then: The generated file should contain the content of the custom template
        val generatedFile = File(outputDir, "SourceClassTypesDto.java")
        Assertions.assertTrue(generatedFile.exists(), "Generated file does not exist")
        Assertions.assertEquals("test-template", generatedFile.readText(), 
            "Generated file does not contain the expected content from the custom template")
    }

}