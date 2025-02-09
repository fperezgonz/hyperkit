package solutions.sulfura

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import solutions.sulfura.gend.dtos.annotations.Dto
import spoon.Launcher
import spoon.SpoonAPI
import spoon.compiler.Environment
import spoon.reflect.CtModel
import spoon.reflect.declaration.*
import spoon.reflect.visitor.chain.CtQuery

interface GenDAnnotationProcessorConfigurationExtension {
    val inputPaths: SetProperty<String>
    val rootOutputPath: Property<String>
}

class GenDAnnotationProcessorPlugin : Plugin<Project> {

    fun collectClasses(model: CtModel, spoonApi: SpoonAPI): CtQuery {

        val dtoAnnotationCtype = spoonApi.factory.Type().get<Annotation>(Dto::class.java).reference

        val dtoAnnotatedClassesQuery = model.filterChildren { el: CtElement ->
            el is CtClass<*> && el.getAnnotation(dtoAnnotationCtype) != null
        }

        return dtoAnnotatedClassesQuery

    }

    fun collectProperties(ctClass: CtClass<*>, spoonApi: SpoonAPI): CtQuery {

        val dtoPropertiesQuery = ctClass.filterChildren({ el: CtElement -> el is CtField<*> })

        return dtoPropertiesQuery

    }

    fun generateClassSourceCode(
        ctClass: CtClass<*>,
        collectedProperties: CtQuery,
        class_name__ctClass: Map<String, CtClass<Any>>
    ): CtClass<*> {

        collectedProperties
            .forEach { ctField: CtField<Any> ->
                ctClass.factory.createField(
                    ctClass,
                    setOf(ModifierKind.PUBLIC),
                    ctField.type,
                    ctField.simpleName
                )
            }

        return ctClass

    }

    override fun apply(project: Project) {

        val extension = project.extensions.create("genD", GenDAnnotationProcessorConfigurationExtension::class.java)
        extension.inputPaths.convention(mutableSetOf("src/main/java/"))
        extension.rootOutputPath.convention("src/main/java/")
        project.task("annotationProcessor") {

            group = "gen-d"

            doFirst {
                val spoon: SpoonAPI = Launcher()
                for (path in extension.inputPaths.get()) {
                    spoon.addInputResource(project.file(path).absolutePath)
                }

                val model = spoon.buildModel()
                val newClasses = mutableSetOf<CtType<*>>()

                val classesCtQuery = collectClasses(model, spoon)
                //A map of collected classes to find out if a class will have generated code or not.
                // This is used to know if references to origin classes have to be turned into references to classes derived from them (Dtos, for example)
                val className__ctClass = classesCtQuery.list<CtClass<Any>>().associateBy { it.qualifiedName }

                classesCtQuery.forEach { ctClass: CtClass<*> ->
                    val collectedProperties = collectProperties(ctClass, spoon)
                    val dtoClassPackage = "solutions.sulfura.dtos"
                    val dtoClassQualifiedName = dtoClassPackage + "." + ctClass.simpleName + "Dto"
                    val dtoClass = spoon.factory.Class().get<CtClass<*>>(dtoClassQualifiedName)
                    val emptyDtoClass = spoon.factory.createClass(dtoClassQualifiedName)
                    val classSourceCode =
                        generateClassSourceCode(emptyDtoClass, collectedProperties, className__ctClass)
                    //TODO add class and source to the list of generated classes
                    newClasses.add(classSourceCode)
                }

                //TODO create files for all generated classes
                spoon.setSourceOutputDirectory(project.file(extension.rootOutputPath.get()).absolutePath)
                spoon.setOutputFilter { el: CtElement -> el in newClasses }
                spoon.factory.environment.prettyPrintingMode = Environment.PRETTY_PRINTING_MODE.AUTOIMPORT
                spoon.prettyprint()
            }
        }
    }
}