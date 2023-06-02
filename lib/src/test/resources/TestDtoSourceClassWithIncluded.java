package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.Dto;
import solutions.sulfura.gend.dtos.annotations.DtoProperty;

/**
 * This class is used as input for the DTO generator test
 */
@Dto(include = DtoProperty.class)
public class TestDtoSourceClassWithIncluded {

    public String ignoredStringProperty;
    @DtoProperty public String stringProperty;
    public String ignoredStringPropertyWithGetter;
    public String ignoredStringPropertyWithGetterAndSetter;
    public String ignoredStringPropertyWithSetter;

    public TestDtoSourceClassWithIncluded(String data) {
    }

    public String getIgnoredStringProperty() {
        return ignoredStringProperty;
    }

    @DtoProperty
    public String getIgnoredStringPropertyWithGetter() {
        return ignoredStringPropertyWithGetter;
    }

    public void setIgnoredStringPropertyWithGetter(String ignoredStringPropertyWithGetter) {
        this.ignoredStringPropertyWithGetter = ignoredStringPropertyWithGetter;
    }

    @DtoProperty
    public String getIgnoredStringPropertyWithGetterAndSetter() {
        return ignoredStringPropertyWithGetterAndSetter;
    }
    @DtoProperty
    public void setIgnoredStringPropertyWithGetterAndSetter(String ignoredStringPropertyWithGetterAndSetter) {
        this.ignoredStringPropertyWithGetterAndSetter = ignoredStringPropertyWithGetterAndSetter;
    }

    public String getIgnoredStringPropertyWithSetter() {
        return ignoredStringPropertyWithSetter;
    }
    @DtoProperty
    public void setIgnoredStringPropertyWithSetter(String ignoredStringPropertyWithSetter) {
        this.ignoredStringPropertyWithSetter = ignoredStringPropertyWithSetter;
    }

}
