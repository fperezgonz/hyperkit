package solutions.sulfura.gend.dtos;

import java.util.Set;
import solutions.sulfura.gend.dtos.annotations.Dto;

/**This class is used as a superclass for the DTO generator test */
@Dto
public class GenericSuperClassSource<T> {

    public T inheritedGenericProperty;
    public Set<T> inheritedNestedGenericProperty;

    public T overlappingGenericProperty;
    public Set<T> overlappingNestedGenericProperty;

    T inheritedGenericPropertyWithGetter;
    T inheritedGenericPropertyWithSetter;

    public T getInheritedGenericPropertyWithGetter(){ return inheritedGenericPropertyWithGetter; }

    public void setInheritedGenericPropertyWithSetter(T inheritedGenericPropertyWithSetter){ this.inheritedGenericPropertyWithSetter = inheritedGenericPropertyWithSetter; }

}
