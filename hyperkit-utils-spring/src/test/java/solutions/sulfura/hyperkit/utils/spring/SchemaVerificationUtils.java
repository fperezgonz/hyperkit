package solutions.sulfura.hyperkit.utils.spring;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dsl.projections.FieldAliasUtils;
import solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class SchemaVerificationUtils {

    protected static final Logger logger = LoggerFactory.getLogger(SchemaVerificationUtils.class);

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
                FieldAliasUtils.FieldConfData fieldConfDataOfPropertyAliasedWithTheNameOfThisField = FieldAliasUtils.findFieldConfForPropertyByFieldAlias(projection, fieldName);
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
