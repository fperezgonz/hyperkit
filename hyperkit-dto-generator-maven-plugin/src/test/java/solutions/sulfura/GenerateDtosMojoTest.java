package solutions.sulfura;

import org.apache.maven.api.plugin.testing.Basedir;
import org.apache.maven.api.plugin.testing.InjectMojo;
import org.apache.maven.api.plugin.testing.MojoTest;
import org.apache.maven.execution.MavenSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MojoTest
public class GenerateDtosMojoTest {
    public static final String testProjectPath = "/test_project/";
    public static final String outputSourcesPath = "src/out/java/";
    public static final String expectedOutputDir = "src/expected_output/";
    public static final String testDtoPackagePath = "solutions/sulfura/hyperkit/dtos/";

    @Test
    @DisplayName("Test DTO generation from Maven Mojo")
    @Basedir("src/test/resources/test_project/")
    @InjectMojo(goal = "hyperkit-dto-generator-maven-plugin")
    public void testGenerateDtos(GenerateDtosMojo mojo) throws URISyntaxException {

        String testProjectFolderPath = Objects.requireNonNull(this.getClass().getResource(testProjectPath)).toURI().getPath();

        mojo.execute() ;

        var generatedFile = new File(testProjectFolderPath + outputSourcesPath +testDtoPackagePath + "UserDto.java");
        assertTrue(generatedFile.exists(), "Generated DTO file should exist at " + generatedFile.getAbsolutePath());

        try(FileInputStream fis = new FileInputStream(generatedFile)) {
            var content = new String(fis.readAllBytes());
            assertTrue(content.contains("public class UserDto"), "Generated content should contain 'public class UserDto'");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
