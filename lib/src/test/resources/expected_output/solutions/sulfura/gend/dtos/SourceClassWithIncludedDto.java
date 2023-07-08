package solutions.sulfura.gend.dtos;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.SourceClassWithIncluded;
import java.lang.String;

@DtoFor(SourceClassWithIncluded.class)
public class SourceClassWithIncludedDto implements Dto<SourceClassWithIncluded>{

    public Option<String> stringPropertyWithGetter;
    public Option<String> stringPropertyWithCustomAnnotation;
    public Option<String> stringPropertyWithSetter;
    public Option<String> stringProperty;
    public Option<String> stringPropertyWithSetterAndCustomAnnotation;

    public SourceClassWithIncludedDto(){}

 }