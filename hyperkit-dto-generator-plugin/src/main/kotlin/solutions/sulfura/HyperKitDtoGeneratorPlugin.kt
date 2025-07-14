package solutions.sulfura

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import solutions.sulfura.processor.utils.*
import spoon.Launcher
import spoon.SpoonAPI
import spoon.compiler.Environment
import spoon.reflect.declaration.CtClass
import java.io.File
import java.time.Instant

interface HyperKitDtoGeneratorConfigurationExtension {
    /*Paths of the input sources*/
    val inputPaths: SetProperty<String>

    /**Output path for the generated sources*/
    val rootOutputPath: Property<String>

    /**Default package where the generated DTOs will be placed. Default: solutions.sulfura.hyperkit.dtos*/
    val defaultOutputPackage: Property<String?>

}

@Suppress("unused")
class HyperKitDtoGeneratorPlugin : Plugin<Project> {

    fun generateClassSourceCode(ctClass: CtClass<*>, dtoCtClass: CtClass<*>): String {
        return SourceBuilder().buildClassSource(ctClass, dtoCtClass)
    }

    override fun apply(project: Project) {

        val extension = project.extensions.create("hyperKitDtoGenerator", HyperKitDtoGeneratorConfigurationExtension::class.java)
        extension.inputPaths.convention(mutableSetOf("src/main/java/"))
        extension.rootOutputPath.convention("src/main/java/")
        extension.defaultOutputPackage.convention("solutions.sulfura.hyperkit.dtos")
        project.task("generateDtos") {

            group = "hyperkit"

            doFirst {
                val spoon: SpoonAPI = Launcher()
                spoon.factory.environment.prettyPrintingMode = Environment.PRETTY_PRINTING_MODE.AUTOIMPORT

                logger.info("${Instant.now()} - Setting up input files...")
                for (path in extension.inputPaths.get()) {
                    spoon.addInputResource(project.file(path).absolutePath)
                }
                logger.info("${Instant.now()} - Setting up input files... DONE")

                logger.info("${Instant.now()} - Setting up classPath...")
                val classpath = project.configurations.getByName("compileClasspath").files
                    .map { it.absolutePath }
                    .toTypedArray()

                logger.info("Classpath: $classpath")
                spoon.environment.sourceClasspath = classpath
                logger.info("${Instant.now()} - Setting up classPath... DONE")

                logger.info("${Instant.now()} - Building model...")
                val model = spoon.buildModel()
                logger.info("# ${Instant.now()} - Building model... DONE")
                logger.info("${Instant.now()} - Collecting classes to process...")
                val classesToProcess = collectClasses(model, spoon.factory)
                logger.info("${Instant.now()} - Collecting classes to process... DONE")
                //A map of references from the source class name to the dto class
                //Used on dto properties to replace the source class references with references to the dto classes
                val className__ctClass = mutableMapOf<String, CtClass<*>>()

                classesToProcess.list<CtClass<*>>().associateTo(className__ctClass) { el ->
                    el.qualifiedName to sourceClassToDtoClassReference(
                        el,
                        spoon.factory,
                        extension.defaultOutputPackage.get()
                    )
                }

                logger.info("Classes to process: ${classesToProcess.list<CtClass<*>>().map { it.qualifiedName }}")

                logger.info("${Instant.now()} - Generating DTOs...")

                classesToProcess.list<CtClass<*>>().parallelStream().forEach { ctClass: CtClass<*> ->
                    createDtoSourceFile(
                        ctClass,
                        spoon,
                        extension.defaultOutputPackage.get(),
                        extension.rootOutputPath.get(),
                        className__ctClass,
                        project,
                        logger
                    )
                }

                logger.info("${Instant.now()} - Generating DTOs... DONE")

            }
        }
    }

    fun createDtoSourceFile(
        ctClass: CtClass<*>,
        spoon: SpoonAPI,
        defaultOutputPackage: String,
        rootOutputPath: String,
        className__ctClass: MutableMap<String, CtClass<*>>,
        project: Project,
        logger: Logger
    ) {

        try {

            val collectedProperties = collectProperties(ctClass.reference, spoon.factory)
            val collectedAnnotations = collectAnnotations(ctClass, spoon)
            val oldDtoClass =
                sourceClassToDtoClassReference(ctClass, spoon.factory, defaultOutputPackage)
            val dtoClassPackage = oldDtoClass.`package`.qualifiedName
            val dtoClassSimpleName = oldDtoClass.simpleName
            val dtoClassQualifiedName = oldDtoClass.qualifiedName

            val dtoClass = buildOutputClass(
                ctClass,
                dtoClassQualifiedName,
                className__ctClass,
                collectedAnnotations,
                collectedProperties,
                spoon.factory
            )

            val classSourceCode = generateClassSourceCode(dtoClass, ctClass)
            val outDirPath = "${rootOutputPath}/${dtoClassPackage.replace(".", "/")}/"
            val outFilePath = "$outDirPath/${dtoClassSimpleName}.java"
            val outFile = File(project.file(outFilePath).absolutePath)

            outFile.parentFile.mkdirs()
            outFile.writeText(classSourceCode)

        } catch (e: Exception) {
            logger.error(
                "${Instant.now()} - ERROR: Error while processing class ${ctClass.qualifiedName}",
                e
            )
            throw e
        }

    }

}