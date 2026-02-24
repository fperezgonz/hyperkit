package solutions.sulfura.hyperkit.utils.spring.openapi.field_alias;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfig;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_0;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_1;
import solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils;
import solutions.sulfura.hyperkit.utils.spring.openapi.model.TestDto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static solutions.sulfura.hyperkit.utils.spring.openapi.field_alias.DtoProjectionOnRequestTestController.verifyTestDtoProjection1Schema;

public abstract class FieldAliasOnRequestTest {

    @Autowired
    private MockMvc mockMvc;

    private OpenAPI parseOpenApiSpec(String json) throws Exception {
        return Json.mapper().readValue(json, OpenAPI.class);
    }

    @Test
    @DisplayName("OpenApi generation should apply projection to request body parameter model")
    public void testOpenApiShouldApplyProjectionToRequestBodyParameterModel() throws Exception {
        // Given a controller with a projection annotation on a Dto

        // When we get the OpenAPI spec
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Parse the OpenAPI JSON response and get the Schema for the controller under test
        OpenAPI openAPI = parseOpenApiSpec(content);
        PathItem pathItem = openAPI.getPaths().get("/test-dto-projection-body");
        Schema<?> schema = pathItem
                .getPost()
                .getRequestBody().getContent().get("application/json").getSchema();

        // Then the schema for the parameter should match expectations
        verifyTestDtoProjection1Schema(openAPI, schema);

    }

}

@WebMvcTest(controllers = DtoProjectionOnRequestTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_0.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_0_Field_AliasOnRequestTest extends FieldAliasOnRequestTest {
}

@WebMvcTest(controllers = DtoProjectionOnRequestTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_1.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_1_Field_AliasOnRequestTest extends FieldAliasOnRequestTest {
}

@DtoProjectionSpec(projectedClass = TestDto.class, value = """
            name as code
            age
            nestedDto{id}
            nestedDtoList{id}
            """)
@Retention(RetentionPolicy.RUNTIME)
@interface TestDtoProjection {
}


/**
 * Uses projection {@link TestDtoProjection}.
 */
@RestController
class DtoProjectionOnRequestTestController {
    @PostMapping("/test-dto-projection-body")
    public HttpEntity<TestDto> postTestDto(
            @TestDtoProjection
            @RequestBody TestDto testDto) {
        return new HttpEntity<>(testDto);
    }

    public static void verifyTestDtoProjection1Schema(OpenAPI openAPI, Schema<?> testDtoProjection1Schema) {

        testDtoProjection1Schema = SchemaBuilderUtils.findReferencedModel(openAPI, testDtoProjection1Schema);
        // Should contain
        assertNotNull(testDtoProjection1Schema);
        assertTrue(testDtoProjection1Schema.getProperties().containsKey("code"));
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
}