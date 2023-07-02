package solutions.sulfura.gend.dtos.circular_dependencies;

import solutions.sulfura.gend.dtos.annotations.Dto;
import java.util.List;

/**This class is used as input for the DTO generator test */
@Dto
public class SourceClassB {

    public SourceClassA property;
    public SourceClassA[] propertyArray;
    public List<SourceClassA> genericProperty;
    public List<SourceClassA>[] genericPropertyArray;

}
