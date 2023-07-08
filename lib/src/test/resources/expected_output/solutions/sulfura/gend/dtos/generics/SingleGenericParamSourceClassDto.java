package solutions.sulfura.gend.dtos.generics;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.generics.SingleGenericParamSourceClass;
import java.util.Set;

@DtoFor(SingleGenericParamSourceClass.class)
public class SingleGenericParamSourceClassDto<T> implements Dto<SingleGenericParamSourceClass>{

    public Option<T> genericPropertyWithSetter;
    public Option<T> genericProperty;
    public Option<T> genericPropertyWithGetter;
    public Option<Set<T>> nestedGenericProperty;

    public SingleGenericParamSourceClassDto(){}

 }