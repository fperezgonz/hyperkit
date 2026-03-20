package solutions.sulfura.hyperkit.utils.spring.openapi.model;

import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;

import java.util.Objects;

/**
 * DTO class with a nested DTO for testing nested projections.
 */
public class NestedTestDto implements solutions.sulfura.hyperkit.dtos.Dto<Long> {
    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<TestDto> nestedDto = ValueWrapper.empty();
    public ValueWrapper<TestDto> nestedDto2 = ValueWrapper.empty();
    public ValueWrapper<java.util.AbstractList<ListOperation<NestedTestDto>>> nestedDtoList = ValueWrapper.empty();

    @SuppressWarnings("unused")
    public NestedTestDto() {
    }

    public NestedTestDto(Long id, TestDto nestedDto) {
        this.id = ValueWrapper.of(id);
        this.nestedDto = ValueWrapper.of(nestedDto);
    }

    @Override
    public Class<?> getSourceClass() {
        return NestedTestDto.class;
    }

    public static class Projection extends DtoProjection<NestedTestDto> {
        public FieldConf id;
        public DtoFieldConf<TestDto.Projection> nestedDto;
        public DtoFieldConf<TestDto.Projection> nestedDto2;
        public DtoListFieldConf<Projection> nestedDtoList;

        @Override
        public void applyProjectionTo(NestedTestDto dto) {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.nestedDto = ProjectionUtils.getProjectedValue(dto.nestedDto, this.nestedDto);
            dto.nestedDto2 = ProjectionUtils.getProjectedValue(dto.nestedDto2, this.nestedDto2);
            dto.nestedDtoList = ProjectionUtils.getProjectedValue(dto.nestedDtoList, this.nestedDtoList);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Projection that = (Projection) o;
            return Objects.equals(id, that.id) && Objects.equals(nestedDto, that.nestedDto) && Objects.equals(nestedDto2, that.nestedDto2) && Objects.equals(nestedDtoList, that.nestedDtoList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, nestedDto, nestedDto2, nestedDtoList);
        }
    }
}
