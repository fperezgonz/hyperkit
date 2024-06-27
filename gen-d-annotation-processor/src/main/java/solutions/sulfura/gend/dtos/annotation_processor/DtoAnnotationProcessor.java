package solutions.sulfura.gend.dtos.annotation_processor;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.Dto;
import solutions.sulfura.gend.dtos.annotations.DtoProperty;
import solutions.sulfura.gend.dtos.annotation_processor.AnnotationProcessorUtils.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.tools.Diagnostic;
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
        //Same for DtoConfig
        Map<String, String> sourceClassName_dtoConfigClassName = elementsAnnotatedWithDto.stream()
                .collect(Collectors.<TypeElement, String, String>toMap(elem -> elem.getQualifiedName().toString(),
                        elem -> getDtoConfQualifiedName(elem.getAnnotation(Dto.class), elem)));
        //TODO Add mappings for preexisting Dtos

        //Generate Dto classes for each element Annotated with @Dto
        for (TypeElement annotatedElement : elementsAnnotatedWithDto) {

            //Collect class properties
            Map<String, SourceClassPropertyData> dtoProperties = collectClassPropertiesData((DeclaredType) annotatedElement.asType(), annotatedElement);

            //Generate source code
            Dto dtoAnnotationInstance = annotatedElement.getAnnotation(Dto.class);
            String dtoSourceCode = generateDtoSourceCode(dtoAnnotationInstance, annotatedElement, dtoProperties, sourceClassName_dtoClassName, sourceClassName_dtoConfigClassName);

            //Create source file
            try {

                String packageName = getDestPackageName(dtoAnnotationInstance, annotatedElement);
                JavaFileObject builderFile = processingEnv.getFiler()
                        .createSourceFile(packageName + "." + dtoAnnotationInstance.prefix() + getDtoClassName(dtoAnnotationInstance, annotatedElement));

                try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                    out.print(dtoSourceCode);
                }

            } catch (IOException | RuntimeException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Exception thrown while generating dto class for element " + annotatedElement + ". Exception message: " + e.getMessage());
                System.err.println(dtoSourceCode);
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
     * @return the qualified name of the DtoConf that will be generated for the element
     */
    public String getDtoConfQualifiedName(Dto dtoAnnotationInstance, TypeElement element) {
        return getDestPackageName(dtoAnnotationInstance, element) + "." + getDtoClassName(dtoAnnotationInstance, element) + ".Conf";
    }

    /**
     * @return for a type like Map.Entry&lt K, V&gt , it would return a String containing &lt K, V&gt . It returns null if the type does not have any parameters
     */
    public StringBuilder typeArgumentsString(DeclaredType genericType) {

        if (genericType.getTypeArguments().isEmpty()) {
            return null;
        }

        StringBuilder genericTypeArgs = new StringBuilder();
        genericTypeArgs.append('<');
        boolean first = true;

        for (TypeMirror typeArgument : genericType.getTypeArguments()) {

            if (first) {
                first = false;
            } else {
                genericTypeArgs.append(", ");
            }

            genericTypeArgs.append(typeArgument.toString());

        }

        genericTypeArgs.append('>');

        return genericTypeArgs;

    }

    /**
     * @param className_replacingDtoClassName a structure that maps classes to replacements. The generation process will take this into account and replace the classes declared in the dto's properties with the replacement classes
     * @return the source code for the Dto of the element
     */
    public String generateDtoSourceCode(Dto dtoAnnotationInstance, TypeElement element,
                                        Map<String, SourceClassPropertyData> dtoProperties,
                                        Map<String, String> className_replacingDtoClassName,
                                        Map<String, String> className_replacingDtoConfClassName) {

        String packageName = getDestPackageName(dtoAnnotationInstance, element);
        String dtoClassName = getDtoClassName(dtoAnnotationInstance, element);

        //Generate code
        DtoCodeGenUtils codeGenUtils = new DtoCodeGenUtils();

        AnnotationProcessorUtils annotationProcessorUtils = new AnnotationProcessorUtils();
        annotationProcessorUtils.setReplacements(className_replacingDtoClassName);

        //Package name
        codeGenUtils.addPackageDeclaration(packageName);

        //Dto and DtoConf imports
        List<CharSequence> requiredImports = annotationProcessorUtils.collectRequiredDtoAndConfImports(
                dtoProperties.values().stream()
                        .map(prop -> prop.typeMirror)
                        .collect(Collectors.toList()),
                processingEnv, true, className_replacingDtoConfClassName);

        for (CharSequence charSequence : requiredImports) {
            codeGenUtils.addImport(charSequence.toString());
        }

        //Source class import
        codeGenUtils.addImport(element.getQualifiedName().toString());

        StringBuilder dtoGenericTypeArgs = null;

        //Class declaration
        {

            //@Dto annotation
            codeGenUtils.append('\n')
                    .append("@DtoFor(")
                    .append(element.getSimpleName())
                    .append(".class)\n");

            StringBuilder classDeclaration = new StringBuilder();
            classDeclaration.append("public class ")
                    .append(dtoClassName);

            //Add parameterized types to class declaration
            dtoGenericTypeArgs = typeArgumentsString((DeclaredType) element.asType());

            if (dtoGenericTypeArgs != null) {
                classDeclaration.append(dtoGenericTypeArgs);
            }

            //Add Dto interface implementation to class declaration
            classDeclaration.append(" implements Dto<")
                    .append(element.getSimpleName());

            if (dtoGenericTypeArgs != null) {
                classDeclaration.append(dtoGenericTypeArgs);
            }

            classDeclaration.append('>');

            codeGenUtils.beginClass(classDeclaration.toString());

        }

        //Generate properties
        List<DtoCodeGenUtils.DtoPropertyData> dtoPropertyDataList = new ArrayList<>();

        for (SourceClassPropertyData sourceClassPropertyData : dtoProperties.values()) {
            Class<?> wrappingClass = null;

            TypeMirror listInterfaceType = processingEnv.getElementUtils().getTypeElement("java.util.List").asType();
            TypeMirror setInterfaceType = processingEnv.getElementUtils().getTypeElement("java.util.Set").asType();

            //If it is not an array, List or Set, wran inside an Option
            if (sourceClassPropertyData.typeMirror.getKind() != TypeKind.ARRAY
                    && (!processingEnv.getTypeUtils().isAssignable(processingEnv.getTypeUtils().erasure(sourceClassPropertyData.typeMirror), processingEnv.getTypeUtils().erasure(listInterfaceType)))
                    && (!processingEnv.getTypeUtils().isAssignable(processingEnv.getTypeUtils().erasure(sourceClassPropertyData.typeMirror), processingEnv.getTypeUtils().erasure(setInterfaceType)))) {
                wrappingClass = Option.class;
            }

            AnnotationProcessorUtils.PropertyTypeDeclaration fieldTypeDeclaration = annotationProcessorUtils.typeToPropertyTypeDeclaration(sourceClassPropertyData.typeMirror, processingEnv, wrappingClass);
            DtoCodeGenUtils.DtoPropertyData dtoPropertyData = DtoCodeGenUtils.DtoPropertyData.builder()
                    .typeDeclaration(fieldTypeDeclaration)
                    .propertyName(sourceClassPropertyData.name)
                    .build();
            //TODO add qualified names to imports if there are no clashes with other types
            codeGenUtils.addFieldDeclaration(dtoPropertyData);
            dtoPropertyDataList.add(dtoPropertyData);

        }


        //Generate constructor
        codeGenUtils.addConstructor(dtoClassName);

        codeGenUtils.addBuilder(dtoClassName, dtoGenericTypeArgs, dtoPropertyDataList);

        //Generate Conf class properties
        List<DtoCodeGenUtils.DtoPropertyData> confPropertyDataList = new ArrayList<>();

        for (SourceClassPropertyData sourceClassPropertyData : dtoProperties.values()) {

            AnnotationProcessorUtils.PropertyTypeDeclaration fieldTypeDeclaration = annotationProcessorUtils.typeToConfPropertyTypeDeclaration(sourceClassPropertyData.typeMirror, processingEnv, className_replacingDtoConfClassName);
            DtoCodeGenUtils.DtoPropertyData dtoPropertyData = DtoCodeGenUtils.DtoPropertyData.builder()
                    .typeDeclaration(fieldTypeDeclaration)
                    .propertyName(sourceClassPropertyData.name)
                    .build();
            //TODO add qualified names to imports if there are no clashes with other types
            confPropertyDataList.add(dtoPropertyData);

        }

        codeGenUtils.addConfigClass(dtoClassName, dtoGenericTypeArgs, confPropertyDataList);
        codeGenUtils.endClass();

        return codeGenUtils.toString();

    }

}