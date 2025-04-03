package solutions.sulfura.hyperkit.dtos;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty;

/**
 * This class is used as input for the DTO generator test to verify boolean getters use "is" prefix
 */
@Dto
public class SourceClassBooleanGetterSetter {

    private boolean booleanPropertyWithGetter;
    private Boolean boxedBooleanPropertyWithGetter;
    private boolean booleanPropertyWithGetterAndSetter;
    private Boolean boxedBooleanPropertyWithGetterAndSetter;

    @DtoProperty(createGetter = true)
    public boolean isBooleanPropertyWithGetter() {
        return booleanPropertyWithGetter;
    }

    @DtoProperty(createGetter = true)
    public Boolean isBoxedBooleanPropertyWithGetter() {
        return boxedBooleanPropertyWithGetter;
    }

    @DtoProperty(createGetter = true, createSetter = true)
    public boolean isBooleanPropertyWithGetterAndSetter() {
        return booleanPropertyWithGetterAndSetter;
    }

    @DtoProperty(createSetter = true)
    public void setBooleanPropertyWithGetterAndSetter(boolean booleanPropertyWithGetterAndSetter) {
        this.booleanPropertyWithGetterAndSetter = booleanPropertyWithGetterAndSetter;
    }

    @DtoProperty(createGetter = true, createSetter = true)
    public Boolean isBoxedBooleanPropertyWithGetterAndSetter() {
        return boxedBooleanPropertyWithGetterAndSetter;
    }

    @DtoProperty(createSetter = true)
    public void setBoxedBooleanPropertyWithGetterAndSetter(Boolean boxedBooleanPropertyWithGetterAndSetter) {
        this.boxedBooleanPropertyWithGetterAndSetter = boxedBooleanPropertyWithGetterAndSetter;
    }
}