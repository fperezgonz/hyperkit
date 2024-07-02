package solutions.sulfura.gend.dtos;


import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import solutions.sulfura.gend.dtos.annotation_processor.DtoAnnotationProcessor;

import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DtoGeneratorTest {

    static final String testOutputDir = "sulfura/temp/";
    static final String expectedOutputDir = "src/test/resources/expected_output/";
    static final String testInputResourcesPath = "/source_code/test_input_sources/";

    @BeforeAll
    public static void setupTempFolder() throws IOException {
        for (File file : new File(testOutputDir).listFiles()) {

            if (Objects.equals(file.getName(), ".gitignore")) {
                continue;
            }

            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else {
                FileUtils.delete(file);
            }

        }
    }

    @Test
    public void generateDtoTest() throws MalformedURLException, ClassNotFoundException, URISyntaxException {
        String qualifiedClassName = "solutions.sulfura.gend.dtos.SourceClassTypes";

        Class<?> compiledClass = compileClassWithProcessor(Paths.get(this.getClass().getResource(testInputResourcesPath + "SourceClassTypes.java").toURI()).toString()
                , qualifiedClassName);
        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName);

    }

    @Test
    public void generateDtoWithUppercasePropertiesTest() throws MalformedURLException, ClassNotFoundException, URISyntaxException {
        String qualifiedClassName = "solutions.sulfura.gend.dtos.SourceUpperCaseProperties";

        Class<?> compiledClass = compileClassWithProcessor(Paths.get(this.getClass().getResource(testInputResourcesPath + "SourceUpperCaseProperties.java").toURI()).toString()
                , qualifiedClassName);
        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName);

    }

    @Test
    public void generateDtoWithIncludedTest() throws MalformedURLException, ClassNotFoundException, URISyntaxException {
        String qualifiedClassName = "solutions.sulfura.gend.dtos.SourceClassWithIncluded";

        Class<?> compiledClass = compileClassWithProcessor(Paths.get(this.getClass().getResource(testInputResourcesPath + "SourceClassWithIncluded.java").toURI()).toString()
                , qualifiedClassName);
        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName);

    }

    @Test
    public void generateDtoWithGetterSetter() throws MalformedURLException, ClassNotFoundException, URISyntaxException {
        String qualifiedClassName = "solutions.sulfura.gend.dtos.SourceClassGetterSetter";

        Class<?> compiledClass = compileClassWithProcessor(Paths.get(this.getClass().getResource(testInputResourcesPath + "SourceClassGetterSetter.java").toURI()).toString()
                , qualifiedClassName);
        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName);

    }

    @Test
    public void generateDtoWithGenericTypes() throws MalformedURLException, ClassNotFoundException, URISyntaxException {
        String qualifiedClassName = "solutions.sulfura.gend.dtos.generics.SingleGenericParamSourceClass";

        Class<?> compiledClass = compileClassWithProcessor(Paths.get(this.getClass().getResource(testInputResourcesPath + "generics/SingleGenericParamSourceClass.java").toURI()).toString()
                , qualifiedClassName);
        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName);

    }

    @Test
    public void generateChildDtoWithParameterizedType() throws ClassNotFoundException, MalformedURLException, URISyntaxException {
        String qualifiedClassName = "solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedType";

        Class<?> compiledClass = compileClassWithProcessor(Paths.get(this.getClass().getResource(testInputResourcesPath + "generics/inheritance/GenericChildClassWithParameterizedType.java").toURI()).toString()
                , qualifiedClassName);
        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassName);

    }

    @Test
    public void generateDtosWithCircularDependencies() throws MalformedURLException, ClassNotFoundException, URISyntaxException {
        String qualifiedClassNameA = "solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA";
        String qualifiedClassNameB = "solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB";
        compileClassesWithProcessor(Paths.get(this.getClass().getResource(testInputResourcesPath + "circular_dependencies/SourceClassA.java").toURI()).toString()
                , Paths.get(this.getClass().getResource(testInputResourcesPath + "circular_dependencies/SourceClassB.java").toURI()).toString());
        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassNameA);
        assertGeneratedDtoSourceCodeMatchesExpectedOutput(qualifiedClassNameB);
    }

    Class<?> compileClassWithProcessor(String filePath, String className) throws MalformedURLException, ClassNotFoundException {
        compileClassesWithProcessor(filePath);
        return loadCompiledClass(className);
    }

    void compileClassesWithProcessor(String... filePaths) throws MalformedURLException, ClassNotFoundException {

        List<String> args = new ArrayList<>();
        //Compiler options
        args.add("-d");
        args.add(testOutputDir);
        args.add("-processor");
        args.add(DtoAnnotationProcessor.class.getCanonicalName());
        //Files to compile
        args.addAll(Arrays.asList(filePaths));

        int compilerResult = ToolProvider.getSystemJavaCompiler().run(System.in, System.out, System.err,
                args.toArray(new String[0]));

        if (compilerResult != 0) {
            throw new RuntimeException("Could not compile classes at paths: " + Arrays.stream(filePaths).reduce("", (str1, str2) -> str1 + ", " + str2));
        }


    }

    public void assertGeneratedDtoSourceCodeMatchesExpectedOutput(String qualifiedClassName) {
        String outputPathString = testOutputDir + "/" + qualifiedClassName.replace('.', '/') + "Dto.java";
        String expectedOutputPathString = expectedOutputDir + "/" + qualifiedClassName.replace('.', '/') + "Dto.java";
        Path expectedOutputPath = new File(expectedOutputPathString).toPath();
        Path outputPath = new File(outputPathString).toPath();
        try {
            String expectedOutput = new String(Files.readAllBytes(expectedOutputPath));
            String output = new String(Files.readAllBytes(outputPath));
            Assertions.assertEquals(expectedOutput, output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> loadCompiledClass(String className) throws MalformedURLException, ClassNotFoundException {
        URL[] classLoaderUrls = {new File(testOutputDir).toURI().toURL()};
        ClassLoader classLoader = new URLClassLoader(classLoaderUrls);

        return Class.forName(className, true, classLoader);
    }

}