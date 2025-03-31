package solutions.sulfura.hyperkit.dsl.projections.test_aux.dto_sources.circular_dependencies;

import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import java.util.List;

/**This class is used as input for the DTO generator test */
@SuppressWarnings("unused")
@Dto
public class SourceClassA {

    public SourceClassB property;
    public SourceClassB[] propertyArray;
    public List<SourceClassB> genericProperty;
    public List<SourceClassB>[] genericPropertyArray;

}
