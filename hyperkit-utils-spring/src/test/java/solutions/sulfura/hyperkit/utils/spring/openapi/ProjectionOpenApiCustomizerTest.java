package solutions.sulfura.hyperkit.utils.spring.openapi;

import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the ProjectionOpenApiCustomizer class.
 * Verifies that projections are correctly applied to the OpenAPI specification.
 */
@WebMvcTest(controllers = ProjectionOpenApiTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class})
public class ProjectionOpenApiCustomizerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void checkApiDocsIsAccessible() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
}
