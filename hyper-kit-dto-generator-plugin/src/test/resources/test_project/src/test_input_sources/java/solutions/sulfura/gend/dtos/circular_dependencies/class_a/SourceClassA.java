package solutions.sulfura.gend.dtos.circular_dependencies.class_a;

import solutions.sulfura.gend.dtos.circular_dependencies.class_b.SourceClassB;
import solutions.sulfura.gend.dtos.annotations.Dto;
import java.util.List;

/**This class is used as input for the DTO generator test */
@Dto(destPackageName = "solutions.sulfura.gend.dtos.circular_dependencies.class_a")
public class SourceClassA {

    public SourceClassB property;
    public SourceClassB[] propertyArray;
    public List<SourceClassB> genericProperty;
    public List<SourceClassB>[] genericPropertyArray;

}
