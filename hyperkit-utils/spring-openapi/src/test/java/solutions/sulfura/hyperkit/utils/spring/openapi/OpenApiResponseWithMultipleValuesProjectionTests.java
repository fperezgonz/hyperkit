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
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_0;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_1;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class OpenApiResponseWithMultipleValuesProjectionTests {

    @Autowired
    private MockMvc mockMvc;

    private OpenAPI parseOpenApiSpec(String json) throws Exception {
        return Json.mapper().readValue(json, OpenAPI.class);
    }

    @Test
    @DisplayName("OpenApi generation should apply projections on ProjectableHolder to the dtos held, and not to other fields in the projectable holder")
    public void testOpenApiShouldApplyProjectionOnProjectableHolderToTheDtosHeldAndNotOtherProperties() throws Exception {
        // Given a controller with a projection annotation on a ProjectableHolder that holds a list of DTOs and has another field

        // When we get the OpenAPI spec
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        OpenAPI openAPI = parseOpenApiSpec(content);

        // Then the OpenAPI spec should contain the projected model
        Schema<?> schema = openAPI.getComponents().getSchemas().get("TestDto1_DtoResponseWithMultipleFieldsTestDto");
        assertNotNull(schema);

        // StdDtoResponse Should contain "data"
        Schema<?> responseDataSchema = schema.getProperties().get("data");
        assertNotNull(responseDataSchema);
        Schema<?> dataItemsSchema = responseDataSchema.getItems();

        //Verify the items schema
        OpenApiTestControllers.verifyTestDtoProjection1Schema(openAPI, dataItemsSchema);

        // StdDtoResponse Should contain "errors"
        Schema<?> responseErrorsSchema = schema.getProperties().get("errors");
        assertNotNull(responseErrorsSchema);
        Schema<?> errorsItemsSchema = responseErrorsSchema.getItems();

        // Verify the error items schema
        OpenApiTestControllers.verifyErrorItemsSchema(openAPI, errorsItemsSchema);

    }

}

@WebMvcTest(controllers = {
        OpenApiTestControllers.ResponseWithMultipleFieldsProjectionTestController.class
})
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_0.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_0_ResponseWithMultipleValuesProjectionTests extends OpenApiResponseWithMultipleValuesProjectionTests {
}

@WebMvcTest(controllers = {
        OpenApiTestControllers.ResponseWithMultipleFieldsProjectionTestController.class
})
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_1.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_1_ResponseWithMultipleValuesProjectionTests extends OpenApiResponseWithMultipleValuesProjectionTests {
}