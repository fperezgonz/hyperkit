import org.gradle.api.Plugin
import org.gradle.api.Project
import spoon.Launcher
import spoon.SpoonAPI
import spoon.compiler.Environment
import spoon.reflect.declaration.*

class GenDAnnotationProcessorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("runSpoon") {

            val spoon: SpoonAPI = Launcher()
            spoon.addInputResource(project.files("src/main/java/").asPath)

            val model = spoon.buildModel()
            val newClasses = mutableSetOf<CtType<*>>()

            model.filterChildren { el: CtElement ->
                el is CtClass<*>
                        && el.simpleName == "Main"
                        && el.getAnnotation(
                    spoon.factory.Type().get<Annotation>("solutions.sulfura.PotatoClass").reference
                ) != null
            }
                .forEach { ctClass: CtClass<*> ->
                    spoon.factory.Class().get<Any>("solutions.sulfura.PotatoClass").position.file.delete()

                    val newClass = ctClass.factory.createClass("solutions.sulfura.PotatoClass")
                    newClasses.add(newClass)
                    ctClass.filterChildren({ el: CtElement -> el is CtField<*> })
                        .forEach { ctField: CtField<Any> ->
                            ctClass.factory.createField(
                                newClass,
                                setOf(ModifierKind.PUBLIC),
                                ctField.type,
                                ctField.simpleName
                            )
                            println(ctField.simpleName)

                        }
                    println(ctClass.qualifiedName)
                }

            spoon.setSourceOutputDirectory(project.files("src/main/java/").asPath)
            spoon.setOutputFilter { el: CtElement -> el in newClasses }
            spoon.factory.environment.prettyPrintingMode = Environment.PRETTY_PRINTING_MODE.AUTOIMPORT
            spoon.prettyprint()
            project.task("hello") {
                doLast {
                    println("Hello from the GenDAnnotationProcessorPlugin")
                }
            }
        }
    }
}