package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.Dto;
import solutions.sulfura.gend.dtos.annotations.DtoProperty;
import java.lang.Deprecated;

/**
 * This class is used as input for the DTO generator test
 */
@Dto(include = {DtoProperty.class, Deprecated.class})
public class SourceClassWithIncluded {

    /**This one should not be included in the generated Dto*/
    public String ignoredStringPropertyCustomAnnotation;
    /**This one should not be included in the generated Dto*/
    public String ignoredStringProperty;
    @DtoProperty public String stringProperty;
    @Deprecated public String stringPropertyWithCustomAnnotation;
    String stringPropertyWithGetter;
    public String ignoredStringPropertyWithGetterAndSetter;
    String stringPropertyWithSetter;
    String stringPropertyWithSetterAndCustomAnnotation;

    public SourceClassWithIncluded(String data) {
    }

    public String getIgnoredStringProperty() {
        return ignoredStringProperty;
    }

    @DtoProperty
    public String getStringPropertyWithGetter() {
        return stringPropertyWithGetter;
    }

    public void setStringPropertyWithGetter(String stringPropertyWithGetter) {
        this.stringPropertyWithGetter = stringPropertyWithGetter;
    }

    public String getIgnoredStringPropertyWithGetterAndSetter() {
        return ignoredStringPropertyWithGetterAndSetter;
    }
    public void setIgnoredStringPropertyWithGetterAndSetter(String ignoredStringPropertyWithGetterAndSetter) {
        this.ignoredStringPropertyWithGetterAndSetter = ignoredStringPropertyWithGetterAndSetter;
    }

    @DtoProperty
    public void setStringPropertyWithSetter(String stringPropertyWithSetter) {
        this.stringPropertyWithSetter = stringPropertyWithSetter;
    }

    @Deprecated
    public void setStringPropertyWithSetterAndCustomAnnotation(String stringPropertyWithSetterAndCustomAnnotation) {
        this.stringPropertyWithSetterAndCustomAnnotation = stringPropertyWithSetterAndCustomAnnotation;
    }

}
