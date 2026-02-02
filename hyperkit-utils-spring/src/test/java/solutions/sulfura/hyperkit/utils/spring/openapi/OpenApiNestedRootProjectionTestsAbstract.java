package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.jupiter.api.Assertions;
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

public abstract class OpenApiNestedRootProjectionTestsAbstract {

    @Autowired
    private MockMvc mockMvc;

    private OpenAPI parseOpenApiSpec(String json) throws Exception {
        return Json.mapper().readValue(json, OpenAPI.class);
    }

    @Test
    @DisplayName("OpenApi generation should apply projections on ProjectableHolder to the dtos in the list")
    public void testOpenApiShouldApplyProjectionOnProjectableHolderToTheDtosInTheList() throws Exception {
        // Given a controller with a projection annotation on a ProjectableHolder that holds a list of DTOs

        // When we get the OpenAPI spec
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        OpenAPI openAPI = parseOpenApiSpec(content);

        // Then the OpenAPI spec should contain the projected model
        Schema<?> schema = openAPI.getComponents().getSchemas().get("TestDto1_DtoListResponseBodyTestDto");
        assertNotNull(schema);

        // StdDtoResponse Should contain "data"
        Schema<?> responseDataSchema = schema.getProperties().get("data");
        assertNotNull(responseDataSchema);
        Schema<?> dataItemsSchema = responseDataSchema.getItems();

        //Verify the items schema
        OpenApiTestControllers.verifyTestDtoProjection1Schema(openAPI, dataItemsSchema);

    }

    @Test
    @DisplayName("OpenApi generation should apply projections on ProjectableHolder to the contained dto")
    public void testOpenApiShouldApplyProjectionOnProjectableHolderToTheContainedDto() throws Exception {
        // Given a controller with a projection annotation on a ProjectableHolder that holds a single DTO

        // When we get the OpenAPI spec
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        OpenAPI openAPI = parseOpenApiSpec(content);
        PathItem pathItem = openAPI.getPaths().get("/test-single-dto-projection-response");

        // Then the OpenAPI spec should contain the projected model
        Schema<?> schema = pathItem.getGet().getResponses().get("200")
                .getContent()
                .get("*/*")
                .getSchema();


        schema = SchemaBuilderUtils.findReferencedModel(openAPI, schema);
        schema = schema.getProperties().get("data");
        assertNotNull(schema);

        //Verify the items schema
        OpenApiTestControllers.verifyTestDtoProjection1Schema(openAPI, schema);

    }


    @Test
    @DisplayName("OpenApi generation should apply projections on collections to its elements")
    public void testOpenApiShouldApplyProjectionOnCollectionsToItsElements() throws Exception {
        // Given a controller with a projection annotation on a Dto

        // When we get the OpenAPI spec
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        OpenAPI openAPI = parseOpenApiSpec(content);

        // Then the OpenAPI spec should contain the projected model
        PathItem pathItem = openAPI.getPaths().get("/test-dto-list-projection-response");
        Schema<?> schema = pathItem
                .getGet()
                .getResponses()
                .get("200")
                .getContent()
                .get("*/*")
                .getSchema();

        assertNotNull(schema);

        Assertions.assertTrue(SchemaBuilderUtils.isArrayType(schema), "Schema is not an array");
        Schema<?> itemsSchema = schema.getItems();
        assertNotNull(itemsSchema);

        //Verify the items schema
        OpenApiTestControllers.verifyTestDtoProjection1Schema(openAPI, itemsSchema);

    }

}

@WebMvcTest(controllers = {
        OpenApiTestControllers.DtoListProjectionOnResponseTestController.class,
        OpenApiTestControllers.StdDtoResponseProjectionOnResponseTestController.class,
        OpenApiTestControllers.SingleDtoResponseProjectionOnResponseTestController.class
})
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_0.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_0_NestedRootProjectionTests extends OpenApiNestedRootProjectionTestsAbstract {}

@WebMvcTest(controllers = {
        OpenApiTestControllers.DtoListProjectionOnResponseTestController.class,
        OpenApiTestControllers.StdDtoResponseProjectionOnResponseTestController.class,
        OpenApiTestControllers.SingleDtoResponseProjectionOnResponseTestController.class
})
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_1.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_1_NestedRootProjectionTests extends OpenApiNestedRootProjectionTestsAbstract {}