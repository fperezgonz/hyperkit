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

@WebMvcTest(controllers ={ FieldAliasDtoProjectionOnRequestTestController.class, ComplexFieldAliasDtoProjectionOnRequestTestController.class})
@Import({SpringTestConfig.class})
public class ProjectionAwareJacksonConverterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Projection field alias should be applied to deserialization of simple properties in Dtos")
    public void testProjectionFieldAliasIsAppliedInControllerForSimpleProperty() throws Exception {
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

    @Test
    @DisplayName("Projection field alias should be applied to deserialization of complex projection structures in Dtos")
    public void testProjectionFieldAliasIsAppliedInControllerForMultipleProperties() throws Exception {
        // Given a controller with a projection annotation on a Dto

        mockMvc.perform(post("/jackson2/field-alias/complex-projection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {"id":"1","code":"AdminAuth","resourceReferences":[{"value":{"resId":"1","name":"Resource1"}}],"role":{"id":"1","name":"Admin","actions":[{"value":{"actionId":"1","name":"Read"}}]}}
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"1\",\"name\":\"AdminAuth\",\"resourceReferences\":[{\"value\":{\"id\":\"1\",\"name\":\"Resource1\"}}],\"role\":{\"id\":\"1\",\"name\":\"Admin\",\"actions\":[{\"value\":{\"id\":\"1\",\"name\":\"Read\"}}]}}")
                );
    }

}




@DtoProjectionSpec(projectedClass = AuthorizationDto.class, value = """
        name as code
        """)
@Retention(RetentionPolicy.RUNTIME)
@interface SimplePropertyProjection {
}

@RestController
class FieldAliasDtoProjectionOnRequestTestController {
    @PostMapping("/jackson2/field-alias/simple-property-projection")
    public HttpEntity<AuthorizationDto> postAuthorizationDto(
            @SimplePropertyProjection
            @RequestBody AuthorizationDto testDto) {
        return new HttpEntity<>(testDto);
    }

}

@DtoProjectionSpec(projectedClass = AuthorizationDto.class, value = """
        id
        name as code
        resourceReferences { id as resId, name as name }
        role { id, name, actions { id as actionId, name } }
        """)
@Retention(RetentionPolicy.RUNTIME)
@interface ComplexPropertyProjection {
}

@RestController
class ComplexFieldAliasDtoProjectionOnRequestTestController {
    @PostMapping("/jackson2/field-alias/complex-projection")
    public HttpEntity<AuthorizationDto> postAuthorizationDto(
            @ComplexPropertyProjection
            @RequestBody
            AuthorizationDto testDto) {
        return new HttpEntity<>(testDto);
    }

}
