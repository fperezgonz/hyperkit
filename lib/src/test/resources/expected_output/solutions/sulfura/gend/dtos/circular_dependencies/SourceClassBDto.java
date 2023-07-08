package solutions.sulfura.gend.dtos.circular_dependencies;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassB;
import java.util.List;
import solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto;

@DtoFor(SourceClassB.class)
public class SourceClassBDto implements Dto<SourceClassB>{

    public Option<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassADto>> propertyArray;
    public Option<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA> property;
    public Option<List<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA>>> genericPropertyArray;
    public Option<List<solutions.sulfura.gend.dtos.circular_dependencies.SourceClassA>> genericProperty;

    public SourceClassBDto(){}

}