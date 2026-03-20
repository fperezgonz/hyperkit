import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.io.path.toPath

const val testProjectPath: String = "/test_project/"
val testProjectFolder: File =
    DtoGeneratorPluginTests::class.java.getResource(testProjectPath)!!.toURI().toPath().toFile()
val generatedSourcesPath: String = testProjectFolder.path + "/src/generated/java/"
val expectedOutputDir: String = testProjectFolder.path + "/src/expected_output/java/"
const val testDtoPackage: String = "solutions.sulfura.hyperkit.dtos"
val testDtoPackageRelativePath: String = testDtoPackage.replace('.', '/') + "/"


@TestInstance(PER_CLASS)
class DtoGeneratorPluginTests {

    fun assertGeneratedCodeMatchesExpectedOutput(expectedSourcePath: String, generatedSourcePath: String) {

        val expectedSource = File(expectedSourcePath).readText()
        val generatedSource = File(generatedSourcePath).readText()

        Assertions.assertEquals(expectedSource, generatedSource)

    }

    @Test
    fun basicTypesDtoTest() {

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

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}${testDtoPackageRelativePath}SourceClassTypesDto.java",
            "${generatedSourcesPath}${testDtoPackageRelativePath}SourceClassTypesDto.java"
        )

    }

}