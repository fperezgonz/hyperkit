package solutions.sulfura.gend.dtos;


import com.google.auto.service.AutoService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import solutions.sulfura.gend.DtoAnnotationProcessor;

import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

@AutoService(DtoAnnotationProcessor.class)
public class DtoGeneratorTest {

    static final String testGeneratedClassesDir = "sulfura/temp/";

    @BeforeAll
    public static void setupTempFolder() throws IOException {
        for (File file : new File(testGeneratedClassesDir).listFiles()) {

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
    public void generateDtoTest() throws MalformedURLException, ClassNotFoundException {

        Class<?> compiledClass = compileClassWithProcessor(this.getClass().getResource("/SourceClassTypes.java").getFile()
                , "solutions.sulfura.gend.dtos.SourceClassTypes");

    }

    @Test
    public void generateDtoWithIncludedTest() throws MalformedURLException, ClassNotFoundException {

        Class<?> compiledClass = compileClassWithProcessor(this.getClass().getResource("/SourceClassWithIncluded.java").getFile()
                , "solutions.sulfura.gend.dtos.SourceClassWithIncluded");

    }

    @Test
    public void generateDtoWithGetterSetter() throws MalformedURLException, ClassNotFoundException {

        Class<?> compiledClass = compileClassWithProcessor(this.getClass().getResource("/SourceClassGetterSetter.java").getFile()
                , "solutions.sulfura.gend.dtos.SourceClassGetterSetter");

    }

    @Test
    public void generateDtoWithGenericTypes() throws MalformedURLException, ClassNotFoundException {

        Class<?> compiledClass = compileClassWithProcessor(this.getClass().getResource("/generics/SingleGenericParamSourceClass.java").getFile()
                , "solutions.sulfura.gend.dtos.generics.SingleGenericParamSourceClass");

    }

    @Test
    public void generateChildDtoWithParameterizedType() throws ClassNotFoundException, MalformedURLException {

        Class<?> compiledClass = compileClassWithProcessor(this.getClass().getResource("/generics/inheritance/GenericChildClassWithParameterizedType.java").getFile()
                , "solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedType");

    }

    @Test
    public void generateDtosWithCircularDependencies() throws MalformedURLException, ClassNotFoundException {
        compileClassesWithProcessor(this.getClass().getResource("/circular_dependencies/SourceClassA.java").getFile()
                , this.getClass().getResource("/circular_dependencies/SourceClassB.java").getFile());
    }

    Class compileClassWithProcessor(String filePath, String className) throws MalformedURLException, ClassNotFoundException {
        compileClassesWithProcessor(filePath);
        return loadCompiledClass(className);
    }

    void compileClassesWithProcessor(String... filePaths) throws MalformedURLException, ClassNotFoundException {

        List<String> args = new ArrayList<>();
        //Compiler options
        args.add("-d");
        args.add(testGeneratedClassesDir);
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

    public Class<?> loadCompiledClass(String className) throws MalformedURLException, ClassNotFoundException {
        URL[] classLoaderUrls = {new File(testGeneratedClassesDir).toURI().toURL()};
        ClassLoader classLoader = new URLClassLoader(classLoaderUrls);

        return Class.forName(className, true, classLoader);
    }

}