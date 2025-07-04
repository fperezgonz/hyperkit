package solutions.sulfura.hyperkit.utils.spring.openapi;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.utils.spring.StdDtoRequestBody;
import solutions.sulfura.hyperkit.utils.spring.StdDtoResponseBody;

import java.util.List;

/**
 * Test controller for testing the ProjectionOpenApiCustomizer.
 * Contains endpoints with various projection scenarios.
 */
@RestController
@RequestMapping("/openapi-test")
public class ProjectionOpenApiTestController {

    /**
     * Endpoint that returns a projected DTO.
     * Uses projection {name, age}.
     */
    @GetMapping("/test-dto-projection-response")
    @TestDto1
    public HttpEntity<TestDto> getTestDto() {
        return new HttpEntity<>(new TestDto(1L, "Test", 25, null));
    }

    /**
     * Endpoint that returns a differently projected DTO.
     * Uses projection {id, name}.
     */
    @GetMapping("/test-dto-projection2-response")
    @TestDto2
    public HttpEntity<TestDto> getTestDto2() {
        return new HttpEntity<>(new TestDto(1L, "Test", 25, null));
    }

    /**
     * Endpoint that returns a projected nested DTO.
     * Uses projection {nestedDto{id}}.
     */
    @GetMapping("/test-nested-dto-projection-response")
    @NestedDto1
    public HttpEntity<TestDto> getNestedTestDto() {
        return new HttpEntity<>(new TestDto(1L, "Test", 25, ValueWrapper.of(new NestedTestDto(1L, null))));
    }

    /**
     * Endpoint that returns a projected nested DTO.
     * Uses projection {nestedDto{id}}.
     */
    @GetMapping("/test-nested-dto-projection2-response")
    @NestedDto2
    public HttpEntity<TestDto> getNestedTestDto2() {
        return new HttpEntity<>(new TestDto(1L, "Test", 25, ValueWrapper.of(new NestedTestDto(1L, null))));
    }

    /**
     * Endpoint that returns a list of projected DTOs.
     * Uses projection {name, age}.
     */
    @GetMapping("/test-dto-list-projection-response")
    @TestDto1
    public HttpEntity<List<TestDto>> getTestDtosList() {
        return new HttpEntity<>(List.of(new TestDto(1L, "Test", 25, null)));
    }

    /**
     * Endpoint that returns a StdDtoResponseBody with projected DTOs.
     * Uses projection {name, age}.
     */
    @GetMapping("/test-std-dto-projection-response")
    @TestDto1
    public HttpEntity<StdDtoResponseBody<TestDto>> getTestDtos() {
        StdDtoResponseBody<TestDto> response = new StdDtoResponseBody<>();
        response.setData(List.of(new TestDto(1L, "Test", 25, null)));
        return new HttpEntity<>(response);
    }

    /**
     * Endpoint that accepts a projected DTO in the request body.
     * Uses projection {name, age}.
     */
    @PostMapping("/test-dto-projection-body")
    public HttpEntity<TestDto> postTestDto(
            @TestDto1
            @RequestBody TestDto testDto) {
        return new HttpEntity<>(testDto);
    }

    /**
     * Endpoint that accepts a StdDtoRequestBody with projected DTOs.
     * Uses projection {name, age}.
     */
    @PostMapping("/test-std-dto-projection-body")
    public HttpEntity<StdDtoResponseBody<TestDto>> postTestDtos(
            @TestDto1
            @RequestBody StdDtoRequestBody<TestDto> request) {
        StdDtoResponseBody<TestDto> response = new StdDtoResponseBody<>();
        response.setData(request.getData());
        return new HttpEntity<>(response);
    }

    /**
     * Simple DTO class for testing projections.
     */
    public static class TestDto implements solutions.sulfura.hyperkit.dtos.Dto<Long> {
        public ValueWrapper<Long> id = ValueWrapper.empty();
        public ValueWrapper<String> name = ValueWrapper.empty();
        public ValueWrapper<Integer> age = ValueWrapper.empty();
        public ValueWrapper<NestedTestDto> nestedDto = ValueWrapper.empty();

        @SuppressWarnings("unused")
        public TestDto() {
        }

        public TestDto(long l, String test, int i, ValueWrapper<NestedTestDto> nestedTestDto) {
            this.id = ValueWrapper.of(l);
            this.name = ValueWrapper.of(test);
            this.age = ValueWrapper.of(i);
            this.nestedDto = nestedTestDto == null ? ValueWrapper.empty() : nestedTestDto;
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

            @Override
            public void applyProjectionTo(TestDto dto) {
                dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
                dto.name = ProjectionUtils.getProjectedValue(dto.name, this.name);
                dto.age = ProjectionUtils.getProjectedValue(dto.age, this.age);
                dto.nestedDto = ProjectionUtils.getProjectedValue(dto.nestedDto, this.nestedDto);
            }
        }
    }

    /**
     * DTO class with a nested DTO for testing nested projections.
     */
    public static class NestedTestDto implements solutions.sulfura.hyperkit.dtos.Dto<Long> {
        public ValueWrapper<Long> id = ValueWrapper.empty();
        public ValueWrapper<TestDto> nestedDto = ValueWrapper.empty();

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
            public FieldConf nestedDto;

            @Override
            public void applyProjectionTo(NestedTestDto dto) {
                dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
                dto.nestedDto = ProjectionUtils.getProjectedValue(dto.nestedDto, this.nestedDto);
            }
        }
    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = "name, age")
    @interface TestDto1 {
    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = "id, name")
    @interface TestDto2 {
    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = "nestedDto{id}")
    @interface NestedDto1 {
    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = "nestedDto{id}")
    @interface NestedDto2 {
    }

}