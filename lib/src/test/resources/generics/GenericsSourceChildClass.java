package solutions.sulfura.gend.dtos;

import solutions.sulfura.gend.dtos.annotations.Dto;

/**This class is used as input for the DTO generator test */
@Dto
public class GenericsSourceChildClass<String> extends GenericsSourceChildClass {

    public String genericProperty;
    String genericPropertyWithGetter;
    String genericPropertyWithSetter;

    public String getGenericPropertyWithGetter(){ return genericPropertyWithGetter; }

    public void setGenericPropertyWithSetter(String genericPropertyWithSetter){ this.genericPropertyWithSetter = genericPropertyWithSetter; }

}
