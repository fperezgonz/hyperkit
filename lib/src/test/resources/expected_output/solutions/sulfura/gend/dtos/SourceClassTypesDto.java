package solutions.sulfura.gend.dtos;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.SourceClassTypes;
import java.util.List;
import java.lang.String;

@DtoFor(SourceClassTypes.class)
public class SourceClassTypesDto implements Dto<SourceClassTypes>{

    public Option<List<java.lang.String>> stringArrayProperty;
    public Option<Boolean> booleanProperty;
    public Option<Double> doubleProperty;
    public Option<Long> longProperty;
    public Option<List<Boolean>> booleanArrayProperty;
    public Option<String> stringProperty;

    public SourceClassTypesDto(){}

 }