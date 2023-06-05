package solutions.sulfura.gend;

import solutions.sulfura.gend.dtos.AnnotationProcessorUtils;
import solutions.sulfura.gend.dtos.DtoCodeGenUtils;
import solutions.sulfura.gend.dtos.annotations.Dto;
import solutions.sulfura.gend.dtos.annotations.DtoProperty;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
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
            Map<String, AnnotationProcessorUtils.DtoPropertyData> dtoProperties = collectClassData(element);
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
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
        }

        return true;

    }

    public Map<String, AnnotationProcessorUtils.DtoPropertyData> collectClassData(TypeElement element) {

        Dto dtoAnnotation = element.getAnnotation(Dto.class);
        List<? extends AnnotationMirror> types = null;

        try {
            dtoAnnotation.include();
        } catch (MirroredTypesException mte) {
            types = (List<AnnotationMirror>) mte.getTypeMirrors();
        }

        final List<? extends AnnotationMirror> finalIncludedTypes = types;

        Map<String, AnnotationProcessorUtils.DtoPropertyData> dtoProperties = new HashMap<>();

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
            AnnotationProcessorUtils.DtoPropertyData.Builder propertyDataBuilder = AnnotationProcessorUtils.DtoPropertyData.builder();
            propertyDataBuilder.typeMirror = field.asType();
            propertyDataBuilder.name(field.getSimpleName().toString())
                    .canRead(true)
                    .canWrite(true);
            AnnotationProcessorUtils.DtoPropertyData propertyData = propertyDataBuilder.build();
            dtoProperties.put(propertyData.name, propertyData);
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
            AnnotationProcessorUtils.DtoPropertyData.Builder propertyDataBuilder = AnnotationProcessorUtils.DtoPropertyData.builder();
            TypeMirror propertyType;

            if (getterSetterName.startsWith("get")) {
                propertyDataBuilder.canRead(true);
                String propertyName = getterSetterName.substring(3);
                propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
                propertyDataBuilder.name(propertyName);
                propertyType = ((ExecutableType) getterSetter.asType()).getReturnType();
            } else if (getterSetterName.startsWith("is")) {
                propertyDataBuilder.canRead(true);
                String propertyName = getterSetterName.substring(2);
                propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
                propertyDataBuilder.name(propertyName);
                propertyType = ((ExecutableType) getterSetter.asType()).getReturnType();
            } else if (getterSetterName.startsWith("set")) {
                propertyDataBuilder.canWrite(true);
                String propertyName = getterSetterName.substring(3);
                propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
                propertyDataBuilder.name(propertyName);
                propertyType = ((ExecutableType) getterSetter.asType()).getParameterTypes().get(0);
            } else {
                throw new RuntimeException("Error processing method " + getterSetterName + " of class " + element.getSimpleName() + ". It is neither a getter nor setter");
            }

            propertyDataBuilder.typeMirror = propertyType;


            AnnotationProcessorUtils.DtoPropertyData propertyData = dtoProperties.get(propertyDataBuilder.name);

            if (propertyData == null) {
                propertyData = propertyDataBuilder.build();
                dtoProperties.put(propertyData.name, propertyData);
            } else {
                if (propertyDataBuilder.canRead) {
                    propertyData.canRead = true;
                }

                if (propertyDataBuilder.canWrite) {
                    propertyData.canWrite = true;
                }
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

        Element parentElement = element.getEnclosingElement();

        while (parentElement.getKind() != ElementKind.PACKAGE) {
            parentElement = parentElement.getEnclosingElement();
        }

        return parentElement.toString();
    }

    public String generateDtoClass(Dto dtoAnnotation, TypeElement element, Map<String, AnnotationProcessorUtils.DtoPropertyData> dtoProperties) {

        String packageName = getDestPackageName(dtoAnnotation, element);
        String dtoClassName = getDtoClassName(dtoAnnotation, element);

        //Generate code
        DtoCodeGenUtils stringBuilder = new DtoCodeGenUtils();
        // writing generated file to out …


        stringBuilder.addPackageDeclaration(packageName)
                .append("import java.util.List;\n")
                .append("import solutions.sulfura.gend.dtos.annotations.DtoFor;\n")
                .append("import solutions.sulfura.gend.dtos.Dto;\n\n")
                .append("import io.vavr.control.Option;\n")
                .append("@DtoFor(")
                .append(element.getQualifiedName())
                .append(".class)\n")
                .append("public class ")
                .append(dtoClassName)
                .append(" implements Dto{\n\n");

        //Generate properties
        for (AnnotationProcessorUtils.DtoPropertyData dtoPropertyData : dtoProperties.values()) {

            AnnotationProcessorUtils.PropertyTypeDeclaration fieldTypeDeclaration = AnnotationProcessorUtils.typeToPropertyTypeDeclaration(dtoPropertyData.typeMirror);
            //TODO add qualified names to imports if there are no clashes with other types
            //TODO replace qualified names with simple names in the case of imported types
            stringBuilder.addFieldDeclaration(DtoCodeGenUtils.DtoPropertyData.builder()
                    .typeDeclaration(fieldTypeDeclaration.fieldDeclarationLiteral.toString())
                    .propertyName(dtoPropertyData.name)
                    .build());

        }

        //Generate constructor
        stringBuilder.append('\n')
                .append("    public ")
                .append(dtoClassName)
                .append("(){}\n\n");

        //TODO generate builder

        stringBuilder.append(" }");
        return stringBuilder.toString();

    }

}