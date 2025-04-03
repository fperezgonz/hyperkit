package solutions.sulfura

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import solutions.sulfura.hyperkit.dtos.ValueWrapper
import solutions.sulfura.processor.utils.*
import spoon.Launcher
import spoon.SpoonAPI
import spoon.compiler.Environment
import spoon.reflect.declaration.CtClass
import java.io.File

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

    fun generateClassSourceCode(ctClass: CtClass<*>, dtoCtClass: CtClass<*>, valueWrapperDefaultvalue: String): String {
        return SourceBuilder().buildClassSource(ctClass, dtoCtClass, valueWrapperDefaultvalue)
    }

    override fun apply(project: Project) {

        val extension = project.extensions.create("hyperKit", HyperKitDtoGeneratorConfigurationExtension::class.java)
        extension.inputPaths.convention(mutableSetOf("src/main/java/"))
        extension.rootOutputPath.convention("src/main/java/")
        extension.defaultOutputPackage.convention("solutions.sulfura.hyperkit.dtos")
        project.task("generateDtos") {

            group = "hyperkit"

            doFirst {
                val spoon: SpoonAPI = Launcher()
                spoon.factory.environment.prettyPrintingMode = Environment.PRETTY_PRINTING_MODE.AUTOIMPORT

                //Set up the input files
                for (path in extension.inputPaths.get()) {
                    spoon.addInputResource(project.file(path).absolutePath)
                }

                val model = spoon.buildModel()

                val classesToProcess = collectClasses(model, spoon.factory)
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
                val valueWrapperType = spoon.factory.Class().create<ValueWrapper<Any>>(ValueWrapper::class.java.canonicalName)
                var valueWrapperDefaultValue = "ValueWrapper.empty()"

                classesToProcess.forEach { ctClass: CtClass<*> ->
                    var collectedProperties = collectProperties(ctClass.reference, spoon.factory)
                    val collectedAnnotations = collectAnnotations(ctClass, spoon)
                    val oldDtoClass =
                        sourceClassToDtoClassReference(ctClass, spoon.factory, extension.defaultOutputPackage.get())
                    var dtoClassPackage = oldDtoClass.`package`.qualifiedName
                    val dtoClassSimpleName = oldDtoClass.simpleName
                    val dtoClassQualifiedName = oldDtoClass.qualifiedName

                    val dtoClass = buildOutputClass(
                        ctClass,
                        dtoClassQualifiedName,
                        className__ctClass,
                        collectedAnnotations,
                        collectedProperties,
                        valueWrapperType,
                        spoon.factory
                    )

                    val classSourceCode = generateClassSourceCode(dtoClass, ctClass, valueWrapperDefaultValue)
                    val outDirPath = "${extension.rootOutputPath.get()}/${dtoClassPackage.replace(".", "/")}/"
                    val outFilePath = "$outDirPath/${dtoClassSimpleName}.java"
                    val outFile = File(project.file(outFilePath).absolutePath)
                    outFile.parentFile.mkdirs()
                    outFile.writeText(classSourceCode)

                }
            }
        }
    }
}