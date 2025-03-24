package solutions.sulfura

import io.vavr.control.Option
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import solutions.sulfura.processor.utils.buildOutputClass
import solutions.sulfura.processor.utils.collectAnnotations
import solutions.sulfura.processor.utils.collectClasses
import solutions.sulfura.processor.utils.collectProperties
import solutions.sulfura.processor.utils.sourceClassToDtoClassReference
import spoon.Launcher
import spoon.SpoonAPI
import spoon.compiler.Environment
import spoon.reflect.declaration.CtClass
import spoon.reflect.declaration.CtType
import java.io.File

interface GenDAnnotationProcessorConfigurationExtension {
    /*Paths of the input sources*/
    val inputPaths: SetProperty<String>

    /**Output path for the generated sources*/
    val rootOutputPath: Property<String>

    /**Canonical name of the type that will be used as a wrapper for the dto fields. Default: io.vavr.control.Option*/
    val valueWrapperType: Property<String?>

    /**The default value for fields that use the value wrapper. Default: Option.none()*/
    val valueWrapperDefaultValue: Property<String?>

    /**Default package where the generated DTOs will be placed. Default: solutions.sulfura.gend.dtos*/
    val defaultOutputPackage: Property<String?>

}

@Suppress("unused")
class GenDAnnotationProcessorPlugin : Plugin<Project> {

    fun generateClassSourceCode(ctClass: CtClass<*>, dtoCtClass: CtClass<*>, valueWrapperDefaultvalue: String): String {
        return SourceBuilder().buildClassSource(ctClass, dtoCtClass, valueWrapperDefaultvalue)
    }

    override fun apply(project: Project) {

        val extension = project.extensions.create("genD", GenDAnnotationProcessorConfigurationExtension::class.java)
        extension.inputPaths.convention(mutableSetOf("src/main/java/"))
        extension.rootOutputPath.convention("src/main/java/")
        extension.valueWrapperType.convention(Option::class.java.canonicalName)
        extension.valueWrapperDefaultValue.convention("Option.none()")
        extension.defaultOutputPackage.convention("solutions.sulfura.gend.dtos")
        project.task("annotationProcessor") {

            group = "gen-d"

            doFirst {
                val spoon: SpoonAPI = Launcher()
                spoon.factory.environment.prettyPrintingMode = Environment.PRETTY_PRINTING_MODE.AUTOIMPORT

                //Setup the input files
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
                /**Classes created or updated by this process*/
                val newClasses = mutableSetOf<CtType<*>>()
                val valueWrapperTypeCanonicalName = extension.valueWrapperType.get()
                var valueWrapperType = spoon.factory.Type().get<Any>(valueWrapperTypeCanonicalName)
                if (valueWrapperType == null) {
                    valueWrapperType = spoon.factory.Class().create<Any>(valueWrapperTypeCanonicalName)
                }
                var valueWrapperDefaultValue = extension.valueWrapperDefaultValue.get()

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
                    //TODO add class and source to the list of generated classes
//                    newClasses.add(classSourceCode)
                }

                //TODO create files for all generated classes
//                spoon.setSourceOutputDirectory(project.file(extension.rootOutputPath.get()).absolutePath)
//                spoon.setOutputFilter { el: CtElement -> el in newClasses }
//                spoon.prettyprint()
            }
        }
    }
}