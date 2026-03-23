package solutions.sulfura.hyperkit.utils.spring;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import solutions.sulfura.hyperkit.dsl.projections.FieldAliasUtils;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestUtils {

    protected static final Logger logger = LoggerFactory.getLogger(TestUtils.class);
    static FieldAliasUtils FIELD_ALIAS_UTILS_INSTANCE = new FieldAliasUtils(true);

    public static OpenAPI getOpenApi(MockMvc mockMvc) throws Exception {
        MvcResult result = mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        return Json.mapper().readValue(json, OpenAPI.class);
    }


    public static Schema<?> getSchemaForPath(OpenAPI openAPI, String path) {
        if (openAPI.getPaths() == null || openAPI.getPaths().get(path) == null) {
            throw new RuntimeException("Path " + path + " not found in OpenAPI spec. Paths: " + (openAPI.getPaths() == null ? "null" : openAPI.getPaths().keySet()));
        }
        if (openAPI.getPaths().get(path).getPost() != null && openAPI.getPaths().get(path).getPost().getRequestBody() != null) {
            return openAPI.getPaths().get(path)
                    .getPost()
                    .getRequestBody().getContent().get("application/json").getSchema();
        } else if (openAPI.getPaths().get(path).getGet() != null && openAPI.getPaths().get(path).getGet().getResponses().get("200") != null) {
            Schema<?> schema = null;
            if (openAPI.getPaths().get(path).getGet().getResponses().get("200").getContent().get("application/json") != null) {
                schema = openAPI.getPaths().get(path).getGet().getResponses().get("200").getContent().get("application/json").getSchema();
            } else if (openAPI.getPaths().get(path).getGet().getResponses().get("200").getContent().get("*/*") != null) {
                schema = openAPI.getPaths().get(path).getGet().getResponses().get("200").getContent().get("*/*").getSchema();
            }
            if (schema != null) {
                return schema;
            }
        }
        throw new RuntimeException("Could not find a schema for path " + path);
    }

    /**
     * Verifies that the given OpenAPI schema structure matches the projection specification.
     *
     */
    public static void verifySchemaMatchesProjection(OpenAPI openAPI, Schema<?> schema, DtoProjection<?> projection) throws IllegalAccessException {

        Schema<?> referencedSchema = SchemaBuilderUtils.findReferencedModel(openAPI, schema);
        String schemaIdentifier = getSchemaIdentifier(referencedSchema, schema);

        assertNotNull(referencedSchema, "Schema  should not be null");

        // TODO use properties instead of fields
        for (Field field : projection.getClass().getFields()) {

            logger.debug("Verifying field: {}", field.getName());

            String fieldName = field.getName();
            FieldConf fieldConf = (FieldConf) field.get(projection);

            if (fieldConf == null) {
                // If there are no other projected fields aliased with the name of this field, verify the field is not declared in the projection
                FieldAliasUtils.FieldConfData fieldConfDataOfPropertyAliasedWithTheNameOfThisField = FIELD_ALIAS_UTILS_INSTANCE.findFieldConfForPropertyByFieldAlias(projection, fieldName);
                fieldConf = fieldConfDataOfPropertyAliasedWithTheNameOfThisField == null ? null : fieldConfDataOfPropertyAliasedWithTheNameOfThisField.fieldConf();
                if (fieldConf == null) {
                    assertFalse(referencedSchema.getProperties().containsKey(fieldName), "Field '" + fieldName + "' is not declared in the projection, schema " + schemaIdentifier + " should not contain the property");
                }
                continue;
            }

            String propertyName = fieldConf.getFieldAlias();
            if (propertyName == null) {
                propertyName = fieldName;
            }

            if (fieldConf.getPresence() == FieldConf.Presence.IGNORED) {
                assertFalse(referencedSchema.getProperties().containsKey(propertyName), "Schema property '" + propertyName + "', alias of dto field '" + fieldName + "' is marked as IGNORED, schema " + schemaIdentifier + " should not contain the property");
                continue;
            }

            assertTrue(referencedSchema.getProperties().containsKey(propertyName),
                    "Schema " + schemaIdentifier + " should contain property '" + propertyName + "', alias of dto field '" + fieldName + "'");
            if (!(fieldConf instanceof DtoFieldConf<?> dtoFieldConf)) {
                continue;
            }

            DtoProjection<?> nestedProjection = dtoFieldConf.dtoProjection;

            Schema<?> nestedSchema = (Schema<?>) referencedSchema.getProperties().get(propertyName);
            nestedSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedSchema);

            // Handle array/list types
            if (nestedSchema.getType() != null && "array".equals(nestedSchema.getType())) {
                nestedSchema = nestedSchema.getItems();
                nestedSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedSchema);

                // Check if it's a ListOperation wrapper
                if (nestedSchema.getProperties() != null && nestedSchema.getProperties().containsKey("value")) {
                    nestedSchema = nestedSchema.getProperties().get("value");
                    nestedSchema = SchemaBuilderUtils.findReferencedModel(openAPI, nestedSchema);
                }

            }

            verifySchemaMatchesProjection(openAPI, nestedSchema, nestedProjection);

        }
    }

    private static String getSchemaIdentifier(Schema<?> referencedSchema, Schema<?> schema) {
        String schemaIdentifier = referencedSchema.getName();
        if (schemaIdentifier == null || schemaIdentifier.isEmpty()) {
            schemaIdentifier = referencedSchema.get$ref();
        }
        if (schemaIdentifier == null || schemaIdentifier.isEmpty()) {
            schemaIdentifier = referencedSchema.get$id();
        }
        if (schemaIdentifier == null || schemaIdentifier.isEmpty()) {
            schemaIdentifier = schema.getName();
        }
        if (schemaIdentifier == null || schemaIdentifier.isEmpty()) {
            schemaIdentifier = schema.get$ref();
        }
        if (schemaIdentifier == null || schemaIdentifier.isEmpty()) {
            schemaIdentifier = schema.get$id();
        }
        if (schemaIdentifier == null || schemaIdentifier.isEmpty()) {
            schemaIdentifier = "unknown";
        }
        return schemaIdentifier;
    }

}
