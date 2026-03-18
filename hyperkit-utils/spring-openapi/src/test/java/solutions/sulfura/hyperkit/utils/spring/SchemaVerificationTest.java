package solutions.sulfura.hyperkit.utils.spring;

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
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.utils.spring.openapi.model.TestDto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static solutions.sulfura.hyperkit.utils.spring.TestUtils.*;

@WebMvcTest(controllers = SchemaVerificationTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_0.class})
public class SchemaVerificationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Verification should pass when the controller's schema has exactly the same attributes and aliases as the reference projection ")
    public void testPerfectMatch() throws Exception {
        // Given a controller with a projection, and a projection that is equivalent to the one on the controller
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/aliased-complex-projection");
        TestDto.Projection matchingProjection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.AliasedComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        // When verifying the projection, no errors are thrown
        assertDoesNotThrow(() -> verifySchemaMatchesProjection(openAPI, schema, matchingProjection));
    }

    @Test
    @DisplayName("Verification should fail when the controller's schema has less attributes than the reference projection")
    public void testMissingAttributeOnSchema() throws Exception {
        //Given a controller with a projection and a projection that has one more attribute projected
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/complex-projection");
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.ComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        projection.name = FieldConf.of(FieldConf.Presence.OPTIONAL);

        // When verifying the projection, an error is thrown
        assertThrows(AssertionError.class, () -> verifySchemaMatchesProjection(openAPI, schema, projection));
    }

    @Test
    @DisplayName("Verification should fail when the controller's schema has more attributes than the reference projection")
    public void testTooManyAttributesOnSchema() throws Exception {
        //Given a controller with a projection and a projection that has one more attribute projected
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/complex-projection");
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.ComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        projection.id = null;

        // When verifying the projection, an error is thrown
        assertThrows(AssertionError.class, () -> verifySchemaMatchesProjection(openAPI, schema, projection));
    }

    @Test
    @DisplayName("Verification should fail when a nested dto on the schema is missing from the reference projection")
    public void nestedDtoInSchemaMissingFromReferenceProjection() throws Exception {
        //Given a controller with a projection with the nestedDto property and a projection that is missing the nestedDto property
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/complex-projection");
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.ComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        projection.nestedDto = null;

        // When verifying the projection, an error is thrown
        assertThrows(AssertionError.class, () -> verifySchemaMatchesProjection(openAPI, schema, projection));
    }

    @Test
    @DisplayName("Verification should fail when a nested dto array on the schema is missing from the reference projection")
    public void nestedDtoArrayInSchemaMissingFromReferenceProjection() throws Exception {
        //Given a controller with a projection with the nestedDto property and a projection that is missing the nestedDtoList property
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/complex-projection");
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.ComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        projection.nestedDtoList = null;

        // When verifying the projection, an error is thrown
        assertThrows(AssertionError.class, () -> verifySchemaMatchesProjection(openAPI, schema, projection));
    }

    @Test
    @DisplayName("Verification should fail when a property within a nested dto in the schema is missing from the reference projection")
    public void propertyWithinNestedDtoInSchemaMissingFromReferenceProjection() throws Exception {
        //Given a controller with a property within the nestedDto property and a projection that is missing that property
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/complex-projection");
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.ComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        projection.nestedDto.dtoProjection.id = null;

        // When verifying the projection, an error is thrown
        assertThrows(AssertionError.class, () -> verifySchemaMatchesProjection(openAPI, schema, projection));
    }

    @Test
    @DisplayName("Verification should fail when a property within a nested dto array in the schema is missing from the reference projection")
    public void propertyWithinNestedDtoArrayInSchemaMissingFromReferenceProjection() throws Exception {
        //Given a controller with a property within the nestedDtoList property and a projection that is missing that property
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/complex-projection");
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.ComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        projection.nestedDtoList.dtoProjection.id = null;

        // When verifying the projection, an error is thrown
        assertThrows(AssertionError.class, () -> verifySchemaMatchesProjection(openAPI, schema, projection));
    }

    @Test
    @DisplayName("Verification should fail when schema property is aliased and the property in the reference projection is not aliased")
    public void propertyAliasMissingFromReferenceProjection() throws Exception {
        // Given a controller with a projection, and a projection that projects a property with a different alias than the one declared on the controller
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/aliased-complex-projection");
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.AliasedComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        projection.id = FieldConf.of(projection.id.getPresence());
        // When verifying the projection, an error is thrown
        assertThrows(AssertionError.class,() -> verifySchemaMatchesProjection(openAPI, schema, projection));
    }

    @Test
    @DisplayName("Verification should fail when schema property is aliased and the alias of the property in the reference projection does not match")
    public void propertyAliasMismatch() throws Exception {
        // Given a controller with a projection, and a projection that projects a property with a different alias than the one declared on the controller
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/aliased-complex-projection");
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.AliasedComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        projection.id = FieldConf.of(projection.id.getPresence(), "other");

        // When verifying the projection, an error is thrown
        assertThrows(AssertionError.class,() -> verifySchemaMatchesProjection(openAPI, schema, projection));
    }

    @Test
    @DisplayName("Verification should fail when nested property within schema is aliased and the alias of the reference projection property does not match")
    public void aliasOfPropertyWithinNestedDtoMismatch() throws Exception {
        // Given a controller with a projection, and a projection that projects a property with a different alias than the one declared on the controller
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/aliased-complex-projection");
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.AliasedComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        projection.nestedDto.dtoProjection.id = FieldConf.of(projection.nestedDto.dtoProjection.id.getPresence(), "other");

        // When verifying the projection, an error is thrown
        assertThrows(AssertionError.class,() -> verifySchemaMatchesProjection(openAPI, schema, projection));
    }

    @Test
    @DisplayName("Verification should fail when array item property within schema is aliased and the alias of the reference projection property does not match")
    public void aliasOfPropertyWithinNestedDtoArrayMismatch() throws Exception {
        // Given a controller with a projection, and a projection that projects a property with a different alias than the one declared on the controller
        OpenAPI openAPI = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openAPI, "/schema-verification/aliased-complex-projection");
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(SchemaVerificationTestController.AliasedComplexProjection.class.getAnnotation(DtoProjectionSpec.class));
        projection.nestedDtoList.dtoProjection.id = FieldConf.of(projection.nestedDtoList.dtoProjection.id.getPresence(), "other");

        // When verifying the projection, an error is thrown
        assertThrows(AssertionError.class,() -> verifySchemaMatchesProjection(openAPI, schema, projection));
    }

}
