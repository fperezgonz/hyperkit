package solutions.sulfura.gend.dtos.hierarchy;

import solutions.sulfura.gend.dtos.annotations.Dto;

/**This class is used as input for the DTO generator test */
@Dto
public class SourceParentClass {

    public String overlappingProperty;
    public String parentProperty;
    String parentPropertyWithGetter;
    String parentPropertyWithSetter;

    public String getParentPropertyWithGetter(){ return parentPropertyWithGetter; }

    public void setParentPropertyWithSetter(String parentPropertyWithSetter){ this.parentPropertyWithSetter = parentPropertyWithSetter; }

}
