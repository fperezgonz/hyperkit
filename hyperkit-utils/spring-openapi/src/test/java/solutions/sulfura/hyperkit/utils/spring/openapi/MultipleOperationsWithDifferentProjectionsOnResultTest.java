package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
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
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_0;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class MultipleOperationsWithDifferentProjectionsOnResultTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void checkApiDocsIsAccessible() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    private OpenAPI parseOpenApiSpec(String json) throws Exception {
        return Json.mapper().readValue(json, OpenAPI.class);
    }

    @Test
    @DisplayName("Should generate different model classes for different projections of the same DTO")
    public void testShouldGenerateDifferentModelClassesForDifferentProjections() throws Exception {
        // Given the OpenAPI spec is generated

        // When we get the OpenAPI spec
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        // Then the OpenAPI spec should contain different model classes for different projections
        String content = result.getResponse().getContentAsString();

        // Parse the OpenAPI JSON response
        OpenAPI openAPI = parseOpenApiSpec(content);

        // Verify that the OpenAPI spec contains both projected models
        OpenApiTestControllers.verifyTestDtoProjection1Schema(openAPI, openAPI.getComponents().getSchemas().get("TestDto1_TestDto"));
        OpenApiTestControllers.verifyTestDtoProjection2Schema(openAPI, openAPI.getComponents().getSchemas().get("TestDto2_TestDto"));

    }

}

@WebMvcTest(controllers = {
        OpenApiTestControllers.ProjectionOnResponseTestController1.class,
        OpenApiTestControllers.ProjectionOnResponseTestController2.class
})
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_0.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_0_MultipleOperationsWithDifferentProjectionsOnResultTest extends MultipleOperationsWithDifferentProjectionsOnResultTest {
}

@WebMvcTest(controllers = {
        OpenApiTestControllers.ProjectionOnResponseTestController1.class,
        OpenApiTestControllers.ProjectionOnResponseTestController2.class
})
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_1.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_1_MultipleOperationsWithDifferentProjectionsOnResultTest extends MultipleOperationsWithDifferentProjectionsOnResultTest {
}
