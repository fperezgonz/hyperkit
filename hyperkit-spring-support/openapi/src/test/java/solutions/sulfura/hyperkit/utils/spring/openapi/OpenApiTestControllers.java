package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.DtoListResponseBody;
import solutions.sulfura.hyperkit.utils.spring.SingleDtoResponseBody;
import solutions.sulfura.hyperkit.utils.spring.StdDtoRequestBody;
import solutions.sulfura.hyperkit.utils.spring.openapi.model.DtoResponseWithMultipleFields;
import solutions.sulfura.hyperkit.utils.spring.openapi.model.NestedTestDto;
import solutions.sulfura.hyperkit.utils.spring.openapi.model.TestDto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unused")
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
            return new HttpEntity<>(new TestDto(1L, "Test", 25, null, null));
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
                    .body(new TestDto(1L, "Test", 25, null, null));
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
            TestDto nested6 = new TestDto(4L, "Test4", 25, null, null);
            NestedTestDto nested5 = new NestedTestDto(3L, nested6);
            TestDto nested4 = new TestDto(3L, "Test3", 25, ValueWrapper.of(nested5), null);
            NestedTestDto nested3 = new NestedTestDto(2L, nested4);
            TestDto nested2 = new TestDto(2L, "Test2", 25, ValueWrapper.of(nested3), null);
            NestedTestDto nested1 = new NestedTestDto(1L, nested2);

            return new TestDto(1L, "Test1", 25, ValueWrapper.of(nested1), null);

        }
    }

    /**
     * Uses projection {@link DeepProjection}.
     */
    @RestController
    public static class RepeatedNestedReferencesProjectionOnResponseTestController {
        @GetMapping("/repeated-nested-references-projection-response")
        @RepeatedNestedReferencesProjection
        public TestDto RepeatedNestedReferencesProjection() {
            return null;
        }
    }

    /**
     * Uses projection {@link DeepProjection}.
     */
    @RestController
    public static class DifferentNestedProjectionOnResponseTestController {
        @GetMapping("/different-nested-references-projection-response")
        @DifferentNestedReferencesProjection
        public TestDto RepeatedNestedReferencesProjection() {
            TestDto nested6 = new TestDto(4L, "Test4", 25, null, null);
            NestedTestDto nested5 = new NestedTestDto(3L, nested6);
            TestDto nested4 = new TestDto(3L, "Test3", 25, ValueWrapper.of(nested5), null);
            NestedTestDto nested3 = new NestedTestDto(2L, nested4);
            TestDto nested2 = new TestDto(2L, "Test2", 25, ValueWrapper.of(nested3), null);
            NestedTestDto nested1 = new NestedTestDto(1L, nested2);

            return new TestDto(1L, "Test1", 25, ValueWrapper.of(nested1), null);
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
            return new HttpEntity<>(List.of(new TestDto(1L, "Test", 25, null, null)));
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
            response.setData(List.of(new TestDto(1L, "Test", 25, null, null)));
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
            response.setData(new TestDto(1L, "Test", 25, null, null));
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
            return new HttpEntity<>(new TestDto(1L, "Test", 25, null, null));
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
                @RequestParam(name = "testDtoParam") TestDto testDto) {
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
            response.setData(List.of(new TestDto(1L, "Test", 25, null, null)));
            return new HttpEntity<>(response);
        }
    }

    public static void verifyErrorItemsSchema(OpenAPI openAPI, Schema<?> errorItemsSchema) {
        errorItemsSchema = SchemaBuilderUtils.findReferencedModel(openAPI, errorItemsSchema);
        assertNotNull(errorItemsSchema);
        assertTrue(errorItemsSchema.getProperties().containsKey("id"));
        assertTrue(errorItemsSchema.getProperties().containsKey("message"));
        assertTrue(errorItemsSchema.getProperties().containsKey("code"));
        Schema<?> errorSourceSchema = errorItemsSchema.getProperties().get("source");
        errorSourceSchema = SchemaBuilderUtils.findReferencedModel(openAPI, errorSourceSchema);
        assertTrue(errorSourceSchema.getProperties().containsKey("id"));
    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = """
            name
            age
            nestedDto{id}
            nestedDtoList{id}
            """)
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
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("nestedDtoList"));
        // Should not contain
        assertFalse(testDtoProjection1Schema.getProperties().containsKey("id"));

        // NestedDto
        {
            Schema<?> nestedDtoSchema = (Schema<?>) testDtoProjection1Schema.getProperties().get("nestedDto");
            nestedDtoSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedDtoSchema);

            assertNotNull(nestedDtoSchema);
            assertTrue(nestedDtoSchema.getProperties().containsKey("id"));
            // Should not contain
            assertFalse(nestedDtoSchema.getProperties().containsKey("nestedDto"));
        }

        // NestedDto list
        {
            Schema<?> nestedDtoListSchema = (Schema<?>) testDtoProjection1Schema.getProperties().get("nestedDtoList");

            assertNotNull(nestedDtoListSchema);
            Schema<?> itemsSchema = nestedDtoListSchema.getItems();
            // It is a ListOperation, find the value
            itemsSchema = SchemaBuilderUtils.findReferencedModel(openAPI, itemsSchema);
            itemsSchema = itemsSchema.getProperties().get("value");

            itemsSchema = SchemaBuilderUtils.findReferencedModel(openAPI, itemsSchema);
            assertTrue(itemsSchema.getProperties().containsKey("id"));
            // Should not contain
            assertFalse(itemsSchema.getProperties().containsKey("nestedDto"));

        }

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

    @DtoProjectionSpec(projectedClass = TestDto.class, value = """
            name
            age
            nestedDto{
                id
                nestedDto{
                    id
                }
            }
            nestedDtoList{
                id
                nestedDto{
                    id
                }
            }
            """)
    @Retention(RetentionPolicy.RUNTIME)
    @interface RepeatedNestedReferencesProjection {
    }

    public static void verifyRepeatedNestedReferencesProjectionSchema(OpenAPI openAPI, Schema<?> testDtoProjection1Schema) {

        testDtoProjection1Schema = SchemaBuilderUtils.findReferencedModel(openAPI, testDtoProjection1Schema);
        // Should contain
        assertNotNull(testDtoProjection1Schema);
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("name"));
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("age"));
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("nestedDto"));
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("nestedDtoList"));
        // Should not contain
        assertFalse(testDtoProjection1Schema.getProperties().containsKey("id"));


        Schema<?> nestedDtoSchema = (Schema<?>) testDtoProjection1Schema.getProperties().get("nestedDto");
        nestedDtoSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedDtoSchema);

        Schema<?> nestedDtoListSchema = (Schema<?>) testDtoProjection1Schema.getProperties().get("nestedDtoList");
        assertTrue(SchemaBuilderUtils.isArrayType(nestedDtoListSchema));
        Schema<?> nestedDtoListItemsSchema = nestedDtoListSchema.getItems();

        // The nested schemas should be the same
        nestedDtoListItemsSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedDtoListItemsSchema);
        assertEquals(nestedDtoSchema, SchemaBuilderUtils.findReferencedModel(openAPI, nestedDtoListItemsSchema.getProperties().get("value")));

        // NestedDto
        assertNotNull(nestedDtoSchema);
        assertTrue(nestedDtoSchema.getProperties().containsKey("id"));
        assertTrue(nestedDtoSchema.getProperties().containsKey("nestedDto"));
        // Should not contain
        assertFalse(nestedDtoSchema.getProperties().containsKey("name"));
        assertFalse(nestedDtoSchema.getProperties().containsKey("age"));

    }

    @DtoProjectionSpec(projectedClass = TestDto.class, value = """
            name
            age
            nestedDto{
                id
                nestedDto{
                    id
                }
            }
            nestedDtoList{
                id
                nestedDto{
                    age
                }
            }
            """)
    @Retention(RetentionPolicy.RUNTIME)
    @interface DifferentNestedReferencesProjection {
    }

    public static void verifyDifferentNestedReferencesProjectionSchema(OpenAPI openAPI, Schema<?> testDtoProjection1Schema) {

        testDtoProjection1Schema = SchemaBuilderUtils.findReferencedModel(openAPI, testDtoProjection1Schema);
        // Should contain
        assertNotNull(testDtoProjection1Schema);
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("name"));
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("age"));
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("nestedDto"));
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("nestedDtoList"));
        // Should not contain
        assertFalse(testDtoProjection1Schema.getProperties().containsKey("id"));

        Schema<?> nestedDtoSchema = (Schema<?>) testDtoProjection1Schema.getProperties().get("nestedDto");
        nestedDtoSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedDtoSchema);

        Schema<?> nestedDtoListSchema = (Schema<?>) testDtoProjection1Schema.getProperties().get("nestedDtoList");
        assertTrue(SchemaBuilderUtils.isArrayType(nestedDtoListSchema));
        Schema<?> nestedDtoListItemsSchema = nestedDtoListSchema.getItems();

        // nestedDto and nestedDtoList projected schemas should be different
        nestedDtoListItemsSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedDtoListItemsSchema);
        nestedDtoListItemsSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedDtoListItemsSchema.getProperties().get("value"));
        assertNotEquals(nestedDtoSchema, nestedDtoListItemsSchema);

        // nestedDto
        assertNotNull(nestedDtoSchema);
        assertTrue(nestedDtoSchema.getProperties().containsKey("id"));
        assertTrue(nestedDtoSchema.getProperties().containsKey("nestedDto"));
        // Should not contain
        assertFalse(nestedDtoSchema.getProperties().containsKey("name"));
        assertFalse(nestedDtoSchema.getProperties().containsKey("age"));

        // nestedDto.nestedDto
        {
            Schema<?> nestedSchema = (Schema<?>) nestedDtoSchema.getProperties().get("nestedDto");
            nestedSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedSchema);

            // nestedDto
            assertNotNull(nestedSchema);
            assertTrue(nestedSchema.getProperties().containsKey("id"));
            // Should not contain
            assertFalse(nestedSchema.getProperties().containsKey("name"));
            assertFalse(nestedSchema.getProperties().containsKey("nestedDto"));
            assertFalse(nestedSchema.getProperties().containsKey("age"));
        }

        // nestedDtoList
        assertNotNull(nestedDtoListItemsSchema);
        assertTrue(nestedDtoListItemsSchema.getProperties().containsKey("id"));
        assertTrue(nestedDtoListItemsSchema.getProperties().containsKey("nestedDto"));
        // Should not contain
        assertFalse(nestedDtoListItemsSchema.getProperties().containsKey("name"));
        assertFalse(nestedDtoSchema.getProperties().containsKey("age"));

        // nestedDtoList.nestedDto
        {
            Schema<?> nestedSchema = (Schema<?>) nestedDtoListItemsSchema.getProperties().get("nestedDto");
            nestedSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedSchema);

            // nestedDto
            assertNotNull(nestedSchema);
            assertTrue(nestedSchema.getProperties().containsKey("age"));
            // Should not contain
            assertFalse(nestedSchema.getProperties().containsKey("id"));
            assertFalse(nestedSchema.getProperties().containsKey("name"));
            assertFalse(nestedSchema.getProperties().containsKey("nestedDto"));
        }

    }

}
