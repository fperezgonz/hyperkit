package solutions.sulfura.gend.dtos.annotation_processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import java.util.*;

public class AnnotationProcessorUtils {

    private Map<String, String> replacements = new HashMap<>();

    public void setReplacements(Map<String, String> replacements) {
        this.replacements = replacements == null ? new HashMap<>() : replacements;
    }

    String getReplacementType(TypeMirror primitiveType) {

        String result = primitiveType.toString();

        //Replace before built-in replacement to allow replacement of primitives
        if (replacements.containsKey(result)) {
            result = replacements.get(result);
        } else if (primitiveType.getKind().isPrimitive()) {
            result = Objects.equals(result, "int") ? "Integer" : result.substring(0, 1).toUpperCase() + result.substring(1);
        }

        return result;

    }

    String getReplacementType(String qualifiedName) {
        return replacements.getOrDefault(qualifiedName, qualifiedName);
    }

    public static SourceClassPropertyData fieldToSourceClassPropertyData(ProcessingEnvironment processingEnv, Element field, DeclaredType sourceType) {

        SourceClassPropertyData.Builder propertyDataBuilder = SourceClassPropertyData.builder();
        propertyDataBuilder.typeMirror = processingEnv.getTypeUtils().asMemberOf(sourceType, field);
        propertyDataBuilder.name(field.getSimpleName().toString())
                .canRead(true)
                .canWrite(true);

        return propertyDataBuilder.build();
    }

    public static SourceClassPropertyData gsToSourceClassPropertyData(ProcessingEnvironment processingEnv, Element getterSetter, DeclaredType sourceType) {

        String getterSetterName = getterSetter.getSimpleName().toString();
        SourceClassPropertyData.Builder propertyDataBuilder = SourceClassPropertyData.builder();
        TypeMirror propertyType;

        if (getterSetterName.startsWith("get")) {

            propertyDataBuilder.canRead(true);
            String propertyName = getterSetterName.substring(3);
            propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
            propertyDataBuilder.name(propertyName);
            propertyType = ((ExecutableType) processingEnv.getTypeUtils().asMemberOf(sourceType, getterSetter)).getReturnType();

        } else if (getterSetterName.startsWith("is")) {

            propertyDataBuilder.canRead(true);
            String propertyName = getterSetterName.substring(2);
            propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
            propertyDataBuilder.name(propertyName);
            propertyType = ((ExecutableType) processingEnv.getTypeUtils().asMemberOf(sourceType, getterSetter)).getReturnType();

        } else if (getterSetterName.startsWith("set")) {

            propertyDataBuilder.canWrite(true);
            String propertyName = getterSetterName.substring(3);
            propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
            propertyDataBuilder.name(propertyName);
            propertyType = ((ExecutableType) processingEnv.getTypeUtils().asMemberOf(sourceType, getterSetter)).getParameterTypes().get(0);

        } else {
            throw new RuntimeException("Error processing method " + getterSetterName + " of class " + sourceType + ". It is neither a getter nor setter");
        }

        propertyDataBuilder.typeMirror = propertyType;

        return propertyDataBuilder.build();
    }

