package solutions.sulfura.hyperkit.dsl.projections.test_aux.circular_dependencies;

import io.vavr.control.Option;
import solutions.sulfura.hyperkit.dsl.projections.test_aux.dto_sources.circular_dependencies.SourceClassA;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionFor;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.ListFieldConf;

import java.util.List;

@DtoFor(SourceClassA.class)
public class SourceClassADto implements Dto<SourceClassA>{

    public Option<List<ListOperation<SourceClassBDto>>> propertyArray = Option.none();
    public Option<SourceClassBDto> property = Option.none();
    public Option<List<ListOperation<List<ListOperation<SourceClassBDto>>>>> genericPropertyArray = Option.none();
    public Option<List<ListOperation<SourceClassBDto>>> genericProperty = Option.none();

    public SourceClassADto(){}

    public Class<SourceClassA> getSourceClass() {
        return SourceClassA.class;
    }

    @ProjectionFor(SourceClassADto.class)
    public static class Projection extends DtoProjection<SourceClassADto>{

        public DtoListFieldConf<solutions.sulfura.hyperkit.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> propertyArray;
        public DtoFieldConf<solutions.sulfura.hyperkit.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> property;
        public ListFieldConf genericPropertyArray;
        public DtoListFieldConf<solutions.sulfura.hyperkit.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> genericProperty;

        public Projection(){}

        public void applyProjectionTo(SourceClassADto dto) throws DtoProjectionException {
            dto.propertyArray = ProjectionUtils.getProjectedValue(dto.propertyArray, this.propertyArray);
            dto.property = ProjectionUtils.getProjectedValue(dto.property, this.property);
            dto.genericPropertyArray = ProjectionUtils.getProjectedValue(dto.genericPropertyArray, this.genericPropertyArray);
            dto.genericProperty = ProjectionUtils.getProjectedValue(dto.genericProperty, this.genericProperty);
        }


    }

}