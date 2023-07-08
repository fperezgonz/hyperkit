package solutions.sulfura.gend.dtos.circular_dependencies;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA;
import java.util.List;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto;

@DtoFor(SourceClassA.class)
public class SourceClassADto implements Dto<SourceClassA>{

    public Option<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassBDto>> propertyArray;
    public Option<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB> property;
    public Option<List<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB>>> genericPropertyArray;
    public Option<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB>> genericProperty;

    public SourceClassADto(){}

}