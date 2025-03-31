package solutions.sulfura.hyperkit.dtos.generics;

import java.util.Set;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;

/**This class is used as input for the DTO generator test */
@Dto(destPackageName = "solutions.sulfura.hyperkit.dtos.generics")
public class SingleGenericParamSourceClass<T> {

    public T genericProperty;
    public Set<T> nestedGenericProperty;
    T genericPropertyWithGetter;
    T genericPropertyWithSetter;

    public T getGenericPropertyWithGetter(){ return genericPropertyWithGetter; }

    public void setGenericPropertyWithSetter(T genericPropertyWithSetter){ this.genericPropertyWithSetter = genericPropertyWithSetter; }

}
