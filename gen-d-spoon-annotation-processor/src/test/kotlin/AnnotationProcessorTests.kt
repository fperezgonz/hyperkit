import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.io.File
import kotlin.io.path.toPath

const val testProjectPath: String = "/test_project/"
const val testInputSourcesPath: String = "/src/test_input_sources/"
const val outputSourcesPath: String = "/src/out/"
const val buildFileSampleName: String = "build.gradle.kts.sample"
const val expectedOutputDir: String = "/src/expected_output/"
const val testDtoPackagePath: String = "solutions/sulfura/gend/dtos/"

@TestInstance(PER_CLASS)
class AnnotationProcessorTests {

    val testProjectFolder = this.javaClass.getResource(testProjectPath)!!.toURI().toPath().toFile()

    @BeforeAll
    fun generateDtosForTestProject() {

        val buildFileContent = File(testProjectFolder, buildFileSampleName).readText()
            .replace("<<input_paths>>", '"' + testInputSourcesPath + '"')
        File(testProjectFolder, "build.gradle.kts").writeText(buildFileContent)

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
        val generatedSource = File(testProjectFolder, generatedSourcePath).readText();

        Assertions.assertEquals(expectedSource, generatedSource)

    }

    @Test
    fun basicTypesDtoTest(){

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
            "${expectedOutputDir}java/${testDtoPackagePath}circular_dependencies/SourceClassADto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}circular_dependencies/SourceClassADto.java"
        )

        assertGeneratedCodeMatchesExpectedOutput(
            "${expectedOutputDir}java/${testDtoPackagePath}circular_dependencies/SourceClassBDto.java",
            "${outputSourcesPath}java/${testDtoPackagePath}circular_dependencies/SourceClassBDto.java"
        )

    }
//
//    @Test
//    @Throws(MalformedURLException::class, ClassNotFoundException::class, URISyntaxException::class)
//    fun generateDtosWithCircularDependencies() {
//        val qualifiedClassNameA = "solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA"
//        val qualifiedClassNameB = "solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB"
//        compileClassesWithProcessor(
//            Paths.get(
//                this.javaClass.getResource(testInputResourcesPath + "circular_dependencies/SourceClassA.java").toURI()
//            ).toString(),
//            Paths.get(
//                this.javaClass.getResource(testInputResourcesPath + "circular_dependencies/SourceClassB.java").toURI()
//            ).toString()
//        )
//        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassNameA)
//        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassNameB)
//    }

//
//    @Test
//    @Throws(MalformedURLException::class, ClassNotFoundException::class, URISyntaxException::class)
//    fun generateDtoWithUppercasePropertiesTest() {
//        val qualifiedClassName = "solutions.sulfura.gend.dtos.SourceUpperCaseProperties"
//
//        val compiledClass = compileClassWithProcessor(
//            Paths.get(this.javaClass.getResource(testInputResourcesPath + "SourceUpperCaseProperties.java").toURI())
//                .toString(),
//            qualifiedClassName
//        )
//        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName)
//    }
//
//    @Test
//    @Throws(MalformedURLException::class, ClassNotFoundException::class, URISyntaxException::class)
//    fun generateDtoWithIncludedTest() {
//        val qualifiedClassName = "solutions.sulfura.gend.dtos.SourceClassWithIncluded"
//
//        val compiledClass = compileClassWithProcessor(
//            Paths.get(this.javaClass.getResource(testInputResourcesPath + "SourceClassWithIncluded.java").toURI())
//                .toString(),
//            qualifiedClassName
//        )
//        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName)
//    }
//
//    @Test
//    @Throws(MalformedURLException::class, ClassNotFoundException::class, URISyntaxException::class)
//    fun generateDtoWithGenericTypes() {
//        val qualifiedClassName = "solutions.sulfura.gend.dtos.generics.SingleGenericParamSourceClass"
//
//        val compiledClass = compileClassWithProcessor(
//            Paths.get(
//                this.javaClass.getResource(testInputResourcesPath + "generics/SingleGenericParamSourceClass.java")
//                    .toURI()
//            ).toString(),
//            qualifiedClassName
//        )
//        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName)
//    }
//
//    @Test
//    @Throws(ClassNotFoundException::class, MalformedURLException::class, URISyntaxException::class)
//    fun generateChildDtoWithParameterizedType() {
//        val qualifiedClassName =
//            "solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedType"
//
//        val compiledClass = compileClassWithProcessor(
//            Paths.get(
//                this.javaClass.getResource(testInputResourcesPath + "generics/inheritance/GenericChildClassWithParameterizedType.java")
//                    .toURI()
//            ).toString(),
//            qualifiedClassName
//        )
//        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName)
//    }
//
//    @Throws(MalformedURLException::class, ClassNotFoundException::class)
//    fun compileClassWithProcessor(filePath: String, className: String?): Class<*> {
//        compileClassesWithProcessor(filePath)
//        return loadCompiledClass(className)
//    }
//
//    @Throws(MalformedURLException::class, ClassNotFoundException::class)
//    fun compileClassesWithProcessor(vararg filePaths: String?) {
//        val args: MutableList<String?> = ArrayList<String?>()
//        //Compiler options
//        args.add("-d")
//        args.add(annotationProcessorOutputDir)
//        args.add("-processor")
//        args.add(DtoAnnotationProcessor::class.java.getCanonicalName())
//        //Files to compile
//        args.addAll(Arrays.asList<String?>(*filePaths))
//
//        val compilerResult = ToolProvider.getSystemJavaCompiler().run(
//            System.`in`, System.out, System.err,
//            *args.toTypedArray<String?>()
//        )
//
//        if (compilerResult != 0) {
//            throw RuntimeException(
//                "Could not compile classes at paths: " + Arrays.stream<String?>(filePaths)
//                    .reduce("") { str1: String?, str2: String? -> str1 + ", " + str2 })
//        }
//    }
//
//    fun assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName: String) {
//        val outputPathString = annotationProcessorOutputDir + "/" + qualifiedClassName.replace('.', '/') + "Dto.java"
//        val expectedOutputPathString = expectedOutputDir + "/" + qualifiedClassName.replace('.', '/') + "Dto.java"
//        val expectedOutputPath = File(expectedOutputPathString).toPath()
//        val outputPath = File(outputPathString).toPath()
//        try {
//            val expectedOutput = String(Files.readAllBytes(expectedOutputPath))
//            val output = String(Files.readAllBytes(outputPath))
//            Assertions.assertEquals(expectedOutput, output)
//        } catch (e: IOException) {
//            throw RuntimeException(e)
//        }
//    }
//
//    @Throws(MalformedURLException::class, ClassNotFoundException::class)
//    fun loadCompiledClass(className: String?): Class<*> {
//        val classLoaderUrls = arrayOf<URL?>(File(annotationProcessorOutputDir).toURI().toURL())
//        val classLoader: ClassLoader = URLClassLoader(classLoaderUrls)
//
//        return Class.forName(className, true, classLoader)
//    }

}