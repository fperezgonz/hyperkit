package solutions.sulfura

import org.apache.velocity.Template
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import solutions.sulfura.processor.utils.*
import spoon.Launcher
import spoon.SpoonAPI
import spoon.compiler.Environment
import spoon.reflect.declaration.CtClass
import java.io.File
import java.time.Instant

private val logger = LoggerFactory.getLogger(DtoGeneratorWorkAction::class.java)

interface DtoGeneratorParameters : WorkParameters {

    /** Absolute paths of the input sources to be processed (files or folders) */
    val absoluteInputPaths: SetProperty<String>
    /** Classpath files for spoon */
    val spoonSourcesClasspath: SetProperty<String>
    /** Root path for the generated sources */
    val rootOutputPath: Property<String>
    /** Default package where the generated DTOs will be placed*/
    val defaultOutputPackage: Property<String>
    /** Velocity template used for DTO code generation */
    val templatePath: Property<String>

}


abstract class DtoGeneratorWorkAction : WorkAction<DtoGeneratorParameters> {

    override fun execute() {

        if (this.parameters == null) {
            throw Exception("Parameters not set")
        }

        val spoonApi: SpoonAPI = Launcher()

        logger.info(
            "Spoon factory environment: \n" +
                    "- Compliance level: ${spoonApi.factory.environment.complianceLevel}\n" +
                    "- Source classpath: ${spoonApi.factory.environment.sourceClasspath}\n" +
                    "- Log Level: ${spoonApi.factory.environment.level}\n"

        )

        spoonApi.factory.environment.prettyPrintingMode = Environment.PRETTY_PRINTING_MODE.AUTOIMPORT

        logger.info("${Instant.now()} - Setting up input files...")
        logger.debug("absoluteInputPaths: {}", parameters.absoluteInputPaths.get())
        for (path in parameters.absoluteInputPaths.get()) {
            spoonApi.addInputResource(path)
        }
        logger.info("${Instant.now()} - Setting up input files... DONE")

        logger.info("${Instant.now()} - Setting up classPath...")
        logger.debug("classpath: {}", parameters.spoonSourcesClasspath.get())

        spoonApi.environment.sourceClasspath = parameters.spoonSourcesClasspath.get().toTypedArray()

        logger.info("${Instant.now()} - Setting up classPath... DONE")

        logger.info("${Instant.now()} - Building model...")
        val model = spoonApi.buildModel()
        logger.info("# ${Instant.now()} - Building model... DONE")

        logger.info("${Instant.now()} - Collecting classes to process...")
        val classesToProcess = collectClasses(model, spoonApi.factory)
        logger.info("${Instant.now()} - Collecting classes to process... DONE")

        //A map of references from the source class name to the dto class
        //Used on dto properties to replace the source class references with references to the dto classes
        val ctClassByClassName = mutableMapOf<String, CtClass<*>>()

        classesToProcess.list<CtClass<*>>().associateTo(ctClassByClassName) { el ->
            el.qualifiedName to sourceClassToDtoClassReference(
                el,
                spoonApi.factory,
                parameters.defaultOutputPackage.get()
            )
        }

        logger.info("Classes to process: ${classesToProcess.list<CtClass<*>>().map { it.qualifiedName }}")

        logger.info("${Instant.now()} - Generating DTOs...")

        val classTemplate = velocityEngine.getTemplate(parameters.templatePath.get())

        classesToProcess.list<CtClass<*>>().parallelStream().map { ctClass: CtClass<*> ->

            logger.debug("Processing class ${ctClass.qualifiedName}")

            try {

                return@map createDtoSourceFileData(
                    ctClass,
                    spoonApi,
                    parameters.defaultOutputPackage.get(),
                    parameters.rootOutputPath.get(),
                    classTemplate,
                    ctClassByClassName,
                    logger
                )

            } catch (e: Exception) {

                logger.error(
                    "${Instant.now()} - ERROR: Error while processing class ${ctClass.qualifiedName}",
                    e
                )
                throw e
            }
        }.toList().parallelStream().forEach { sourceFileData ->

            try {

                val outFile = File(sourceFileData.outFilePath)

                logger.debug("Writing file ${outFile.absolutePath}")

                outFile.parentFile.mkdirs()
                outFile.writeText(sourceFileData.contents)

            } catch (e: Exception) {

                logger.error(
                    "${Instant.now()} - ERROR: Error creating file ${sourceFileData.outFilePath}",
                    e
                )
                throw e
            }
        }

        logger.info("${Instant.now()} - Generating DTOs... DONE")
    }


    fun createDtoSourceFileData(
        ctClass: CtClass<*>,
        spoon: SpoonAPI,
        defaultOutputPackage: String,
        rootOutputPath: String,
        classTemplate: Template,
        ctClassByClassName: MutableMap<String, CtClass<*>>,
        logger: Logger
    ): SourceFileData {

        val collectedProperties = collectProperties(ctClass.reference, spoon.factory)
        logger.debug("collectedProperties: {}", collectedProperties)
        val collectedAnnotations = collectAnnotations(ctClass, spoon)
        logger.debug("collectedAnnotations: {}", collectedAnnotations)
        val oldDtoClass =
            sourceClassToDtoClassReference(ctClass, spoon.factory, defaultOutputPackage)
        val dtoClassPackage = oldDtoClass.`package`.qualifiedName
        val dtoClassSimpleName = oldDtoClass.simpleName
        val dtoClassQualifiedName = oldDtoClass.qualifiedName

        val dtoClass = buildOutputClass(
            ctClass,
            dtoClassQualifiedName,
            ctClassByClassName,
            collectedAnnotations,
            collectedProperties,
            spoon.factory
        )

        val classSourceCode = generateClassSourceCode(dtoClass, ctClass, classTemplate)
        val outDirPath = "${rootOutputPath}/${dtoClassPackage.replace(".", "/")}/"
        val outFilePath = "$outDirPath/${dtoClassSimpleName}.java"

        return SourceFileData(
            contents = classSourceCode,
            outFilePath = outFilePath
        )

    }

    fun generateClassSourceCode(ctClass: CtClass<*>, dtoCtClass: CtClass<*>, classTemplate: Template): String {
        return buildClassSource(ctClass, dtoCtClass, classTemplate)
    }

    data class SourceFileData(
        val contents: String,
        val outFilePath: String,
    )


}