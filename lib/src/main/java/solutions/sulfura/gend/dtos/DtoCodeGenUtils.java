package solutions.sulfura.gend.dtos;

import javax.lang.model.element.Name;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DtoCodeGenUtils {

    StringBuilder stringBuilder = new StringBuilder();
    public Map<String, String> importsSimpleTypes_qualifiedTypes = new HashMap<>();

    public DtoCodeGenUtils() {
    }

    public DtoCodeGenUtils append(String string) {
        stringBuilder.append(string);
        return this;
    }

    public DtoCodeGenUtils append(char character) {
        stringBuilder.append(character);
        return this;
    }

    public DtoCodeGenUtils append(Name character) {
        stringBuilder.append(character);
        return this;
    }

    public DtoCodeGenUtils addPackageDeclaration(String packageName) {
        stringBuilder.append("package ")
                .append(packageName)
                .append(";\n\n");
        return this;
    }

    public DtoCodeGenUtils addImport(String importQualifiedType) {
        importsSimpleTypes_qualifiedTypes.put(importQualifiedType.substring(importQualifiedType.lastIndexOf('.') + 1), importQualifiedType);
        stringBuilder.append("import ")
                .append(importQualifiedType)
                .append(";\n");
        return this;
    }

    public DtoCodeGenUtils addFieldDeclaration(DtoPropertyData fieldData) {
        String typeDeclarationString = fieldData.typeDeclaration.fieldDeclarationLiteral.toString();
        for (String propertyQualifiedName : fieldData.typeDeclaration.declaredTypesQualifiedNames) {

            String qualifiedNameForAlias = importsSimpleTypes_qualifiedTypes.get(propertyQualifiedName.substring(propertyQualifiedName.lastIndexOf('.') + 1));
            if (Objects.equals(qualifiedNameForAlias, propertyQualifiedName)) {
                typeDeclarationString = typeDeclarationString.replace(propertyQualifiedName, propertyQualifiedName.substring(propertyQualifiedName.lastIndexOf('.') + 1));
            }

        }

        stringBuilder.append("    public Option<")
                .append(typeDeclarationString).append("> ")
                .append(fieldData.propertyName)
                .append(";\n");

        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }


    public static class DtoPropertyData {

        public DtoPropertyData() {
        }

        DtoAnnotationProcessorUtils.PropertyTypeDeclaration typeDeclaration;
        String propertyName;

        public static DtoCodeGenUtils.DtoPropertyData.Builder builder() {
            return new DtoCodeGenUtils.DtoPropertyData.Builder();
        }

        public static class Builder {
            DtoAnnotationProcessorUtils.PropertyTypeDeclaration typeDeclaration;
            String propertyName;

            public Builder() {
            }

            public Builder typeDeclaration(DtoAnnotationProcessorUtils.PropertyTypeDeclaration typeDeclaration) {
                this.typeDeclaration = typeDeclaration;
                return this;
            }

            public Builder propertyName(String propertyName) {
                this.propertyName = propertyName;
                return this;
            }

            public DtoPropertyData build() {

                DtoPropertyData result = new DtoPropertyData();
                result.typeDeclaration = this.typeDeclaration;
                result.propertyName = this.propertyName;

                return result;

            }

        }

    }

}
