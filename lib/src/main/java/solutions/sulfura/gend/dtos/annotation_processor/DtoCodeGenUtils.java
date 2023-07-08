package solutions.sulfura.gend.dtos.annotation_processor;

import solutions.sulfura.gend.dtos.annotation_processor.AnnotationProcessorUtils.PropertyTypeDeclaration;

import javax.lang.model.element.Name;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DtoCodeGenUtils {

    StringBuilder stringBuilder = new StringBuilder();
    public Map<String, String> importsSimpleTypes_qualifiedTypes = new HashMap<>();
    public String contextIndentation = "";

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

    private String getTypeDeclarationString(DtoPropertyData fieldData) {

        String typeDeclarationString = fieldData.typeDeclaration.fieldDeclarationLiteral.toString();

        for (String propertyQualifiedName : fieldData.typeDeclaration.declaredTypesQualifiedNames) {
            String qualifiedNameForAlias = importsSimpleTypes_qualifiedTypes.get(propertyQualifiedName.substring(propertyQualifiedName.lastIndexOf('.') + 1));
            if (Objects.equals(qualifiedNameForAlias, propertyQualifiedName)) {
                typeDeclarationString = typeDeclarationString.replace(propertyQualifiedName, propertyQualifiedName.substring(propertyQualifiedName.lastIndexOf('.') + 1));
            }
        }

        return typeDeclarationString;

    }

    public DtoCodeGenUtils addFieldDeclaration(DtoPropertyData fieldData) {

        String typeDeclarationString = getTypeDeclarationString(fieldData);

        stringBuilder.append(contextIndentation)
                .append("public Option<")
                .append(typeDeclarationString).append("> ")
                .append(fieldData.propertyName)
                .append(";\n");

        return this;
    }

    public DtoCodeGenUtils addBuilder(String baseClassName, List<DtoPropertyData> propertyDataList) {
        append(contextIndentation)
                .append("public Builder builder(){\n")
                .append(contextIndentation).append("    ")
                .append("return new Builder();\n")
                .append(contextIndentation).append("}\n");

        beginClass("Builder")
                .append(contextIndentation)
                .append(baseClassName).append(" instance;");

        return this;

    }

    public DtoCodeGenUtils beginClass(String classDeclaration) {
        stringBuilder.append(contextIndentation)
                .append(classDeclaration)
                .append("{\n\n");
        increaseIndent();
        return this;
    }

    public DtoCodeGenUtils endClass() {
        decreaseIndent();
        stringBuilder.append(contextIndentation)
                .append('}');
        return this;
    }

    public DtoCodeGenUtils addBuilderField(DtoPropertyData fieldData) {
        addFieldDeclaration(fieldData);
        return this;
    }

    public DtoCodeGenUtils addBuilderProperty(DtoPropertyData propertyData) {

        String typeDeclarationString = getTypeDeclarationString(propertyData);

        stringBuilder.append(contextIndentation).append("public Builder ")
                .append(propertyData.propertyName)
                .append('(')
                .append("Option<").append(typeDeclarationString).append("> ")
                .append(propertyData.propertyName)
                .append("){\n")
                .append(contextIndentation).append("    this.").append(propertyData.propertyName)
                .append(" = ").append(propertyData.propertyName).append(";\n")
                .append(contextIndentation).append("    }\n");

        return this;
    }

    public DtoCodeGenUtils addBuildMethod(String builderSourceClass, List<String> propertyNames) {
        stringBuilder.append(contextIndentation)
                .append("public ").append(builderSourceClass).append("build(){\n")
                .append(contextIndentation)
                .append(builderSourceClass).append(" this.instance = new ")
                .append(builderSourceClass).append("();\n");
        for (String propertyName : propertyNames) {
            stringBuilder.append(contextIndentation)
                    .append("this.").append(propertyName)
                    .append(" = ").append(propertyName).append(";\n");
        }
        stringBuilder.append(contextIndentation).append("}\n");
        return this;
    }

    private DtoCodeGenUtils increaseIndent() {
        contextIndentation += "    ";
        return this;
    }

    private DtoCodeGenUtils decreaseIndent() {
        contextIndentation = contextIndentation.substring(0, contextIndentation.length() - 4);
        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }


    public static class DtoPropertyData {

        public DtoPropertyData() {
        }

        PropertyTypeDeclaration typeDeclaration;
        String propertyName;

        public static DtoCodeGenUtils.DtoPropertyData.Builder builder() {
            return new DtoCodeGenUtils.DtoPropertyData.Builder();
        }

        public static class Builder {
            PropertyTypeDeclaration typeDeclaration;
            String propertyName;

            public Builder() {
            }

            public Builder typeDeclaration(PropertyTypeDeclaration typeDeclaration) {
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
