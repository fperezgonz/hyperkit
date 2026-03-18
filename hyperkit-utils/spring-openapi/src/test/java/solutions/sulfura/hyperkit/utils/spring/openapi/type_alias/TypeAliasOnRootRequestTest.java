package solutions.sulfura.hyperkit.utils.spring.openapi.type_alias;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfig;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_0;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_1;
import solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils;
import solutions.sulfura.hyperkit.utils.spring.openapi.model.TestDto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static solutions.sulfura.hyperkit.utils.spring.TestUtils.*;

public abstract class TypeAliasOnRootRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("OpenApi generation should apply type aliases on the root projection to types in the openapi model")
    public void testOpenApiShouldApplyTypeAliasOnRootProjection() throws Exception {
        // Given a controller with a projection annotation on a Dto

        // When we get the OpenAPI spec
        OpenAPI openApi = getOpenApi(mockMvc);

        // Then the model aliases should show up on the openApi spec
        Schema<?> aliasedRootModelSchema = openApi.getComponents().getSchemas().get("Aliased_RootModel");
        assertNotNull(aliasedRootModelSchema, "Aliased_RootModel schema should be present in OpenAPI spec");

        // Then the model name for the parameter should match the alias
        Schema<?> schema = getSchemaForPath(openApi, "/type-alias/test-aliased-root-request");
        schema = SchemaBuilderUtils.findReferencedModel(openApi, schema);
        assertEquals(aliasedRootModelSchema, schema);
        TestDto.Projection projection = (TestDto.Projection) ProjectionDsl.parse(AliasedRootProjection.class.getAnnotation(DtoProjectionSpec.class));
        verifySchemaMatchesProjection(openApi, schema, projection);

    }

}

@WebMvcTest(controllers = AliasedRootDtoTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_0.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_0_TypeAliasOnRootRequestTest extends TypeAliasOnRootRequestTest {
}

@WebMvcTest(controllers = AliasedRootDtoTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_1.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_1_TypeAliasOnRootRequestTest extends TypeAliasOnRootRequestTest {
}

@DtoProjectionSpec(projectedClass = TestDto.class, namespace = "Aliased", value = "{id}:RootModel")
@Retention(RetentionPolicy.RUNTIME)
@interface AliasedRootProjection {
}

/**
 * Uses projection {@link AliasedRootProjection}.
 */
@RestController
class AliasedRootDtoTestController {
    @PostMapping("/type-alias/test-aliased-root-request")
    public HttpEntity<TestDto> postTestDto(
            @AliasedRootProjection
            @RequestBody TestDto testDto) {
        return new HttpEntity<>(testDto);
    }
}