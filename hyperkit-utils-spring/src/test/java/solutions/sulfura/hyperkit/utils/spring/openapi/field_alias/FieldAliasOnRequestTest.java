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
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfig;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_0;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_1;
import solutions.sulfura.hyperkit.utils.spring.openapi.model.TestDto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static solutions.sulfura.hyperkit.utils.spring.SchemaVerificationUtils.verifySchemaMatchesProjection;

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
        PathItem pathItem = openAPI.getPaths().get("/test-field-alias-on-request");
        Schema<?> schema = pathItem
                .getPost()
                .getRequestBody().getContent().get("application/json").getSchema();

        // Then the schema for the parameter should match expectations
        DtoProjection<?> projection = ProjectionDsl.parse(TestDtoProjection.class.getAnnotation(DtoProjectionSpec.class));
        verifySchemaMatchesProjection(openAPI, schema, projection);

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
    @PostMapping("/test-field-alias-on-request")
    public HttpEntity<TestDto> postTestDto(
            @TestDtoProjection
            @RequestBody TestDto testDto) {
        return new HttpEntity<>(testDto);
    }

}