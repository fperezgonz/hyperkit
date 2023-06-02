package solutions.sulfura.gend.dtos;


import com.google.auto.service.AutoService;
import org.joor.CompileOptions;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.junit.jupiter.api.Test;
import solutions.sulfura.gend.DtoAnnotationProcessor;

import java.util.Scanner;

@AutoService(DtoAnnotationProcessor.class)
public class DtoGeneratorTest {

    @Test
    public void generateDtoTest() {
        String exampleDtoClassSource = new Scanner(this.getClass().getResourceAsStream("/SourceClassTypes.java"), "UTF-8")
                .useDelimiter("\\A").next();
        DtoAnnotationProcessor annotationProcessor = new DtoAnnotationProcessor();
        try {
            Reflect.compile("solutions.sulfura.gend.dto.SourceClassTypes",
                    exampleDtoClassSource,
                    new CompileOptions().processors(annotationProcessor)
            );

        } catch (ReflectException rethrow) {
            throw new RuntimeException(rethrow);
        }
    }

    @Test
    public void generateDtoWithIncludedTest() {
        String exampleDtoClassSource = new Scanner(this.getClass().getResourceAsStream("/SourceClassWithIncluded.java"), "UTF-8")
                .useDelimiter("\\A").next();
        DtoAnnotationProcessor annotationProcessor = new DtoAnnotationProcessor();
        try {
            Reflect.compile("solutions.sulfura.gend.dto.SourceClassWithIncluded",
                    exampleDtoClassSource,
                    new CompileOptions().processors(annotationProcessor)
            );

        } catch (ReflectException rethrow) {
            throw new RuntimeException(rethrow);
        }
    }

    @Test
    public void generateDtoWithGetterSetter() {
        String exampleDtoClassSource = new Scanner(this.getClass().getResourceAsStream("/SourceClassGetterSetter.java"), "UTF-8")
                .useDelimiter("\\A").next();
        DtoAnnotationProcessor annotationProcessor = new DtoAnnotationProcessor();
        try {
            Reflect.compile("solutions.sulfura.gend.dto.SourceClassGetterSetter",
                    exampleDtoClassSource,
                    new CompileOptions().processors(annotationProcessor)
            );

        } catch (ReflectException rethrow) {
            throw new RuntimeException(rethrow);
        }
    }

}