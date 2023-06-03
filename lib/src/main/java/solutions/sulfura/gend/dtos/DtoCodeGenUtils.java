package solutions.sulfura.gend.dtos;

import javax.lang.model.element.Name;
import java.util.Objects;

public class DtoCodeGenUtils {

    StringBuilder stringBuilder = new StringBuilder();

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

    public DtoCodeGenUtils addFieldDeclaration(DtoPropertyData fieldData) {

        stringBuilder.append("    public Option<")
                .append(fieldData.typeDeclaration).append("> ")
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

        String typeDeclaration;
        String propertyName;

        public static DtoCodeGenUtils.DtoPropertyData.Builder builder() {
            return new DtoCodeGenUtils.DtoPropertyData.Builder();
        }

        public static class Builder {
            String typeDeclaration;
            String propertyName;

            public Builder() {
            }

            public Builder typeDeclaration(String typeDeclaration) {
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
