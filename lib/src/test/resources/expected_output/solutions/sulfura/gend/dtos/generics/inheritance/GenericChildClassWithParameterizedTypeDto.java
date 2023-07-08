package solutions.sulfura.gend.dtos.generics.inheritance;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.generics.inheritance.GenericChildClassWithParameterizedType;
import java.lang.String;
import java.util.Set;

@DtoFor(GenericChildClassWithParameterizedType.class)
public class GenericChildClassWithParameterizedTypeDto implements Dto<GenericChildClassWithParameterizedType>{

    public Option<String> overlappingGenericProperty;
    public Option<String> inheritedGenericPropertyWithGetter;
    public Option<String> inheritedGenericProperty;
    public Option<Set<String>> inheritedNestedGenericProperty;
    public Option<Set<String>> overlappingNestedGenericProperty;
    public Option<String> inheritedGenericPropertyWithSetter;
    public Option<String> genericPropertyWithSetter;
    public Option<String> genericProperty;
    public Option<String> genericPropertyWithGetter;
    public Option<Set<String>> nestedGenericProperty;

    public GenericChildClassWithParameterizedTypeDto(){}

}