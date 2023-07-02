package solutions.sulfura.gend.dtos.circular_dependencies;

import solutions.sulfura.gend.dtos.annotations.Dto;
import java.util.List;

/**This class is used as input for the DTO generator test */
@Dto
public class SourceClassA {

    public SourceClassB property;
    public SourceClassB[] propertyArray;
    public List<SourceClassB> genericProperty;
    public List<SourceClassB>[] genericPropertyArray;

}
