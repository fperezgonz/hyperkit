package solutions.sulfura.gend.dtos.annotation_processor;

import solutions.sulfura.gend.dtos.annotation_processor.AnnotationProcessorUtils.PropertyTypeDeclaration;
import solutions.sulfura.gend.dtos.conf.DtoConf;

import javax.lang.model.element.Name;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DtoCodeGenUtils {

    StringBuilder stringBuilder = new StringBuilder();
    public Map<String, String> importsAliasedTypes_qualifiedTypes = new HashMap<>();
    public String contextIndentation = "";

    public DtoCodeGenUtils() {
    }

    public DtoCodeGenUtils append(CharSequence string) {
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

    private String aliasedNameFromQualifiedName(String importQualifiedType) {
        return importQualifiedType.substring(importQualifiedType.lastIndexOf('.') + 1);
    }

    public DtoCodeGenUtils addImport(String importQualifiedType) {

        String aliasedName = aliasedNameFromQualifiedName(importQualifiedType);

        //TODO find a better way to deal with these two
        if (aliasedName.equals("Builder") || aliasedName.equals("Conf")) {
            return this;
        }

        //If the type has already been imported, skip
        if (importsAliasedTypes_qualifiedTypes.containsKey(aliasedName)) {
            return this;
        }

        importsAliasedTypes_qualifiedTypes.put(aliasedName, importQualifiedType);

        stringBuilder.append("import ")
                .append(importQualifiedType)
                .append(";\n");

        return this;

    }

    public DtoCodeGenUtils addConstructor(String className) {

        append('\n')
                .append("    public ")
                .append(className)
                .append("(){}\n\n");

        return this;

    }

    private String getTypeDeclarationString(DtoPropertyData fieldData) {

        String typeDeclarationString = fieldData.typeDeclaration.fieldDeclarationLiteral.toString();

        for (String propertyQualifiedName : fieldData.typeDeclaration.declaredTypesQualifiedNames) {
            String qualifiedNameForAlias = importsAliasedTypes_qualifiedTypes.get(aliasedNameFromQualifiedName(propertyQualifiedName));
            if (Objects.equals(qualifiedNameForAlias, propertyQualifiedName)) {
                typeDeclarationString = typeDeclarationString.replace(propertyQualifiedName, aliasedNameFromQualifiedName(propertyQualifiedName));
            }
        }

        return typeDeclarationString;

    }

    public DtoCodeGenUtils addFieldDeclaration(DtoPropertyData fieldData) {

        String typeDeclarationString = getTypeDeclarationString(fieldData);

        stringBuilder.append(contextIndentation)
                .append("public ");

        stringBuilder.append(typeDeclarationString);

        stringBuilder.append(' ')
                .append(fieldData.propertyName)
                .append(";\n");

        return this;
    }

    public DtoCodeGenUtils addConfigClass(CharSequence baseClassName, CharSequence genericTypeArgs, List<DtoPropertyData> propertyDataList) {

        //Conf class declaration
        StringBuilder confClassDeclaration = new StringBuilder("public static class Conf");

        if (genericTypeArgs != null) {
            confClassDeclaration.append(genericTypeArgs);
        }

        confClassDeclaration.append(" extends ")
                .append(DtoConf.class.getSimpleName())
                .append('<')
                .append(baseClassName);

        if (genericTypeArgs != null) {
            confClassDeclaration.append(genericTypeArgs);
        }

        confClassDeclaration.append('>');

        beginClass(confClassDeclaration);

        //Conf fields
        for (DtoPropertyData propertyData : propertyDataList) {
            addFieldDeclaration(propertyData);
        }

        addConstructor("Conf");

        addBuilder(baseClassName + ".Conf", genericTypeArgs, propertyDataList);

        append('\n')
                .endClass()
                .append("\n\n");

        return this;
    }

    public DtoCodeGenUtils beginClass(CharSequence classDeclaration) {
        stringBuilder.append(contextIndentation)
                .append(classDeclaration)
                .append("{\n\n");
        increaseIndent();
        return this;
    }

    public DtoCodeGenUtils endClass() {
        decreaseIndent();
        stringBuilder.append(contextIndentation)
                .append("}");
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

    public DtoCodeGenUtils addBuilder(CharSequence baseClassName, CharSequence genericTypeArgs, List<DtoPropertyData> propertyDataList) {

        //Builder class declaration
        StringBuilder builderClassDeclaration = new StringBuilder("public static class Builder");

        if (genericTypeArgs != null) {
            builderClassDeclaration.append(genericTypeArgs);
        }

        beginClass(builderClassDeclaration);

        //Builder fields
        for (DtoPropertyData propertyData : propertyDataList) {
            addFieldDeclaration(propertyData);
        }

        append('\n');

        //Method to get a builder from the main class
        append(contextIndentation)
                .append("public static ");

        if (genericTypeArgs != null) {
            append(genericTypeArgs);
        }

        append(" Builder");

        if (genericTypeArgs != null) {
            append(genericTypeArgs);
        }

        append(" newInstance(){\n");

        if (genericTypeArgs != null) {
            append(contextIndentation).append("    return new Builder<>();\n");
        } else {
            append(contextIndentation).append("    return new Builder();\n");
        }

        append(contextIndentation).append("}\n\n");

        //Builder property methods
        for (DtoPropertyData propertyData : propertyDataList) {
            addBuilderProperty(propertyData, genericTypeArgs);
        }

        //build method
        addBuildMethod(baseClassName, propertyDataList.stream()
                .map(propertyData -> propertyData.propertyName)
                .collect(Collectors.toList()), genericTypeArgs);

        endClass().append("\n\n");

        return this;

    }

    public DtoCodeGenUtils addBuilderProperty(DtoPropertyData propertyData, CharSequence genericTypeArguments) {

        String typeDeclarationString = getTypeDeclarationString(propertyData);

        stringBuilder.append(contextIndentation).append("public Builder");
        if (genericTypeArguments != null) {
            stringBuilder.append(genericTypeArguments);
        }
        stringBuilder.append(' ')
                .append(propertyData.propertyName)
                .append('(').append(typeDeclarationString).append(' ')
                .append(propertyData.propertyName)
                .append("){\n")
                .append(contextIndentation).append("    this.").append(propertyData.propertyName)
                .append(" = ").append(propertyData.propertyName).append(";\n")
                .append(contextIndentation).append("    return this;\n")
                .append(contextIndentation).append("}\n\n");

        return this;
    }

    public DtoCodeGenUtils addBuildMethod(CharSequence builderSourceClass, List<String> propertyNames, CharSequence genericTypeArguments) {

        stringBuilder.append(contextIndentation)
                .append("public ").append(builderSourceClass);
        if (genericTypeArguments != null) {
            stringBuilder.append(genericTypeArguments);
        }
        stringBuilder.append(" build(){\n");
        increaseIndent();

        stringBuilder.append(contextIndentation)
                .append(builderSourceClass).append(" instance = new ")
                .append(builderSourceClass).append("();\n");

        for (String propertyName : propertyNames) {
            stringBuilder.append(contextIndentation)
                    .append("instance.").append(propertyName)
                    .append(" = ").append(propertyName).append(";\n");
        }

        append(contextIndentation).append("return instance;\n");

        decreaseIndent();
        stringBuilder.append(contextIndentation).append("}\n\n");

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
