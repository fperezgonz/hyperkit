package solutions.sulfura.gend;

import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.DtoProperty;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.*;
import java.util.stream.Collectors;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DtoAnnotationProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        HashSet<String> result = new HashSet<>();
        result.add(Dto.class.getName());

        return result;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<TypeElement> elements = (Set<TypeElement>) roundEnv.getElementsAnnotatedWith(Dto.class);

        TypeKind[] fieldKinds = {TypeKind.ARRAY,
                TypeKind.BYTE,
                TypeKind.CHAR,
                TypeKind.BOOLEAN,
                TypeKind.INT,
                TypeKind.LONG,
                TypeKind.FLOAT,
                TypeKind.DOUBLE,
                TypeKind.SHORT,
                TypeKind.TYPEVAR,
                TypeKind.DECLARED
        };

        for (TypeElement element : elements) {

            element.asType();
            Dto dtoAnnotation = element.getAnnotation(Dto.class);
            List<? extends TypeMirror> types = null;

            try {
                dtoAnnotation.include();
            } catch (MirroredTypesException mte) {
                types = mte.getTypeMirrors();
            }

            final List<? extends TypeMirror> finalIncludedTypes = types;

            List<? extends Element> publicFields = element.getEnclosedElements().stream()
                    .filter(enclosedElement ->
                            enclosedElement.getKind() == ElementKind.FIELD
                                    && Arrays.stream(fieldKinds).anyMatch(kind -> kind == enclosedElement.asType().getKind())
                                    && enclosedElement.getModifiers().contains(Modifier.PUBLIC)
                                    && (enclosedElement.getAnnotation(DtoProperty.class) != null || finalIncludedTypes.size() == 0 || finalIncludedTypes.stream()
                                    .anyMatch(annotationType -> enclosedElement.getAnnotationMirrors().contains(annotationType)))
                    )
                    .collect(Collectors.toList());
            List<? extends Element> gettersAndSetters = element.getEnclosedElements().stream()
                    .filter(enclosedElement ->
                            enclosedElement.getKind() == ElementKind.METHOD
                                    && enclosedElement.getModifiers().contains(Modifier.PUBLIC)
                                    && (enclosedElement.getAnnotation(DtoProperty.class) != null || finalIncludedTypes.size() == 0 || finalIncludedTypes.stream()
                                    .anyMatch(annotationType -> enclosedElement.getAnnotationMirrors().contains(annotationType)))
                                    && (enclosedElement.getSimpleName().toString().startsWith("get") || enclosedElement.getSimpleName().toString().startsWith("is") || enclosedElement.getSimpleName().toString().startsWith("set"))
                    )
                    .collect(Collectors.toList());

            return true;
        }

        return true;

    }

}