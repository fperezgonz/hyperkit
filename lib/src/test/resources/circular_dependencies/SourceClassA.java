package solutions.sulfura.gend.dtos.circular_dependencies;

import solutions.sulfura.gend.dtos.annotations.Dto;

/**This class is used as input for the DTO generator test */
@Dto
public class SourceClassA {

    SourceClassB property;
    SourceClassB[] propertyArray;
    List<SourceClassB> genericProperty;
    List<SourceClassB>[] genericPropertyArray;

}
