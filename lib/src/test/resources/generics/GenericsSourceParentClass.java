package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.Dto;

/**This class is used as input for the DTO generator test */
@Dto
public class GenericsSourceParentClass<T> {

    public <T> genericProperty;
    <T> genericPropertyWithGetter;
    <T> genericPropertyWithSetter;

    public <T> getGenericPropertyWithGetter(){ return genericPropertyWithGetter; }

    public void setGenericPropertyWithSetter(<T> genericPropertyWithSetter){ this.genericPropertyWithSetter = genericPropertyWithSetter; }

}
