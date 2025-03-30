package solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies;

import io.vavr.control.Option;
import solutions.sulfura.gend.dsl.projections.test_aux.dto_sources.circular_dependencies.SourceClassA;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.ListOperation;
import solutions.sulfura.gend.dtos.annotations.DtoFor;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.projection.DtoProjectionException;
import solutions.sulfura.gend.dtos.projection.ProjectionFor;
import solutions.sulfura.gend.dtos.projection.ProjectionUtils;
import solutions.sulfura.gend.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.gend.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf;

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

        public DtoListFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> propertyArray;
        public DtoFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> property;
        public ListFieldConf genericPropertyArray;
        public DtoListFieldConf<solutions.sulfura.gend.dsl.projections.test_aux.circular_dependencies.SourceClassBDto.Projection> genericProperty;

        public Projection(){}

        public void applyProjectionTo(SourceClassADto dto) throws DtoProjectionException {
            dto.propertyArray = ProjectionUtils.getProjectedValue(dto.propertyArray, this.propertyArray);
            dto.property = ProjectionUtils.getProjectedValue(dto.property, this.property);
            dto.genericPropertyArray = ProjectionUtils.getProjectedValue(dto.genericPropertyArray, this.genericPropertyArray);
            dto.genericProperty = ProjectionUtils.getProjectedValue(dto.genericProperty, this.genericProperty);
        }


    }

}