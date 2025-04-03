package solutions.sulfura.hyperkit.dsl.projections.test_aux.circular_dependencies;

import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dsl.projections.test_aux.dto_sources.circular_dependencies.SourceClassB;
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

@DtoFor(SourceClassB.class)
public class SourceClassBDto implements Dto<SourceClassB> {

    public ValueWrapper<List<ListOperation<SourceClassADto>>> propertyArray = ValueWrapper.empty();
    public ValueWrapper<SourceClassADto> property = ValueWrapper.empty();
    public ValueWrapper<List<ListOperation<List<ListOperation<SourceClassADto>>>>> genericPropertyArray = ValueWrapper.empty();
    public ValueWrapper<List<ListOperation<SourceClassADto>>> genericProperty = ValueWrapper.empty();

    public SourceClassBDto(){}

    public Class<SourceClassB> getSourceClass() {
        return SourceClassB.class;
    }

    @ProjectionFor(SourceClassBDto.class)
    public static class Projection extends DtoProjection<SourceClassBDto> {

        public DtoListFieldConf<solutions.sulfura.hyperkit.dsl.projections.test_aux.circular_dependencies.SourceClassADto.Projection> propertyArray;
        public DtoFieldConf<solutions.sulfura.hyperkit.dsl.projections.test_aux.circular_dependencies.SourceClassADto.Projection> property;
        public ListFieldConf genericPropertyArray;
        public DtoListFieldConf<solutions.sulfura.hyperkit.dsl.projections.test_aux.circular_dependencies.SourceClassADto.Projection> genericProperty;

        public Projection(){}

        public void applyProjectionTo(SourceClassBDto dto) throws DtoProjectionException {
            dto.propertyArray = ProjectionUtils.getProjectedValue(dto.propertyArray, this.propertyArray);
            dto.property = ProjectionUtils.getProjectedValue(dto.property, this.property);
            dto.genericPropertyArray = ProjectionUtils.getProjectedValue(dto.genericPropertyArray, this.genericPropertyArray);
            dto.genericProperty = ProjectionUtils.getProjectedValue(dto.genericProperty, this.genericProperty);
        }

    }

}