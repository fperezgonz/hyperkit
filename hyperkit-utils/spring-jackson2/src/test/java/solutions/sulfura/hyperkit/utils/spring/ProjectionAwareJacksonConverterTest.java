package solutions.sulfura.hyperkit.utils.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.utils.test.model.dtos.AuthorizationDto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class ProjectionAwareJacksonConverterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("OpenApi generation should apply projection aliases to request body parameter model")
    public void testOpenApiShouldApplyProjectionAliasesToRequestBodyParameterModel() throws Exception {
        // Given a controller with a projection annotation on a Dto

        mockMvc.perform(post("/jackson2/field-alias/simple-property-projection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {"code":"AdminAuth"}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"AdminAuth\"}")
                );
    }

}

@WebMvcTest(controllers = FieldAliasDtoProjectionOnRequestTestController.class)
@Import({SpringTestConfig.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_0_ProjectionAwareJacksonConverterTest extends ProjectionAwareJacksonConverterTest {
}

@WebMvcTest(controllers = FieldAliasDtoProjectionOnRequestTestController.class)
@Import({SpringTestConfig.class})
@SuppressWarnings("NewClassNamingConvention")
class OpenApi_3_1_ProjectionAwareJacksonConverterTest extends ProjectionAwareJacksonConverterTest {
}

@DtoProjectionSpec(projectedClass = AuthorizationDto.class, value = """
        name as code
        """)
@Retention(RetentionPolicy.RUNTIME)
@interface SimplePropertyProjection {
}


/**
 * Uses projection {@link SimplePropertyProjection}.
 */
@RestController
class FieldAliasDtoProjectionOnRequestTestController {
    @PostMapping("/jackson2/field-alias/simple-property-projection")
    public HttpEntity<AuthorizationDto> postAuthorizationDto(
            @SimplePropertyProjection
            @RequestBody AuthorizationDto testDto) {
        return new HttpEntity<>(testDto);
    }

}
