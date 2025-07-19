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

@WebMvcTest(controllers = OpenApiTestControllersUnderTest.ProjectionOnResponseTestController1.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class})
public class SingleOperationWithProjectionOnResponseTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Parses the OpenAPI JSON response to get the OpenAPI object.
     */
    private OpenAPI parseOpenApiJson(String json) throws Exception {
        return Json.mapper().readValue(json, OpenAPI.class);
    }

    @Test
    @DisplayName("OpenApi should apply projection to the generated model")
    public void testOpenApiShouldApplyProjectionsToGeneratedModel() throws Exception {
        // Given a controller with a projection annotation on a Dto

        // When we get the OpenAPI spec
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        OpenAPI openAPI = parseOpenApiJson(content);

        // Then the OpenAPI spec should contain the projected model
        Schema<?> schema = openAPI.getComponents().getSchemas().get("TestDto1_TestDto");
        OpenApiTestControllersUnderTest.verifyTestDtoProjection1Schema(openAPI, schema);

    }

}
