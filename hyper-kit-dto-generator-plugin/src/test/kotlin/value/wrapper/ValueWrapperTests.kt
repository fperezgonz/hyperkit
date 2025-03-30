package value.wrapper

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.io.path.toPath

const val testProjectPath: String = "/value_wrapper_project/"
const val outputSourcesPath: String = "src/out/"
const val expectedOutputDir: String = "src/expected_output/"
const val testDtoPackagePath: String = "solutions/sulfura/gend/dtos/"

@TestInstance(PER_CLASS)
class AnnotationProcessorTests {

    val testProjectFolder = this.javaClass.getResource(testProjectPath)!!.toURI().toPath().toFile()

    @BeforeAll
    fun generateDtosForTestProject() {

        val gradleRunner = GradleRunner.create()
        gradleRunner.withProjectDir(testProjectFolder)
            .withArguments(":annotationProcessor")
            .withPluginClasspath()
        val gradleBuild = gradleRunner.build()
//        println(gradleBuild.output)
        val outcome = gradleBuild.task(":annotationProcessor")!!.outcome.name

        if (!"SUCCESS".equals(outcome)) {
            println(gradleBuild.output)
            Assertions.fail<Any>()
        }

    }

    fun assertGeneratedCodeMatchesExpectedOutput(expectedSourcePath: String, generatedSourcePath: String){

        val expectedSource = File(testProjectFolder, expectedSourcePath).readText()
        val generatedSource = File(testProjectFolder, generatedSourcePath).readText()

        Assertions.assertEquals(expectedSource, generatedSource)

    }

    @Test
    fun basicTypesDtoTest(){

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}SourceClassTypesDto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}SourceClassTypesDto.java"
        )

    }

}