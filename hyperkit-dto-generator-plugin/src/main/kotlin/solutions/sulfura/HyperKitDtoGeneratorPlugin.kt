package solutions.sulfura

import org.apache.velocity.Template
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.register
import spoon.reflect.declaration.CtClass

interface HyperKitDtoGeneratorConfigurationExtension {
    /**Paths of the input sources*/
    val inputPaths: SetProperty<String>

    /** Output path for the generated sources*/
    val rootOutputPath: Property<String>

    /** Default package where the generated DTOs will be placed. Default: solutions.sulfura.hyperkit.dtos*/
    val defaultOutputPackage: Property<String?>

    /** Velocity template used for DTO code generation */
    val templatePath: Property<String?>

}

@Suppress("unused")
class HyperKitDtoGeneratorPlugin : Plugin<Project> {

    fun generateClassSourceCode(ctClass: CtClass<*>, dtoCtClass: CtClass<*>, classTemplate: Template): String {
        return buildClassSource(ctClass, dtoCtClass, classTemplate)
    }

    override fun apply(project: Project) {

        val extension = project.extensions.create(
            "hyperKitDtoGenerator",
            HyperKitDtoGeneratorConfigurationExtension::class.java
        )
        val sourceSets = project.extensions.getByType(org.gradle.api.tasks.SourceSetContainer::class.java)
        extension.inputPaths.convention(
            sourceSets.flatMap { it.allJava.sourceDirectories }
                .map { it.absolutePath })
        extension.rootOutputPath.convention("src/main/java/")
        extension.defaultOutputPackage.convention("solutions.sulfura.hyperkit.dtos")
        extension.templatePath.convention("templates/dto.vm")

        val templatePathAux = project.findProperty("hyperkit.dtoGenerator.templatePath")?.toString()

        if (templatePathAux != null) {

            val templateFile = project.projectDir.resolve(templatePathAux)

            val resolvedTemplatePath =
                if (templateFile.exists())
                    templateFile.absolutePath
                else
                    templatePathAux

            extension.templatePath.set(resolvedTemplatePath)

        }

        project.tasks.register<GenerateDtosTask>("generateDtos") {

            group = "hyperkit"
            description = "Generate DTOs from source classes"
            templatePath.set(extension.templatePath)
            rootOutputPath.set(project.file(extension.rootOutputPath).absolutePath)
            defaultOutputPackage.set(extension.defaultOutputPackage)
            inputFiles.setFrom(extension.inputPaths)

            // Add the project classpath to Spoon's analysis classpath
            project.plugins.withType(org.gradle.api.plugins.JavaBasePlugin::class.java) {
                val sourceSets = project.extensions.getByType(org.gradle.api.tasks.SourceSetContainer::class.java)
                sourceSets.findByName(org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME)?.let { mainSourceSet ->
                    spoonSourcesClasspath.from(mainSourceSet.compileClasspath)
                }
            }

        }
    }

}