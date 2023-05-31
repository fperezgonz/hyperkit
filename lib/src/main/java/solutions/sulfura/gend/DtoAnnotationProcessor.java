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
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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

        for (TypeElement element : elements) {
            createDtoFromClassElement(element, roundEnv);
        }

        return true;

    }

    public void createDtoFromClassElement(TypeElement element, RoundEnvironment roundEnv) {

        Dto dtoAnnotation = element.getAnnotation(Dto.class);
        List<? extends TypeMirror> types = null;

        try {
            dtoAnnotation.include();
        } catch (MirroredTypesException mte) {
            types = mte.getTypeMirrors();
        }

        final List<? extends TypeMirror> finalIncludedTypes = types;

        Map<String, DtoPropertyData> dtoProperties = new HashMap<>();

        //Collect public field data
        List<? extends Element> publicFields = element.getEnclosedElements().stream()
                .filter(enclosedElement ->
                        enclosedElement.getKind() == ElementKind.FIELD
                                && enclosedElement.getModifiers().contains(Modifier.PUBLIC)
                                && (enclosedElement.getAnnotation(DtoProperty.class) != null || finalIncludedTypes.isEmpty() || finalIncludedTypes.stream()
                                .anyMatch(annotationType -> enclosedElement.getAnnotationMirrors().contains(annotationType)))
                )
                .collect(Collectors.toList());

        for (Element field : publicFields) {
            String fieldName = field.getSimpleName().toString();
            dtoProperties.put(fieldName, new DtoPropertyData(fieldName, true, true));
        }

        //Collect getter and setter data
        List<? extends Element> gettersAndSetters = element.getEnclosedElements().stream()
                .filter(enclosedElement ->
                        enclosedElement.getKind() == ElementKind.METHOD
                                && enclosedElement.getModifiers().contains(Modifier.PUBLIC)
                                && (enclosedElement.getAnnotation(DtoProperty.class) != null || finalIncludedTypes.isEmpty() || finalIncludedTypes.stream()
                                .anyMatch(annotationType -> enclosedElement.getAnnotationMirrors().contains(annotationType)))
                                &&
                                (
                                        ((enclosedElement.getSimpleName().toString().startsWith("get")
                                                || enclosedElement.getSimpleName().toString().startsWith("is"))
                                                && ((ExecutableType) enclosedElement.asType()).getReturnType().getKind() != TypeKind.VOID
                                                && ((ExecutableType) enclosedElement.asType()).getParameterTypes().isEmpty()
                                        )
                                                || (enclosedElement.getSimpleName().toString().startsWith("set")
                                                && ((ExecutableType) enclosedElement.asType()).getReturnType().getKind() == TypeKind.VOID
                                                && ((ExecutableType) enclosedElement.asType()).getParameterTypes().size() == 1
                                        )
                                )
                )
                .collect(Collectors.toList());

        for (Element getterSetter : gettersAndSetters) {

            String getterSetterName = getterSetter.getSimpleName().toString();

            boolean canRead = false;
            boolean canWrite = false;

            if (getterSetterName.startsWith("get")) {
                canRead = true;
                getterSetterName = getterSetterName.substring(0, 3);
            } else if (getterSetterName.startsWith("is")) {
                canRead = true;
                getterSetterName = getterSetterName.substring(0, 2);
            } else if (getterSetterName.startsWith("set")) {
                canWrite = true;
                getterSetterName = getterSetterName.substring(0, 3);
            }

            DtoPropertyData propertyData = dtoProperties.get(getterSetterName);

            if (propertyData == null) {
                propertyData = new DtoPropertyData(getterSetter.getSimpleName().toString(), false, false);
                dtoProperties.put(getterSetterName, propertyData);
            }

            if (canRead) {
                propertyData.canRead = true;
            }

            if (canWrite) {
                propertyData.canWrite = true;
            }

        }

        String packageName = null;

        if (dtoAnnotation.destPackageName() != null) {
            packageName = dtoAnnotation.destPackageName();
        }

        if (packageName == null || packageName.isEmpty()) {
            Element parentElement = element.getEnclosingElement();
            while (parentElement.getKind() != ElementKind.PACKAGE) {
                parentElement = parentElement.getEnclosingElement();
            }
            packageName = parentElement.toString();
        }

        String dtoClassName = dtoAnnotation.prefix() + element.getSimpleName() + dtoAnnotation.suffix();

        //Generate code
        try {
            JavaFileObject builderFile = processingEnv.getFiler()
                    .createSourceFile(packageName + "." + dtoClassName);
            StringBuilder stringBuilder = new StringBuilder();
            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                // writing generated file to out …

                if (packageName != null) {
                    stringBuilder.append("package ");
                    stringBuilder.append(packageName);
                    stringBuilder.append(";");
                    stringBuilder.append("\n");
                }

                stringBuilder.append("public class ");
                stringBuilder.append(dtoClassName);
                stringBuilder.append(" {");
                stringBuilder.append("\n");

                stringBuilder.append("    public ");
                stringBuilder.append(dtoClassName);
                stringBuilder.append("(){}");
                stringBuilder.append("\n");
                //TODO Generate properties, getters, setters and builder
                stringBuilder.append(" }");
                System.out.println(stringBuilder.toString());
                out.print(stringBuilder.toString());
            }
        } catch (IOException e) {
            //TODO Create error message
        }
    }

    public static class DtoPropertyData {

        public DtoPropertyData(String name, boolean canRead, boolean canWrite) {
            this.name = name;
            this.canRead = canRead;
            this.canWrite = canWrite;
        }

        String name;
        boolean canRead;
        boolean canWrite;

    }

}