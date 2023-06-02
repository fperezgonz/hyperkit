package solutions.sulfura.gend;

import solutions.sulfura.gend.dtos.annotations.Dto;
import solutions.sulfura.gend.dtos.annotations.DtoProperty;

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

    public String getDtoClassName(Dto dtoAnnotation, TypeElement element) {
        return element.getSimpleName() + dtoAnnotation.suffix();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        @SuppressWarnings("unchecked")
        Set<TypeElement> elements = (Set<TypeElement>) roundEnv.getElementsAnnotatedWith(Dto.class);

        for (TypeElement element : elements) {
            Map<String, DtoPropertyData> dtoProperties = collectProperties(element);
            Dto dtoAnnotation = element.getAnnotation(Dto.class);
            String dtoSourceCode = generateDtoClass(dtoAnnotation, element, dtoProperties);
            System.out.println(dtoSourceCode);

            try {
                String packageName = getDestPackageName(dtoAnnotation, element);
                JavaFileObject builderFile = processingEnv.getFiler()
                        .createSourceFile(packageName + "." + dtoAnnotation.prefix() + getDtoClassName(dtoAnnotation, element));
                try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                    out.print(dtoSourceCode);
                }
            } catch (IOException e) {
                //TODO Create error message
            }
        }

        return true;

    }

    public Map<String, DtoPropertyData> collectProperties(TypeElement element) {

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
            String typeQualifiedName = field.asType().toString();
            String fieldName = field.getSimpleName().toString();
            dtoProperties.put(fieldName, new DtoPropertyData(fieldName, true, true, typeQualifiedName));
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
            String propertyName = "";
            String typeQualifiedName;
            boolean canRead = false;
            boolean canWrite = false;

            if (getterSetterName.startsWith("get")) {
                canRead = true;
                propertyName = getterSetterName.substring(3);
                propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
                typeQualifiedName = ((ExecutableType) getterSetter.asType()).getReturnType().toString();
            } else if (getterSetterName.startsWith("is")) {
                canRead = true;
                propertyName = getterSetterName.substring(02);
                propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
                typeQualifiedName = ((ExecutableType) getterSetter.asType()).getReturnType().toString();
            } else if (getterSetterName.startsWith("set")) {
                canWrite = true;
                propertyName = getterSetterName.substring(3);
                propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
                typeQualifiedName = ((ExecutableType) getterSetter.asType()).getParameterTypes().get(0).toString();
            } else {
                throw new RuntimeException("Error processing method " + getterSetterName + " of class " + element.getSimpleName() + ". It is neither a getter nor setter");
            }

            DtoPropertyData propertyData = dtoProperties.get(propertyName);

            if (propertyData == null) {
                propertyData = new DtoPropertyData(propertyName, false, false, typeQualifiedName);
                dtoProperties.put(propertyName, propertyData);
            }

            if (canRead) {
                propertyData.canRead = true;
            }

            if (canWrite) {
                propertyData.canWrite = true;
            }

        }

        //TODO collect from class hierarchy
        //TODO deal with generic types

        return dtoProperties;
    }

    public String getDestPackageName(Dto dtoAnnotation, TypeElement element) {

        String packageName = null;

        if (dtoAnnotation.destPackageName() != null) {
            packageName = dtoAnnotation.destPackageName();
        }

        if (packageName == null || packageName.isEmpty()) {
            packageName = getElementPackageName(element);
        }

        return packageName;
    }

    public String getElementPackageName(TypeElement element) {

        String packageName = null;
        Element parentElement = element.getEnclosingElement();

        while (parentElement.getKind() != ElementKind.PACKAGE) {
            parentElement = parentElement.getEnclosingElement();
        }
        packageName = parentElement.toString();

        return packageName;
    }

    public String generateDtoClass(Dto dtoAnnotation, TypeElement element, Map<String, DtoPropertyData> dtoProperties) {

        String packageName = getDestPackageName(dtoAnnotation, element);
        String dtoClassName = getDtoClassName(dtoAnnotation, element);

        //Generate code
        StringBuilder stringBuilder = new StringBuilder();
        // writing generated file to out …

        stringBuilder.append("package ")
                .append(packageName)
                .append(";")
                .append("\n\n")
                .append("import solutions.sulfura.gend.dtos.annotations.DtoFor;\n")
                .append("import solutions.sulfura.gend.dtos.Dto;\n\n")
                .append("@DtoFor(")
                .append(element.getQualifiedName())
                .append(".class)\n")
                .append("public class ")
                .append(dtoClassName)
                .append(" implements Dto{\n\n");

        //Generate properties
        for (DtoPropertyData dtoPropertyData : dtoProperties.values()) {
            if (dtoPropertyData.canRead && dtoPropertyData.canWrite) {
                stringBuilder.append("    public " + dtoPropertyData.typeSimpleName + " " + dtoPropertyData.name + ";\n");
            } else if (dtoPropertyData.canRead || dtoPropertyData.canWrite) {
                stringBuilder.append("    " + dtoPropertyData.typeSimpleName + " " + dtoPropertyData.name + ";\n");
            }
        }

        //Generate constructor
        stringBuilder.append("\n")
                .append("    public ")
                .append(dtoClassName)
                .append("(){}\n\n");


        //Generate getters and setters
        for (DtoPropertyData dtoPropertyData : dtoProperties.values()) {
            if (!dtoPropertyData.canRead || !dtoPropertyData.canWrite) {
                if (dtoPropertyData.canRead) {
                    stringBuilder.append("    public " + dtoPropertyData.typeSimpleName + " get" + dtoPropertyData.name.substring(0, 1).toUpperCase()
                            + dtoPropertyData.name.substring(1) + "(){ " +
                            "return this." + dtoPropertyData.name + "; }\n\n");
                }
                if (dtoPropertyData.canWrite) {
                    stringBuilder.append("    public void set" + dtoPropertyData.name.substring(0, 1).toUpperCase()
                            + dtoPropertyData.name.substring(1) + "(" + dtoPropertyData.typeSimpleName + " " + dtoPropertyData.name + "){ " +
                            "this." + dtoPropertyData.name + " = " + dtoPropertyData.name + "; }\n\n");
                }
            }
        }

        //TODO generate builder

        stringBuilder.append(" }");
        return stringBuilder.toString();

    }

    public static class DtoPropertyData {

        public DtoPropertyData(String name, boolean canRead, boolean canWrite, String typeQualifiedName) {
            this.name = name;
            this.canRead = canRead;
            this.canWrite = canWrite;
            this.typeQualifiedName = typeQualifiedName;
            this.typeSimpleName = typeQualifiedName.substring(typeQualifiedName.lastIndexOf('.') + 1);
        }

        String typeQualifiedName;
        String typeSimpleName;
        String name;
        boolean canRead;
        boolean canWrite;

    }

}