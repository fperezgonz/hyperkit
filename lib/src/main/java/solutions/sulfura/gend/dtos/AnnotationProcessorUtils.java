package solutions.sulfura.gend.dtos;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.*;
import java.util.*;

public class AnnotationProcessorUtils {

    static String primitiveTypeToClassType(TypeMirror primitiveType) {
        String result = primitiveType.toString();

        return !primitiveType.getKind().isPrimitive() ? result
                : Objects.equals(result, "int") ? "Integer"
                : result.substring(0, 1).toUpperCase() + result.substring(1);
    }

    public static PropertyTypeDeclaration typeToPropertyTypeDeclaration(TypeMirror typeMirror) {
        PropertyTypeDeclaration.Builder fieldTypeDeclarationBuilder = PropertyTypeDeclaration.builder();
        String declaredTypeString;
        boolean isPrimitive = false;

        List<String> declaredTypesQualifiedNames = new ArrayList<>();

        //Use lists instead of arrays
        if (typeMirror.getKind() == TypeKind.ARRAY) {
            declaredTypeString = "java.util.List<" + primitiveTypeToClassType(((ArrayType) typeMirror).getComponentType()) + ">";
            declaredTypesQualifiedNames.add("java.util.List");
        } else {
            isPrimitive = typeMirror.getKind().isPrimitive();
            declaredTypeString = typeMirror.toString();
            if (typeMirror.getKind() == TypeKind.DECLARED) {
                declaredTypesQualifiedNames.add(((DeclaredType)typeMirror).asElement().toString());
            }
        }


        StringBuilder stringBuilder = new StringBuilder();

        if (isPrimitive) {
            stringBuilder.append(primitiveTypeToClassType(typeMirror));
        } else {
            stringBuilder.append(declaredTypeString);
        }

        if (typeMirror instanceof DeclaredType) {
            for (TypeMirror typeArg : ((DeclaredType) typeMirror).getTypeArguments()) {
                if (typeArg.getKind() == TypeKind.DECLARED) {
                    declaredTypesQualifiedNames.add(((DeclaredType)typeMirror).asElement().toString());
                }
            }
        }

        return fieldTypeDeclarationBuilder
                .fieldDeclarationLiteral(stringBuilder)
                .declaredTypesQualifiedNames(declaredTypesQualifiedNames)
                .build();
    }

    public static class DtoPropertyData {

        public DtoPropertyData() {
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

            public DtoPropertyData build() {

                DtoPropertyData result = new DtoPropertyData();
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
        Collection<DtoPropertyData> properties;

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            String packageName;
            List<String> imports;
            String className;
            Collection<DtoPropertyData> properties;

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

            Builder properties(Collection<DtoPropertyData> properties) {
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

    public static String getElementPackageName(TypeElement element) {

        Element parentElement = element.getEnclosingElement();

        while (parentElement.getKind() != ElementKind.PACKAGE) {
            parentElement = parentElement.getEnclosingElement();
        }

        return parentElement.toString();
    }
}
