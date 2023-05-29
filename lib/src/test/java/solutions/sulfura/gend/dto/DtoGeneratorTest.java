package solutions.sulfura.gend.dto;


import com.google.auto.service.AutoService;
import org.joor.CompileOptions;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.junit.jupiter.api.Test;
import solutions.sulfura.gend.DtoAnnotationProcessor;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoService(DtoAnnotationProcessor.class)
public class DtoGeneratorTest {

    @Test
    public void generateDtoTest() {
        String exampleDtoClassSource = new Scanner(this.getClass().getResourceAsStream("/ExampleDtoSourceClass.java.example"), "UTF-8")
                .useDelimiter("\\A").next();
        DtoAnnotationProcessor annotationProcessor = new DtoAnnotationProcessor();
        try {
            Reflect.compile("solutions.sulfura.gend.dto.ExampleDtoSourceClass",
                    exampleDtoClassSource,
                    new CompileOptions().processors(annotationProcessor)
            );
        } catch (ReflectException rethrow) {
            throw new RuntimeException(rethrow);
        }
    }

}