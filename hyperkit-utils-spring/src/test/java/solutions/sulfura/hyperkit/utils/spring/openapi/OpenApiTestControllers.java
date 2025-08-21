package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.utils.spring.SingleDtoResponseBody;
import solutions.sulfura.hyperkit.utils.spring.StdDtoRequestBody;
import solutions.sulfura.hyperkit.utils.spring.DtoListResponseBody;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OpenApiTestControllers {

    /**
     * Endpoint that returns a projected DTO.
     * Uses projection {@link TestDto1}.
     */
    @RestController
    @RequestMapping("/openapi-test")
    public static class ProjectionOnResponseTestController1 {

        @GetMapping("/openapi-test/test-dto-projection1-response")
        @TestDto1
        public HttpEntity<TestDto> getTestDto1() {
            return new HttpEntity<>(new TestDto(1L, "Test", 25, null));
        }

    }

    /**
     * Uses projection {@link TestDto2}.
     */
    @RestController
    public static class ProjectionOnResponseTestController2 {

        @GetMapping("/openapi-test/test-dto-projection2-response")
        @TestDto2
        public ResponseEntity<TestDto> getTestDto2() {
            return ResponseEntity
                    .ok()
                    .body(new TestDto(1L, "Test", 25, null));
        }

    }

    /**
     * Uses projection {@link DeepProjection}.
     */
    @RestController
    public static class DeeplyNestedProjectionOnResponseTestController {
        @GetMapping("/deeply-nested-projection-response")
        @DeepProjection
        public TestDto DeepProjection() {
            return new TestDto(1L, "Test1", 25,
                    ValueWrapper.of(new NestedTestDto(1L, new TestDto(2L, "Test2", 25,
                            ValueWrapper.of(new NestedTestDto(2L, new TestDto(3L, "Test3", 25,
                                    ValueWrapper.of(new NestedTestDto(3L, new TestDto(4L, "Test4", 25, null)))))))))
            );
        }
    }

    /**
     * Uses projection {@link TestDto1}.
     */
    @RestController
    public static class DtoListProjectionOnResponseTestController {

        @GetMapping("/test-dto-list-projection-response")
        @TestDto1
        public HttpEntity<List<TestDto>> getTestDtosList() {
            return new HttpEntity<>(List.of(new TestDto(1L, "Test", 25, null)));
        }

    }

    /**
     * Uses projection {@link TestDto1}.
     */
    @RestController
    public static class StdDtoResponseProjectionOnResponseTestController {
        @GetMapping("/test-multi-dto-projection-response")
        @TestDto1
        public HttpEntity<DtoListResponseBody<TestDto>> getTestDtos() {
            DtoListResponseBody<TestDto> response = new DtoListResponseBody<>();
            response.setData(List.of(new TestDto(1L, "Test", 25, null)));
            return new HttpEntity<>(response);
        }
    }

    /**
     * Uses projection {@link TestDto1}.
     */
    @RestController
    public static class SingleDtoResponseProjectionOnResponseTestController {
        @GetMapping("/test-single-dto-projection-response")
        @TestDto1
        public HttpEntity<SingleDtoResponseBody<TestDto>> getTestDtos() {
            SingleDtoResponseBody<TestDto> response = new SingleDtoResponseBody<>();
            response.setData(new TestDto(1L, "Test", 25, null));
            return new HttpEntity<>(response);
        }
    }

    /**
     * Uses projection {@link TestDto1}.
     */
    @RestController
    public static class DtoProjectionOnRequestTestController {
        @PostMapping("/test-dto-projection-body")
        public HttpEntity<TestDto> postTestDto(
                @TestDto1
                @RequestBody TestDto testDto) {
            return new HttpEntity<>(testDto);
        }
    }

    /**
     * Uses projection {@link TestDto1}.
     */
    @RestController
    public static class StdDtoRequestProjectionOnRequestTestController {
        @PostMapping("/test-std-dto-projection-request")
        public HttpEntity<StdDtoRequestBody<TestDto>> postTestDtos(
                @TestDto1
                @RequestBody StdDtoRequestBody<TestDto> request) {
            StdDtoRequestBody<TestDto> response = new StdDtoRequestBody<>();
            response.setData(request.getData());
            return new HttpEntity<>(response);
        }
    }

    /**
     * Uses projection {@link ExplicitNamespaceProjection}
     */
    @RestController
    @RequestMapping("/explicit-namespace")
    public static class ExplicitNamespaceController {

        @GetMapping("/test")
        @ExplicitNamespaceProjection
        public HttpEntity<TestDto> getTestDto() {
            return new HttpEntity<>(new TestDto(1L, "Test", 25, null));
        }
    }

    /**
     * Controller for testing projections on parameters. Uses projection {@link OpenApiTestControllers.TestDto1}
     */
    @RestController
    public static class ParameterProjectionTestController {
        @GetMapping("/parameter-projection-test")
        public String getWithProjectedParameter(
                @OpenApiTestControllers.TestDto1
                @RequestParam(name = "testDtoParam") OpenApiTestControllers.TestDto testDto) {
            return "Test";
        }
    }

    /**
     * Controller for testing projections on responses with other fields besides the projected class. Uses projection {@link OpenApiTestControllers.TestDto1}
     */
    @RestController
    public static class ResponseWithMultipleFieldsProjectionTestController {
        @GetMapping("/test-multiple-fields-projection-response")
        @TestDto1
        public HttpEntity<DtoResponseWithMultipleFields<TestDto>> getTestDtos() {
            DtoResponseWithMultipleFields<TestDto> response = new DtoResponseWithMultipleFields<>();
            response.setData(List.of(new TestDto(1L, "Test", 25, null)));
            return new HttpEntity<>(response);
        }
    }

    public static class DtoResponseWithMultipleFields<T extends Dto<?>> extends DtoListResponseBody<T> {

        public List<ErrorData> errors = new ArrayList<>();
        // Object field with no properties
        public HashMap<String, Object> extensions = new HashMap<>();

        public static class ErrorData {
            public String id;
            public String message;
            public String code;
        }
    }

    public static void verifyErrorItemsSchema(OpenAPI openAPI, Schema<?> errorItemsSchema) {
        errorItemsSchema = SchemaBuilderUtils.findReferencedModel(openAPI, errorItemsSchema);
        assertNotNull(errorItemsSchema);
        assertTrue(errorItemsSchema.getProperties().containsKey("id"));
        assertTrue(errorItemsSchema.getProperties().containsKey("message"));
        assertTrue(errorItemsSchema.getProperties().containsKey("code"));
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
            public DtoFieldConf<TestDto.Projection> nestedDto;

            @Override
            public void applyProjectionTo(NestedTestDto dto) {
                dto.id = ProjectionUtils.getProjectedValue(dto.id, this.id);
                dto.nestedDto = ProjectionUtils.getProjectedValue(dto.nestedDto, this.nestedDto);
            }
        }
    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = "name, age, nestedDto{id}")
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestDto1 {
    }

    public static void verifyTestDtoProjection1Schema(OpenAPI openAPI, Schema<?> testDtoProjection1Schema) {

        testDtoProjection1Schema = SchemaBuilderUtils.findReferencedModel(openAPI, testDtoProjection1Schema);
        // Should contain
        assertNotNull(testDtoProjection1Schema);
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("name"));
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("age"));
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("nestedDto"));
        // Should not contain
        assertFalse(testDtoProjection1Schema.getProperties().containsKey("id"));

        Schema<?> nestedDtoSchema = (Schema<?>) testDtoProjection1Schema.getProperties().get("nestedDto");
        nestedDtoSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedDtoSchema);

        assertNotNull(nestedDtoSchema);
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("nestedDto"));
        assertTrue(nestedDtoSchema.getProperties().containsKey("id"));
        // Should not contain
        assertFalse(nestedDtoSchema.getProperties().containsKey("nestedDto"));

    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = "id, name")
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestDto2 {
    }

    public static void verifyTestDtoProjection2Schema(OpenAPI openAPI, Schema<?> testDtoProjection2Schema) {

        testDtoProjection2Schema = SchemaBuilderUtils.findReferencedModel(openAPI, testDtoProjection2Schema);

        assertNotNull(testDtoProjection2Schema);
        assertTrue(testDtoProjection2Schema.getProperties().containsKey("id"));
        assertTrue(testDtoProjection2Schema.getProperties().containsKey("name"));
        // Should not contain
        assertFalse(testDtoProjection2Schema.getProperties().containsKey("age"));
        assertFalse(testDtoProjection2Schema.getProperties().containsKey("nestedDto"));

    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = "id, name, nestedDto{id,nestedDto{id,nestedDto{id,nestedDto{id}}}}")
    @Retention(RetentionPolicy.RUNTIME)
    @interface DeepProjection {
    }

    public static void verifyTestDtoDeepProjectionSchema(OpenAPI openAPI, Schema<?> deepProjectionSchema) {

        deepProjectionSchema = SchemaBuilderUtils.findReferencedModel(openAPI, deepProjectionSchema);

        // Should contain
        assertNotNull(deepProjectionSchema);
        assertTrue(deepProjectionSchema.getProperties().containsKey("id"));
        assertTrue(deepProjectionSchema.getProperties().containsKey("nestedDto"));
        assertTrue(deepProjectionSchema.getProperties().containsKey("name"));
        // Should not contain
        assertFalse(deepProjectionSchema.getProperties().containsKey("age"));

        Schema<?> nestedSchema = (Schema<?>) deepProjectionSchema.getProperties().get("nestedDto");
        nestedSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedSchema);
        //Should contain
        assertNotNull(nestedSchema);
        assertTrue(nestedSchema.getProperties().containsKey("id"));
        assertTrue(deepProjectionSchema.getProperties().containsKey("nestedDto"));

        nestedSchema = (Schema<?>) nestedSchema.getProperties().get("nestedDto");
        nestedSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedSchema);
        //Should contain
        assertNotNull(nestedSchema);
        assertTrue(nestedSchema.getProperties().containsKey("id"));
        assertTrue(nestedSchema.getProperties().containsKey("nestedDto"));
        //Should not contain
        assertFalse(nestedSchema.getProperties().containsKey("age"));
        assertFalse(nestedSchema.getProperties().containsKey("name"));

        nestedSchema = (Schema<?>) nestedSchema.getProperties().get("nestedDto");
        nestedSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedSchema);
        //Should contain
        assertNotNull(nestedSchema);
        assertTrue(nestedSchema.getProperties().containsKey("id"));
        assertTrue(nestedSchema.getProperties().containsKey("nestedDto"));

        nestedSchema = (Schema<?>) nestedSchema.getProperties().get("nestedDto");
        nestedSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedSchema);
        //Should contain
        assertNotNull(nestedSchema);
        assertTrue(nestedSchema.getProperties().containsKey("id"));
        //Should not contain
        assertFalse(nestedSchema.getProperties().containsKey("nestedDto"));
        assertFalse(nestedSchema.getProperties().containsKey("age"));
        assertFalse(nestedSchema.getProperties().containsKey("name"));

    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = "nestedDto{id}")
    @Retention(RetentionPolicy.RUNTIME)
    @interface NestedDto1 {
    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = "nestedDto{id}")
    @Retention(RetentionPolicy.RUNTIME)
    @interface NestedDto2 {
    }

    /**
     * Projection annotation with an explicit namespace declaration.
     */
    @DtoProjectionSpec(
            projectedClass = TestDto.class,
            value = "id",
            namespace = "CustomNamespace"
    )
    @Retention(RetentionPolicy.RUNTIME)
    @interface ExplicitNamespaceProjection {
    }

    public static void verifyExplicitNamespaceProjectionSchema(OpenAPI openAPI, Schema<?> schema) {
        schema = SchemaBuilderUtils.findReferencedModel(openAPI, schema);

        assertNotNull(schema, "Schema with explicit namespace should exist");
        assertTrue(schema.getProperties().containsKey("id"), "Schema should contain 'id' property");
        assertFalse(schema.getProperties().containsKey("name"), "Schema should contain 'name' property");
        assertFalse(schema.getProperties().containsKey("age"), "Schema should not contain 'age' property");
        assertFalse(schema.getProperties().containsKey("nestedDto"), "Schema should not contain 'nestedDto' property");
    }
}