    public PropertyTypeDeclaration typeToPropertyTypeDeclaration(TypeMirror typeMirror, ProcessingEnvironment processingEnv) {

        PropertyTypeDeclaration.Builder fieldTypeDeclarationBuilder = PropertyTypeDeclaration.builder();
        String declaredTypeString;
        List<String> declaredTypesQualifiedNames = new ArrayList<>();

        TypeMirror listInterfaceType = processingEnv.getElementUtils().getTypeElement("java.util.List").asType();
        TypeMirror setInterfaceType = processingEnv.getElementUtils().getTypeElement("java.util.Set").asType();

        if (typeMirror.getKind() == TypeKind.ARRAY) {
            ArrayType arrayType = (ArrayType) typeMirror;
            TypeMirror typeArg = arrayType.getComponentType();

            //Use lists instead of arrays
            String collectionTypeQualifiedName = replacements.getOrDefault("java.util.List", "java.util.List");
            String collectionElementTypeLiteral;

            if (typeArg.getKind() == TypeKind.DECLARED) {
                PropertyTypeDeclaration typeArgDeclaration = typeToPropertyTypeDeclaration(typeArg, processingEnv);
                declaredTypesQualifiedNames.addAll(typeArgDeclaration.declaredTypesQualifiedNames);
                declaredTypesQualifiedNames.add(getReplacementType(((DeclaredType) typeArg).asElement().toString()));
                collectionElementTypeLiteral = typeArgDeclaration.fieldDeclarationLiteral.toString();
            } else {
                collectionElementTypeLiteral = getReplacementType(typeArg);
            }

            declaredTypeString = collectionTypeQualifiedName + "<ListOperation<" + collectionElementTypeLiteral + ">>";

            declaredTypesQualifiedNames.add(collectionTypeQualifiedName);

            declaredTypesQualifiedNames.add("solutions.sulfura.gend.dtos.ListOperation");

        } else {

            //List operations for List and Set types
            if (processingEnv.getTypeUtils().isAssignable(processingEnv.getTypeUtils().erasure(typeMirror), processingEnv.getTypeUtils().erasure(listInterfaceType))) {

                TypeMirror typeArg = ((DeclaredType) typeMirror).getTypeArguments().get(0);
                declaredTypesQualifiedNames.add("java.util.List");

                String collectionElementTypeLiteral;

                if (typeArg.getKind() == TypeKind.DECLARED) {
                    PropertyTypeDeclaration typeArgDeclaration = typeToPropertyTypeDeclaration(typeArg, processingEnv);
                    declaredTypesQualifiedNames.addAll(typeArgDeclaration.declaredTypesQualifiedNames);
                    declaredTypesQualifiedNames.add(getReplacementType(((DeclaredType) typeArg).asElement().toString()));
                    collectionElementTypeLiteral = typeArgDeclaration.fieldDeclarationLiteral.toString();
                } else {
                    collectionElementTypeLiteral = getReplacementType(typeArg);
                }

                declaredTypeString = "java.util.List<ListOperation<" + collectionElementTypeLiteral + ">>";

                declaredTypesQualifiedNames.add("solutions.sulfura.gend.dtos.ListOperation");

            } else if (processingEnv.getTypeUtils().isAssignable(processingEnv.getTypeUtils().erasure(typeMirror), processingEnv.getTypeUtils().erasure(setInterfaceType))) {

                TypeMirror typeArg = ((DeclaredType) typeMirror).getTypeArguments().get(0);
                declaredTypesQualifiedNames.add("java.util.Set");

                String collectionElementTypeLiteral;

                if (typeArg.getKind() == TypeKind.DECLARED) {
                    PropertyTypeDeclaration typeArgDeclaration = typeToPropertyTypeDeclaration(typeArg, processingEnv);
                    declaredTypesQualifiedNames.addAll(typeArgDeclaration.declaredTypesQualifiedNames);
                    declaredTypesQualifiedNames.add(getReplacementType(((DeclaredType) typeArg).asElement().toString()));
                    collectionElementTypeLiteral = typeArgDeclaration.fieldDeclarationLiteral.toString();
                } else {
                    collectionElementTypeLiteral = getReplacementType(typeArg);
                }

                declaredTypeString = "java.util.Set<ListOperation<" + collectionElementTypeLiteral + ">>";

                declaredTypesQualifiedNames.add("solutions.sulfura.gend.dtos.ListOperation");

            } else {

                declaredTypeString = getReplacementType(typeMirror);

                if (typeMirror.getKind() == TypeKind.DECLARED) {
                    declaredTypesQualifiedNames.add(getReplacementType(((DeclaredType) typeMirror).asElement().toString()));
                }

            }

        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(declaredTypeString);

        if (typeMirror instanceof DeclaredType) {
            for (TypeMirror typeArg : ((DeclaredType) typeMirror).getTypeArguments()) {
                if (typeArg.getKind() == TypeKind.DECLARED) {
                    declaredTypesQualifiedNames.add(getReplacementType(((DeclaredType) typeArg).asElement().toString()));
                }
            }
        }

        return fieldTypeDeclarationBuilder
                .fieldDeclarationLiteral(stringBuilder)
                .declaredTypesQualifiedNames(declaredTypesQualifiedNames)
                .build();
    }

    public static String getElementPackageName(TypeElement element) {

        Element parentElement = element.getEnclosingElement();

        while (parentElement.getKind() != ElementKind.PACKAGE) {
            parentElement = parentElement.getEnclosingElement();
        }

        return parentElement.toString();
    }

    public static class SourceClassPropertyData {

        public SourceClassPropertyData() {
        }

        public TypeMirror typeMirror;
        public String name;
        public boolean canRead;
        public boolean canWrite;

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {

            public TypeMirror typeMirror;
            public String name;
            public boolean canRead;
            public boolean canWrite;

            public Builder() {
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder canRead(boolean canRead) {
                this.canRead = canRead;
                return this;
            }

            public Builder canWrite(boolean canWrite) {
                this.canWrite = canWrite;
                return this;
            }

            public SourceClassPropertyData build() {

                SourceClassPropertyData result = new SourceClassPropertyData();
                result.typeMirror = this.typeMirror;
                result.name = this.name;
                result.canRead = this.canRead;
                result.canWrite = this.canWrite;

                return result;

            }

        }

    }

    public static class ClassData {
        String packageName;
        List<String> imports;
        String className;
        Collection<SourceClassPropertyData> properties;

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            String packageName;
            List<String> imports;
            String className;
            Collection<SourceClassPropertyData> properties;

            ClassData build() {

                ClassData result = new ClassData();
                result.packageName = this.packageName;
                result.className = this.className;
                result.imports = this.imports;
                result.properties = this.properties;

                return result;
            }

            Builder packageName(String packageName) {
                this.packageName = packageName;
                return this;
            }

            Builder className(String className) {
                this.className = className;
                return this;
            }

            Builder imports(List<String> imports) {
                this.imports = imports;
                return this;
            }

            Builder properties(Collection<SourceClassPropertyData> properties) {
                this.properties = properties;
                return this;
            }

        }

    }

    public static class PropertyTypeDeclaration {
        /**
         * Literal text to be used in the type declaration
         */
        public StringBuilder fieldDeclarationLiteral;
        /**
         * List of qualified names of the types used in this declaration
         */
        public List<String> declaredTypesQualifiedNames;

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            StringBuilder fieldDeclarationLiteral;
            List<String> declaredTypesQualifiedNames;

            public Builder fieldDeclarationLiteral(StringBuilder fieldDeclarationLiteral) {
                this.fieldDeclarationLiteral = fieldDeclarationLiteral;
                return this;
            }

            public Builder declaredTypesQualifiedNames(List<String> declaredTypesQualifiedNames) {
                this.declaredTypesQualifiedNames = declaredTypesQualifiedNames;
                return this;
            }

            public PropertyTypeDeclaration build() {
                PropertyTypeDeclaration result = new PropertyTypeDeclaration();
                result.fieldDeclarationLiteral = this.fieldDeclarationLiteral;
                result.declaredTypesQualifiedNames = this.declaredTypesQualifiedNames;
                return result;
            }

        }
    }

}
