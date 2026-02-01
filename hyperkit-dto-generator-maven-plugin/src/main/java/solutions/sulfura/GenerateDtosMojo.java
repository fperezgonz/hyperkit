package solutions.sulfura;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mojo(
        name = "hyperkit-dto-generator-maven-plugin",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresDependencyResolution = ResolutionScope.COMPILE
)
public class GenerateDtosMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    public MavenProject project;

    @Parameter
    Set<String> inputPaths = null;

    @Parameter(defaultValue = "src/main/java/")
    public String rootOutputPath;

    @Parameter(defaultValue = "solutions.sulfura.hyperkit.dtos")
    public String defaultOutputPackage;

    @Parameter(defaultValue = "templates/dto.vm")
    public String templatePath;

    @Override
    public void execute() {

        var resolvedInputPaths = inputPaths == null || inputPaths.isEmpty() ?
                new HashSet<>(project.getCompileSourceRoots()) :
                inputPaths;

        List<String> classpathElements = null;
            classpathElements = project.getArtifacts().stream()
                    .map( it-> {
                        System.out.println(it.getArtifactId());
                        return it.getFile().getAbsolutePath();})
                    .collect(Collectors.toList());

        getLog().info("project.basedir=" + project.getBasedir());
        getLog().info("rootOutputPath=" + rootOutputPath);

        var generator = new DtoGenerator(resolvedInputPaths,
                classpathElements,
                rootOutputPath,
                defaultOutputPackage,
                templatePath
        );

        generator.generate();

        project.addCompileSourceRoot(rootOutputPath);
    }
}