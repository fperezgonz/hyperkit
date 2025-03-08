package solutions.sulfura

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
    val inputPaths: SetProperty<String>
    val rootOutputPath: Property<String>
}

class GenDAnnotationProcessorPlugin : Plugin<Project> {

    fun generateClassSourceCode(ctClass: CtClass<*>, dtoCtClass: CtClass<*>): String {
        return SourceBuilder().buildClassSource(ctClass, dtoCtClass)
    }

    override fun apply(project: Project) {

        val extension = project.extensions.create("genD", GenDAnnotationProcessorConfigurationExtension::class.java)
        extension.inputPaths.convention(mutableSetOf("src/main/java/"))
        extension.rootOutputPath.convention("src/main/java/")
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
                    el.qualifiedName to sourceClassToDtoClassReference(el, spoon.factory)
                }

                logger.info("Classes to process: ${classesToProcess.list<CtClass<*>>().map { it.qualifiedName }}")
                /**Classes created or updated by this process*/
                val newClasses = mutableSetOf<CtType<*>>()

                classesToProcess.forEach { ctClass: CtClass<*> ->
                    var collectedProperties = collectProperties(ctClass.reference, spoon.factory)
                    val collectedAnnotations = collectAnnotations(ctClass, spoon)
                    val oldDtoClass = sourceClassToDtoClassReference(ctClass, spoon.factory)
                    var dtoClassPackage = oldDtoClass.`package`.qualifiedName
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