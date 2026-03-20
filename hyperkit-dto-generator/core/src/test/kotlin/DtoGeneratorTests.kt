import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import solutions.sulfura.DtoGenerator
import java.io.File
import kotlin.io.path.toPath

const val inputSourcesRelativePath: String = "/test_input_sources/"
val resourcesFolder: File =
    DtoGeneratorTests::class.java.getResource(inputSourcesRelativePath)!!.toURI().toPath().toFile().parentFile
val inputSourcesPath: String = resourcesFolder.path + inputSourcesRelativePath
val outputSourcesPath: String = resourcesFolder.path + "/generated/"
val expectedOutputDir: String = resourcesFolder.path + "/expected_output/"
const val testDtoPackage: String = "solutions.sulfura.hyperkit.dtos"
val testDtoPackageRelativePath: String = "/" + testDtoPackage.replace('.', '/') + "/"

@TestInstance(PER_CLASS)
class DtoGeneratorTests {

    fun assertGeneratedCodeMatchesExpectedOutput(expectedSourcePath: String, generatedSourcePath: String) {

        val expectedSource = File(expectedSourcePath).readText()
        val generatedSource = File(generatedSourcePath).readText()

        Assertions.assertEquals(expectedSource, generatedSource)

    }

    @BeforeAll
    fun setup() {

        DtoGenerator(
            setOf(inputSourcesPath),
            listOf(),
            outputSourcesPath,
            testDtoPackage
        ).generate()

    }

    @Test
    fun basicTypesDtoTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}SourceClassTypesDto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}SourceClassTypesDto.java"
        )

    }

    @Test
    fun genericHierarchyDtoTest() {


        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}generics/inheritance/GenericChildClassWithParameterizedTypeDto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}generics/inheritance/GenericChildClassWithParameterizedTypeDto.java"
        )

    }

    @Test
    fun getterSetterDtoTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}SourceClassGetterSetterDto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}SourceClassGetterSetterDto.java"
        )

    }

    @Test
    fun circularDependenciesDtoTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}circular_dependencies/class_a/SourceClassADto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}circular_dependencies/class_a/SourceClassADto.java"
        )

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}circular_dependencies/class_b/SourceClassBDto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}circular_dependencies/class_b/SourceClassBDto.java"
        )

    }

    @Test
    fun dtoAnnotationIncludedAttributeTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}SourceClassWithIncludedDto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}SourceClassWithIncludedDto.java"
        )

    }

    @Test
    fun singleGenericParamDtoTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}generics/SingleGenericParamSourceClassDto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}generics/SingleGenericParamSourceClassDto.java"
        )

    }

    @Test
    fun upperCasePropertiesTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}SourceUpperCasePropertiesDto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}SourceUpperCasePropertiesDto.java"
        )

    }

    /** Tests that Dto generation works when {@link Dto#includedAnnotations()} that are in the project's classpath but not in the source or the plugin's classpath */
    @Test
    fun includedAnnotationNotInSourcesTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}DtoAnnotationIncludedClassNotInSourcesDto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}DtoAnnotationIncludedClassNotInSourcesDto.java"
        )

    }

    @Test
    fun referenceToNestedClassTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}SourceClassWithReferenceToNestedClassDto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}SourceClassWithReferenceToNestedClassDto.java"
        )

    }

    @Test
    fun referencesToClassesWithSameNameTest() {

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}SourceClassReferencesClassesWithSameNameDto.java",
            "${outputSourcesPath}${testDtoPackageRelativePath}SourceClassReferencesClassesWithSameNameDto.java"
        )

    }


}