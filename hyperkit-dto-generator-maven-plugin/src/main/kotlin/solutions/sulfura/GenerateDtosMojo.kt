package solutions.sulfura

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.plugins.annotations.ResolutionScope
import org.apache.maven.project.MavenProject
import java.io.File

@Mojo(
    name = "generate-dtos",
    defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
class GenerateDtosMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${project}", readonly = true, required = true)
    lateinit var project: MavenProject

    @Parameter
    var inputPaths: Set<String>? = null

    @Parameter(defaultValue = "\${project.build.directory}/generated-sources/hyperkit")
    lateinit var rootOutputPath: String

    @Parameter(defaultValue = "solutions.sulfura.hyperkit.dtos")
    lateinit var defaultOutputPackage: String

    @Parameter(defaultValue = "templates/dto.vm")
    lateinit var templatePath: String

    override fun execute() {
        val resolvedInputPaths = if (inputPaths.isNullOrEmpty()) {
            @Suppress("UNCHECKED_CAST")
            (project.compileSourceRoots as List<String>).toSet()
        } else {
            inputPaths!!
        }

        val classpathElements = project.compileClasspathElements.map { File(it as String).absolutePath }

        val generator = DtoGenerator(
            absoluteInputPaths = resolvedInputPaths,
            spoonSourcesClasspath = classpathElements,
            rootOutputPath = rootOutputPath,
            defaultOutputPackage = defaultOutputPackage,
            templatePath = templatePath
        )

        generator.generate()
        
        project.addCompileSourceRoot(rootOutputPath)
    }
}
