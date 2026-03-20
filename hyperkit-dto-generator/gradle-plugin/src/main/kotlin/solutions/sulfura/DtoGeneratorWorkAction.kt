package solutions.sulfura

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(DtoGeneratorWorkAction::class.java)

interface DtoGeneratorParameters : WorkParameters {

    /** Absolute paths of the input sources to be processed (files or folders) */
    val absoluteInputPaths: SetProperty<String>
    /** Classpath files for spoon */
    val spoonSourcesClasspath: ListProperty<String>
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

        val generator = DtoGenerator(
            absoluteInputPaths = parameters.absoluteInputPaths.get(),
            spoonSourcesClasspath = parameters.spoonSourcesClasspath.get(),
            rootOutputPath = parameters.rootOutputPath.get(),
            defaultOutputPackage = parameters.defaultOutputPackage.get(),
            templatePath = parameters.templatePath.get()
        )

        generator.generate()
    }

}