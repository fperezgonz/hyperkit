package solutions.sulfura.gend.dtos;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.SourceClassGetterSetter;
import java.lang.String;

@DtoFor(SourceClassGetterSetter.class)
public class SourceClassGetterSetterDto implements Dto<SourceClassGetterSetter>{

    public Option<String> stringPropertyWithGetter;
    public Option<String> stringPropertyWithSetter;
    public Option<String> stringPropertyWithGetterAndSetter;

    public SourceClassGetterSetterDto(){}

 }