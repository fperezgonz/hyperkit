package solutions.sulfura.hyperkit.utils.spring.openapi.field_alias;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfig;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_0;
import solutions.sulfura.hyperkit.utils.spring.SpringTestConfigOpenApi_3_1;
import solutions.sulfura.hyperkit.utils.spring.openapi.model.TestDto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static solutions.sulfura.hyperkit.utils.spring.TestUtils.*;

public abstract class FieldAliasOnResponseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("OpenApi generation should apply projection aliases to response body model")
    public void testOpenApiShouldApplyProjectionAliasesToResponseBodyModel() throws Exception {
        // Given a controller with a projection annotation on a method returning a Dto

        // When we get the OpenAPI spec
        OpenAPI openApi = getOpenApi(mockMvc);
        Schema<?> schema = getSchemaForPath(openApi, "/field-alias/test-field-alias-on-response");

        // Then the schema for the response should match expectations
        DtoProjection<?> projection = ProjectionDsl.parse(TestDtoProjectionOnResponse.class.getAnnotation(DtoProjectionSpec.class));
        verifySchemaMatchesProjection(openApi, schema, projection);
    }
}

@WebMvcTest(controllers = FieldAliasDtoProjectionOnResponseTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_0.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_0_Field_AliasOnResponseTest extends FieldAliasOnResponseTest {
}

@WebMvcTest(controllers = FieldAliasDtoProjectionOnResponseTestController.class)
@Import({SpringTestConfig.class, SpringDocConfiguration.class, SpringDocWebMvcConfiguration.class, SpringTestConfigOpenApi_3_1.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_1_Field_AliasOnResponseTest extends FieldAliasOnResponseTest {
}

@DtoProjectionSpec(projectedClass = TestDto.class, value = """
        name as code
        age
        nestedDto{id as nestedId}
        nestedDtoList{id as nestedId}
        """)
@Retention(RetentionPolicy.RUNTIME)
@interface TestDtoProjectionOnResponse {
}

@RestController
class FieldAliasDtoProjectionOnResponseTestController {
    @GetMapping("/field-alias/test-field-alias-on-response")
    @TestDtoProjectionOnResponse
    public HttpEntity<TestDto> getTestDto() {
        return new HttpEntity<>(new TestDto());
    }
}
