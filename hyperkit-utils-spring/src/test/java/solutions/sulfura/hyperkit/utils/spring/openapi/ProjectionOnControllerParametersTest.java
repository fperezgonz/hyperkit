package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ProjectionOnControllerParametersTest {

    @Autowired
    private MockMvc mockMvc;

    private OpenAPI parseOpenApiSpec(String json) throws Exception {
        return Json.mapper().readValue(json, OpenAPI.class);
    }

    @Test
    @DisplayName("OpenApi should generate projected model for controller parameter with projection annotation")
    public void testOpenApiShouldGenerateProjectedModelForControllerParameter() throws Exception {
        // Given the OpenAPI spec is generated

        // When we build the OpenAPI spec
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        OpenAPI openAPI = parseOpenApiSpec(content);

        // Then
        // Test the path exists

        // Get the parameter from the operation
        Parameter parameter = openAPI.getPaths().get("/parameter-projection-test").getGet()
                .getParameters().stream()
                .filter(param -> Objects.equals(param.getName(), "testDtoParam"))
                .findFirst()
                .orElseThrow();

        // Get the schema for the parameter
        Schema<?> paramSchema = parameter.getSchema();
        assertNotNull(paramSchema, "Parameter schema should exist");

        // Verify that the schema has the expected properties based on the projection
        OpenApiTestControllers.verifyTestDtoProjection1Schema(openAPI, paramSchema);
    }
}

@WebMvcTest(controllers = {
        OpenApiTestControllers.ParameterProjectionTestController.class
})
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_0.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_0_ProjectionOnControllerParametersTest extends ProjectionOnControllerParametersTest {
}

@WebMvcTest(controllers = {
        OpenApiTestControllers.ParameterProjectionTestController.class
})
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_1.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_1_ProjectionOnControllerParametersTest extends ProjectionOnControllerParametersTest {
}
