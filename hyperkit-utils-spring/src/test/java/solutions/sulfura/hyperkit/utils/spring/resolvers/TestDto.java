package solutions.sulfura.hyperkit.utils.spring.resolvers;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;

// Simple DTO class for testing
public class TestDto implements Dto<Long> {
    public ValueWrapper<Long> id;
    public ValueWrapper<String> name;
    public ValueWrapper<Integer> age;

    public TestDto() {
    }

    public TestDto(Long id, String name, Integer age) {
        this.id = ValueWrapper.of(id);
        this.name = ValueWrapper.of(name);
        this.age = ValueWrapper.of(age);
    }

    @Override
    public Class<?> getSourceClass() {
        return TestDto.class;
    }

    public static class Projection extends DtoProjection<TestDto> {
        public FieldConf id;
        public FieldConf name;
        public FieldConf age;

        @Override
        public void applyProjectionTo(TestDto dto) {
            dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
            dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
            dto.age = ProjectionUtils.getProjectedValue(dto.age, this.age);
        }
    }
}
