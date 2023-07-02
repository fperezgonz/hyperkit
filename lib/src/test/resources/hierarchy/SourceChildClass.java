package solutions.sulfura.gend.dtos.hierarchy;

import solutions.sulfura.gend.dtos.annotations.Dto;

/**This class is used as input for the DTO generator test */
@Dto
public class SourceChildClass extends SourceParentClass{

    public String overlappingProperty;
    public String childProperty;
    String childPropertyWithGetter;
    String childPropertyWithSetter;

    public String getChildPropertyWithGetter(){ return childPropertyWithGetter; }

    public void setChildPropertyWithSetter(String childPropertyWithSetter){ this.childPropertyWithSetter = childPropertyWithSetter; }

}
