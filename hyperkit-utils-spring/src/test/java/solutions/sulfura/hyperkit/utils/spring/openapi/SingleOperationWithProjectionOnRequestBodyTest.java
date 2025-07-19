package solutions.sulfura.hyperkit.utils.spring.openapi;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OpenApiTestControllers.DtoProjectionOnRequestTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class})
public class SingleOperationWithProjectionOnRequestBodyTest {

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
        OpenApiTestControllers.verifyTestDtoProjection1Schema(openAPI, schema);

    }

}
