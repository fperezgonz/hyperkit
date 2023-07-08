package solutions.sulfura.gend.dtos.annotation_processor;

import solutions.sulfura.gend.dtos.annotations.Dto;
import solutions.sulfura.gend.dtos.annotations.DtoProperty;
import solutions.sulfura.gend.dtos.annotation_processor.AnnotationProcessorUtils.*;

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
        Set<TypeElement> elementsAnnotatedWithDto = (Set<TypeElement>) roundEnv.getElementsAnnotatedWith(Dto.class);

        //Collect a map of the names of classes that will be processed and the name of the Dto class that will be generated
        Map<String, String> sourceClassName_dtoClassName = elementsAnnotatedWithDto.stream()
                .collect(Collectors.<TypeElement, String, String>toMap(elem -> elem.getQualifiedName().toString(),
                        elem -> getDtoQualifiedName(elem.getAnnotation(Dto.class), elem)));
        //TODO Add mappings for existing Dtos

        //Generate Dto classes for each element Annotated with @Dto
        for (TypeElement annotatedElement : elementsAnnotatedWithDto) {

            //Collect class properties
            Map<String, SourceClassPropertyData> dtoProperties = collectClassPropertiesData((DeclaredType) annotatedElement.asType(), annotatedElement);

            //Generate source code
            Dto dtoAnnotationInstance = annotatedElement.getAnnotation(Dto.class);
            String dtoSourceCode = generateDtoSourceCode(dtoAnnotationInstance, annotatedElement, dtoProperties, sourceClassName_dtoClassName);
            System.out.println(dtoSourceCode);

            //Create source file
            try {

                String packageName = getDestPackageName(dtoAnnotationInstance, annotatedElement);
                JavaFileObject builderFile = processingEnv.getFiler()
                        .createSourceFile(packageName + "." + dtoAnnotationInstance.prefix() + getDtoClassName(dtoAnnotationInstance, annotatedElement));
                
                try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                    out.print(dtoSourceCode);
                }

            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
        }

        return true;

    }

    /**
     * @param sourceType The class referenced by the Dto annotation
     * @param element    The class whose properties are going to be collected. It can be the class referenced by the Dto annotation, or another ancestor class or interface in the Type hierarchy
     */
    public Map<String, SourceClassPropertyData> collectClassPropertiesData(DeclaredType sourceType, TypeElement element) {

        Map<String, SourceClassPropertyData> dtoProperties = null;
        List<? extends TypeMirror> superTypes = processingEnv.getTypeUtils().directSupertypes(element.asType());
        //Collect properties from ancestor classes and interfaces
        for (DeclaredType type : (List<DeclaredType>) superTypes) {
            if (!Objects.equals(type.toString(), "java.lang.Object")) {
                Map<String, SourceClassPropertyData> superClassProperties = collectClassPropertiesData(sourceType, (TypeElement) type.asElement());
                if (superClassProperties != null) {
                    if (dtoProperties == null) {
                        dtoProperties = superClassProperties;
                    } else {
                        dtoProperties.putAll(superClassProperties);
                    }
                }
            }
        }

        if (dtoProperties == null) {
            dtoProperties = new HashMap<>();
        }

        //Collect annotation data
        Dto dtoAnnotation = element.getAnnotation(Dto.class);
        List<? extends TypeMirror> types = null;

        if (dtoAnnotation == null) {
            types = Collections.EMPTY_LIST;
        } else {
            try {
                dtoAnnotation.include();
            } catch (MirroredTypesException mte) {
                types = mte.getTypeMirrors();
            }
        }

        final List<? extends TypeMirror> finalIncludedTypes = types;

        //Collect public field data
        List<? extends Element> publicFields = element.getEnclosedElements().stream()
                .filter(enclosedElement ->
                        enclosedElement.getKind() == ElementKind.FIELD
                                && enclosedElement.getModifiers().contains(Modifier.PUBLIC)
                                && (enclosedElement.getAnnotation(DtoProperty.class) != null || finalIncludedTypes.isEmpty() || finalIncludedTypes.stream()
                                .anyMatch(annotationType -> enclosedElement.getAnnotationMirrors().stream()
                                        .anyMatch(annotationMirror -> Objects.equals(annotationMirror.getAnnotationType(), annotationType))
                                ))
                )
                .collect(Collectors.toList());

        //Map public field data to DtoPropertyData
        for (Element field : publicFields) {
            SourceClassPropertyData propertyData = AnnotationProcessorUtils.fieldToSourceClassPropertyData(processingEnv, field, sourceType);
            dtoProperties.put(propertyData.name, propertyData);
        }

        //Collect getters and setters data
        List<? extends Element> gettersAndSetters = element.getEnclosedElements().stream()
                .filter(enclosedElement ->
                        enclosedElement.getKind() == ElementKind.METHOD
                                && enclosedElement.getModifiers().contains(Modifier.PUBLIC)
                                && (enclosedElement.getAnnotation(DtoProperty.class) != null || finalIncludedTypes.isEmpty() || finalIncludedTypes.stream()
                                .anyMatch(annotationType -> enclosedElement.getAnnotationMirrors().stream()
                                        .anyMatch(annotationMirror -> Objects.equals(annotationMirror.getAnnotationType(), annotationType))))
                                &&
                                (
                                        ((enclosedElement.getSimpleName().toString().startsWith("get")
                                                || enclosedElement.getSimpleName().toString().startsWith("is"))
                                                &&
                                                (((ExecutableType) enclosedElement.asType()).getReturnType().getKind() != TypeKind.VOID
                                                        && ((ExecutableType) enclosedElement.asType()).getParameterTypes().isEmpty())
                                        )
                                                ||
                                                (enclosedElement.getSimpleName().toString().startsWith("set")
                                                        && ((ExecutableType) enclosedElement.asType()).getReturnType().getKind() == TypeKind.VOID
                                                        && ((ExecutableType) enclosedElement.asType()).getParameterTypes().size() == 1)
                                )
                )
                .collect(Collectors.toList());

        //Map getters and setters data to DtoPropertyData
        for (Element getterSetter : gettersAndSetters) {

            //Get property data from getter/setter
            SourceClassPropertyData getterSetterPropertyData = AnnotationProcessorUtils.gsToSourceClassPropertyData(processingEnv, getterSetter, sourceType);

            //Merge cached data with getter/setter data
            SourceClassPropertyData cachedPropertyData = dtoProperties.get(getterSetterPropertyData.name);

            if (cachedPropertyData != null) {

                if (cachedPropertyData.canRead) {
                    getterSetterPropertyData.canRead = true;
                }

                if (cachedPropertyData.canWrite) {
                    getterSetterPropertyData.canWrite = true;
                }

            }

            //Update cache with merged instance
            dtoProperties.put(getterSetterPropertyData.name, getterSetterPropertyData);

        }

        return dtoProperties;
    }

    /**
     * Returns the package name declared on the annotation instance, or the element package name if there is no package specified on the annotation
     */
    public String getDestPackageName(Dto dtoAnnotationInstance, TypeElement element) {

        String packageName = null;

        if (dtoAnnotationInstance.destPackageName() != null) {
            packageName = dtoAnnotationInstance.destPackageName();
        }

        if (packageName == null || packageName.isEmpty()) {
            packageName = AnnotationProcessorUtils.getElementPackageName(element);
        }

        return packageName;
    }

    /**
     * @return the qualified name of the Dto that will be generated for the element
     */
    public String getDtoQualifiedName(Dto dtoAnnotationInstance, TypeElement element) {
        return getDestPackageName(dtoAnnotationInstance, element) + "." + getDtoClassName(dtoAnnotationInstance, element);
    }

    /**
     * @param className_replacingClassName a structure that maps classes to replacements. The generation process will take this into account and replace the classes declared in the dto's properties with the replacement classes
     * @return the source code for the Dto of the element
     */
    public String generateDtoSourceCode(Dto dtoAnnotationInstance, TypeElement element, Map<String, SourceClassPropertyData> dtoProperties, Map<String, String> className_replacingClassName) {

        String packageName = getDestPackageName(dtoAnnotationInstance, element);
        String dtoClassName = getDtoClassName(dtoAnnotationInstance, element);

        //Generate code
        DtoCodeGenUtils stringBuilder = new DtoCodeGenUtils();

        stringBuilder.addPackageDeclaration(packageName)
                .addImport("io.vavr.control.Option")
                .addImport("solutions.sulfura.gend.dtos.annotations.DtoFor")
                .addImport("solutions.sulfura.gend.dtos.Dto")
                .addImport(element.getQualifiedName().toString());

        AnnotationProcessorUtils annotationProcessorUtils = new AnnotationProcessorUtils();
        annotationProcessorUtils.setReplacements(className_replacingClassName);

        //Add imports for types used in properties
        for (SourceClassPropertyData sourceClassPropertyData : dtoProperties.values()) {
            for (String propertyTypeQualifiedName :
                    annotationProcessorUtils.typeToPropertyTypeDeclaration(sourceClassPropertyData.typeMirror).declaredTypesQualifiedNames) {
                //Replace with replacement
                if (className_replacingClassName.containsKey(propertyTypeQualifiedName)) {
                    propertyTypeQualifiedName = className_replacingClassName.get(propertyTypeQualifiedName);
                }

                String qualifiedNameForAlias = stringBuilder.importsSimpleTypes_qualifiedTypes.get(propertyTypeQualifiedName.substring(propertyTypeQualifiedName.lastIndexOf('.') + 1));

                if (qualifiedNameForAlias == null) {
                    stringBuilder.addImport(propertyTypeQualifiedName);
                }

            }
        }

        //Class declaration
        stringBuilder.append('\n')
                .append("@DtoFor(")
                .append(element.getSimpleName())
                .append(".class)\n")
                .append("public class ")
                .append(dtoClassName);

        //Add parameterized types to class declaration
        if (((DeclaredType) element.asType()).getTypeArguments().size() > 0) {

            stringBuilder.append('<');
            boolean first = true;
            for (TypeMirror typeArgument : ((DeclaredType) element.asType()).getTypeArguments()) {
                if (first) {
                    first = false;
                } else {
                    stringBuilder.append(',')
                            .append(' ');
                }
                stringBuilder.append(typeArgument.toString());
            }

            stringBuilder.append('>');
        }

        //Add Dto interface implementation to class declaration
        stringBuilder.append(" implements Dto<")
                .append(element.getSimpleName())
                .append(">{\n\n");

        //Generate properties
        for (SourceClassPropertyData sourceClassPropertyData : dtoProperties.values()) {

            AnnotationProcessorUtils.PropertyTypeDeclaration fieldTypeDeclaration = annotationProcessorUtils.typeToPropertyTypeDeclaration(sourceClassPropertyData.typeMirror);
            //TODO add qualified names to imports if there are no clashes with other types
            //TODO replace qualified names with simple names in the case of imported types
            stringBuilder.addFieldDeclaration(DtoCodeGenUtils.DtoPropertyData.builder()
                    .typeDeclaration(fieldTypeDeclaration)
                    .propertyName(sourceClassPropertyData.name)
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