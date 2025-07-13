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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
//        OpenApiTestControllers.DtoListProjectionOnResponseTestController.class,
        OpenApiTestControllers.StdDtoResponseProjectionOnResponseTestController.class
})
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class})
public class OpenApiNestedRootProjectionTests {

    @Autowired
    private MockMvc mockMvc;

    private OpenAPI parseOpenApiSpec(String json) throws Exception {
        return Json.mapper().readValue(json, OpenAPI.class);
    }


    @Test
    @DisplayName("Should apply nested root projections on Lists and StdDtoResponse")
    public void testShouldApplyNestedRootProjections() throws Exception {
        // Given a controller with a projection annotation on a Dto

        // When we get the OpenAPI spec
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        OpenAPI openAPI = parseOpenApiSpec(content);

        // Then the OpenAPI spec should contain the projected model
        Schema<?> schema = openAPI.getComponents().getSchemas().get("TestDto1_StdDtoResponseBodyTestDto");
        assertNotNull(schema);

        // StdDtoResponse Should contain "data"
        Schema<?> responseDataSchema = schema.getProperties().get("data");
        assertNotNull(responseDataSchema);
        Schema<?> dataItemsSchema = responseDataSchema.getItems();

        //Verify the items schema
        OpenApiTestControllers.verifyTestDtoProjection1Schema(openAPI, dataItemsSchema);

    }


//    // Verify that the OpenAPI spec contains the projected collection model
//    assertTrue(content.contains("ProjectedTestDtoList"));
//
//    // Get the operation for the collection endpoint
//    PathItem pathItem = openAPI.getPaths().get("/test/test-dtos-list");
//    assertNotNull(pathItem);
//
//    Operation operation = pathItem.getGet();
//    assertNotNull(operation);
//
//    // Verify that the response schema is an array of projected DTOs
//    Schema<?> responseSchema = operation.getResponses().get("200").getContent().get("application/json").getSchema();
//    assertNotNull(responseSchema);
//
//    // Verify that the array items are projected DTOs
//    Schema<?> itemsSchema = responseSchema.getItems();
//    assertNotNull(itemsSchema);
//
//    // If it's a reference, resolve it
//        if (itemsSchema.get$ref() != null) {
//        String ref = itemsSchema.get$ref();
//        ref = ref.substring(ref.lastIndexOf("/") + 1);
//        itemsSchema = openAPI.getComponents().getSchemas().get(ref);
//        assertNotNull(itemsSchema);
//    }
//
//    // Verify that the items only contain the fields specified in the projection
//    assertTrue(itemsSchema.getProperties().containsKey("name"));
//    assertTrue(itemsSchema.getProperties().containsKey("age"));
//    assertFalse(itemsSchema.getProperties().containsKey("id"));


}
