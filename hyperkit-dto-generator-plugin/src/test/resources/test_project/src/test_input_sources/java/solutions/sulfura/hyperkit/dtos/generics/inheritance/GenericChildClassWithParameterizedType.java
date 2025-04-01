package solutions.sulfura.hyperkit.dtos.generics.inheritance;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;

import solutions.sulfura.hyperkit.dtos.aux_classes.GenericSuperClassSource;
import java.util.Set;

/**This class is used as input for the DTO generator test */
@Dto(destPackageName = "solutions.sulfura.hyperkit.dtos.generics.inheritance")
public class GenericChildClassWithParameterizedType extends GenericSuperClassSource<String> {

    public String genericProperty;
    String genericPropertyWithGetter;
    String genericPropertyWithSetter;
    public Set<String> nestedGenericProperty;

    public String getGenericPropertyWithGetter(){ return genericPropertyWithGetter; }

    public void setGenericPropertyWithSetter(String genericPropertyWithSetter){ this.genericPropertyWithSetter = genericPropertyWithSetter; }

}
