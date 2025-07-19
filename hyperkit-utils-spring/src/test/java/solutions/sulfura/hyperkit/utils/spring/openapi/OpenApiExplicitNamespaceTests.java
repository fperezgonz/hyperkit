package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
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

/**
 * Tests for controllers with explicit namespace declarations on projection annotations.
 */
@WebMvcTest(controllers = OpenApiTestControllers.ExplicitNamespaceController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class})
public class OpenApiExplicitNamespaceTests {

    @Autowired
    private MockMvc mockMvc;

    private OpenAPI parseOpenApiSpec(String json) throws Exception {
        return Json.mapper().readValue(json, OpenAPI.class);
    }

    @Test
    @DisplayName("OpenApi should use explicit namespace for projected schema name")
    public void testOpenApiShouldUseExplicitNamespace() throws Exception {
        // Given a controller with a projection annotation that has an explicit namespace

        // When we get the OpenAPI spec
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        OpenAPI openAPI = parseOpenApiSpec(content);

        // Then the OpenAPI spec should contain the projected model with the explicit namespace
        Schema<?> schema = openAPI.getComponents().getSchemas().get("CustomNamespace_TestDto");
        OpenApiTestControllers.verifyExplicitNamespaceProjectionSchema(openAPI, schema);
    }
}
