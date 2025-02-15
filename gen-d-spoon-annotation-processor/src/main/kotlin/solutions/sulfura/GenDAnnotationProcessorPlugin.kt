package solutions.sulfura

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
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

    fun generateClassSourceCode(
        ctClass: CtClass<*>
    ): String {

        return SourceBuilder().buildClassSource(ctClass)

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

                val classesToProcess = collectClasses(model, spoon)
                //A map of collected classes to find out if a class will have generated code or not.
                // This is used to know if references to origin classes have to be turned into references to classes derived from them (Dtos, for example)
                val className__ctClass = classesToProcess.list<CtClass<Any>>().associateBy { it.qualifiedName }

                /**Classes created or updated by this process*/
                val newClasses = mutableSetOf<CtType<*>>()

                classesToProcess.forEach { ctClass: CtClass<*> ->
                    var collectedProperties = collectProperties(ctClass, spoon)
                    val collectedAnnotations = collectAnnotations(ctClass, spoon)
                    val dtoClassPackage = "solutions.sulfura.dtos"
                    val dtoClassSimpleName = ctClass.simpleName + "Dto"
                    val dtoClassQualifiedName = "$dtoClassPackage.$dtoClassSimpleName"
                    val oldDtoClass = spoon.factory.Class().get<CtClass<*>>(dtoClassQualifiedName)

                    val dtoClass = buildOutputClass(
                        spoon,
                        ctClass,
                        dtoClassQualifiedName,
                        className__ctClass,
                        collectedAnnotations,
                        collectedProperties,
                    )
                    val classSourceCode = generateClassSourceCode(dtoClass)
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