package solutions.sulfura.gend.dtos.circular_dependencies;

import solutions.sulfura.gend.dtos.annotations.Dto;

/**This class is used as input for the DTO generator test */
@Dto
public class SourceClassB {

    SourceClassA property;
    SourceClassA[] propertyArray;
    List<SourceClassA> genericProperty;
    List<SourceClassA>[] genericPropertyArray;

}
