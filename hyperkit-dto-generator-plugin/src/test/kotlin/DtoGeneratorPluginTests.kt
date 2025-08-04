import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.io.path.toPath

const val testProjectPath: String = "/test_project/"
const val outputSourcesPath: String = "src/out/"
const val expectedOutputDir: String = "src/expected_output/"
const val testDtoPackagePath: String = "solutions/sulfura/hyperkit/dtos/"

@TestInstance(PER_CLASS)
class DtoGeneratorPluginTests {

    val testProjectFolder: File = this.javaClass.getResource(testProjectPath)!!.toURI().toPath().toFile()

    @BeforeAll
    fun generateDtosForTestProject() {

        val gradleRunner = GradleRunner.create()
        gradleRunner.withProjectDir(testProjectFolder)
            .withArguments(":clean", ":generateDtos", "--info")
            .withPluginClasspath()
        val gradleBuild = gradleRunner.build()
        val outcome = gradleBuild.task(":generateDtos")!!.outcome.name

//        println(gradleBuild.output)

        if ("SUCCESS" != outcome) {
            println(gradleBuild.output)
            Assertions.fail<Any>()
        }

    }

    fun assertGeneratedCodeMatchesExpectedOutput(expectedSourcePath: String, generatedSourcePath: String) {

        val expectedSource = File(testProjectFolder, expectedSourcePath).readText()
        val generatedSource = File(testProjectFolder, generatedSourcePath).readText()

        Assertions.assertEquals(expectedSource, generatedSource)

    }

    @Test
    fun basicTypesDtoTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}SourceClassTypesDto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}SourceClassTypesDto.java"
        )

    }

    @Test
    fun genericHierarchyDtoTest() {


        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}generics/inheritance/GenericChildClassWithParameterizedTypeDto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}generics/inheritance/GenericChildClassWithParameterizedTypeDto.java"
        )

    }

    @Test
    fun getterSetterDtoTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}SourceClassGetterSetterDto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}SourceClassGetterSetterDto.java"
        )

    }

    @Test
    fun circularDependenciesDtoTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}circular_dependencies/class_a/SourceClassADto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}circular_dependencies/class_a/SourceClassADto.java"
        )

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}circular_dependencies/class_b/SourceClassBDto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}circular_dependencies/class_b/SourceClassBDto.java"
        )

    }

    @Test
    fun dtoAnnotationIncludedAttributeTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}SourceClassWithIncludedDto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}SourceClassWithIncludedDto.java"
        )

    }

    @Test
    fun singleGenericParamDtoTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}generics/SingleGenericParamSourceClassDto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}generics/SingleGenericParamSourceClassDto.java"
        )

    }

    @Test
    fun upperCasePropertiesTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}SourceUpperCasePropertiesDto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}SourceUpperCasePropertiesDto.java"
        )

    }

    /** Tests that Dto generation works when {@link Dto#includedAnnotations()} that are in the project's classpath but not in the source or the plugin's classpath */
    @Test
    fun includedAnnotationNotInSourcesTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}DtoAnnotationIncludedClassNotInSourcesDto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}DtoAnnotationIncludedClassNotInSourcesDto.java"
        )

    }

}