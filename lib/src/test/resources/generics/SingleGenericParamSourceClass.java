package solutions.sulfura.gend.dtos;

import java.util.Set;
import solutions.sulfura.gend.dtos.annotations.Dto;

/**This class is used as input for the DTO generator test */
@Dto
public class SingleGenericParamSourceClass<T> {

    public T genericProperty;
    public Set<T> nestedGenericProperty;
    T genericPropertyWithGetter;
    T genericPropertyWithSetter;

    public T getGenericPropertyWithGetter(){ return genericPropertyWithGetter; }

    public void setGenericPropertyWithSetter(T genericPropertyWithSetter){ this.genericPropertyWithSetter = genericPropertyWithSetter; }

}
