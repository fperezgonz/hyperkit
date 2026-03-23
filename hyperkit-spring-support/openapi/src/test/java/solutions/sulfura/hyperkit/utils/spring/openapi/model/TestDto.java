package solutions.sulfura.hyperkit.utils.spring.openapi.model;

import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;

import java.util.AbstractList;
import java.util.Objects;

/**
 * Simple DTO class for testing projections.
 */
public class TestDto implements solutions.sulfura.hyperkit.dtos.Dto<Long> {
    public ValueWrapper<Long> id = ValueWrapper.empty();
    public ValueWrapper<String> name = ValueWrapper.empty();
    public ValueWrapper<Integer> age = ValueWrapper.empty();
    public ValueWrapper<NestedTestDto> nestedDto = ValueWrapper.empty();
    public ValueWrapper<java.util.AbstractList<ListOperation<NestedTestDto>>> nestedDtoList = ValueWrapper.empty();

    @SuppressWarnings("unused")
    public TestDto() {
    }

    public TestDto(long l, String test, int i, ValueWrapper<NestedTestDto> nestedTestDto, ValueWrapper<AbstractList<ListOperation<NestedTestDto>>> nestedTestDtoList) {
        this.id = ValueWrapper.of(l);
        this.name = ValueWrapper.of(test);
        this.age = ValueWrapper.of(i);
        this.nestedDto = nestedTestDto == null ? ValueWrapper.empty() : nestedTestDto;
        this.nestedDtoList = nestedTestDtoList == null ? ValueWrapper.empty() : nestedTestDtoList;
    }

    @Override
    public Class<?> getSourceClass() {
        return TestDto.class;
    }

    @SuppressWarnings("unused")
    public static class Projection extends DtoProjection<TestDto> {
        public FieldConf id;
        public FieldConf name;
        public FieldConf age;
        public DtoFieldConf<NestedTestDto.Projection> nestedDto;
        public DtoListFieldConf<NestedTestDto.Projection> nestedDtoList;

        @Override
        public void applyProjectionTo(TestDto dto) {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.age = ProjectionUtils.getProjectedValue(dto.age, this.age);
            dto.nestedDto = ProjectionUtils.getProjectedValue(dto.nestedDto, this.nestedDto);
            dto.nestedDtoList = ProjectionUtils.getProjectedValue(dto.nestedDtoList, this.nestedDtoList);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Projection that = (Projection) o;
            return Objects.equals(id, that.id)
                    && Objects.equals(name, that.name)
                    && Objects.equals(age, that.age)
                    && Objects.equals(nestedDto, that.nestedDto)
                    && Objects.equals(nestedDtoList, that.nestedDtoList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, age, nestedDto, nestedDtoList);
        }

    }

}
