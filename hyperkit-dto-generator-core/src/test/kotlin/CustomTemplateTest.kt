import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import solutions.sulfura.DtoGenerator
import java.io.File

class CustomTemplateTest {
    /**
     * Tests that the plugin can use a custom template for DTO generation.
     * This test verifies that when a custom template path is provided,
     * the plugin uses that template instead of the default one.
     */
    @Test
    fun customTemplateTest() {
        // Given: A test project directory with a custom template
        val outputDir = File( "${outputSourcesPath}${testDtoPackageRelativePath}")

        DtoGenerator(
            setOf(inputSourcesPath),
            listOf(),
            outputSourcesPath,
            testDtoPackage,
            "templates/test-template.vm"
        ).generate()

        // Then: The generated file should contain the content of the custom template
        val generatedFile = File(outputDir, "SourceClassTypesDto.java")
        Assertions.assertTrue(generatedFile.exists(), "Generated file ${generatedFile.path} does not exist")
        Assertions.assertEquals(
            "test-template", generatedFile.readText(),
            "Generated file does not contain the expected content from the custom template"
        )
    }
}
