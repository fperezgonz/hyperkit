package solutions.sulfura.gend.dtos.generics.inheritance;

import solutions.sulfura.gend.dtos.annotations.Dto;

import solutions.sulfura.gend.dtos.GenericSuperClassSource;
import java.util.Set;

/**This class is used as input for the DTO generator test */
@Dto
public class GenericChildClassWithParameterizedType extends GenericSuperClassSource<String> {

    public String genericProperty;
    String genericPropertyWithGetter;
    String genericPropertyWithSetter;
    public Set<String> nestedGenericProperty;

    public String getGenericPropertyWithGetter(){ return genericPropertyWithGetter; }

    public void setGenericPropertyWithSetter(String genericPropertyWithSetter){ this.genericPropertyWithSetter = genericPropertyWithSetter; }

}
