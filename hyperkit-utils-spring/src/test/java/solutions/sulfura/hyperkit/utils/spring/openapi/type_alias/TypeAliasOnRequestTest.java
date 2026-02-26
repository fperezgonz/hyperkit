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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfig;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_0;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_1;
import solutions.sulfura.hyperkit.utils.spring.openapi.model.TestDto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static solutions.sulfura.hyperkit.utils.spring.TestUtils.*;

public abstract class TypeAliasOnRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("OpenApi generation should apply type aliases to types in the openapi model")
    public void testOpenApiShouldApplyTypeAliasToModel() throws Exception {
        // Given a controller with a projection annotation on a Dto

        // When we get the OpenAPI spec
        OpenAPI openApi = getOpenApi(mockMvc);

        // Then the model aliases should show up on the openApi spec
        Schema<?> aliasedModelSchema = openApi.getComponents().getSchemas().get("Aliased_TestDto_AliasedModel");
        assertNotNull(aliasedModelSchema, "Aliased_AliasedModel schema should be present in OpenAPI spec");
        Schema<?> aliasedItemModelSchema = openApi.getComponents().getSchemas().get("Aliased_TestDto_AliasedItemModel");
        assertNotNull(aliasedItemModelSchema, "Aliased_AliasedItemModel schema should be present in OpenAPI spec");

        // Then the model name for the parameter should match the alias
        Schema<?> schema = getSchemaForPath(openApi, "/type-alias/test-type-alias-on-request");
        //TODO
        fail();

    }

}

@WebMvcTest(controllers = TypeAliasDtoProjectionOnRequestTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_0.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_0_Type_AliasOnRequestTest extends TypeAliasOnRequestTest {
}

@WebMvcTest(controllers = TypeAliasDtoProjectionOnRequestTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_1.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_1_Type_AliasOnRequestTest extends TypeAliasOnRequestTest {
}

@DtoProjectionSpec(projectedClass = TestDto.class, namespace = "Aliased", value = """
        name as code
        age
        nestedDto:AliasedModel{id}
        nestedDtoList:AliasedItemModel{id}
        """)
@Retention(RetentionPolicy.RUNTIME)
@interface AliasedTypeProjection {
}


/**
 * Uses projection {@link AliasedTypeProjection}.
 */
@RestController
class TypeAliasDtoProjectionOnRequestTestController {
    @PostMapping("/type-alias/test-type-alias-on-request")
    public HttpEntity<TestDto> postTestDto(
            @AliasedTypeProjection
            @RequestBody TestDto testDto) {
        return new HttpEntity<>(testDto);
    }

}