package solutions.sulfura.hyperkit.starter.config.jackson2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson2.autoconfigure.Jackson2AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import solutions.sulfura.hyperkit.utils.spring.ProjectionAwareJacksonConverter;
import solutions.sulfura.hyperkit.utils.test.model.dtos.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({HyperKitJackson2AutoConfigTest.class, Jackson2AutoConfiguration.class})
public class ProjectionAwareJacksonJsonConverterTest {

    @MockitoSpyBean
    ProjectionAwareJacksonConverter jackson2Converter;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldUseProjectionAwareJacksonJsonConverter() throws Exception {
        mockMvc.perform(post("/hyperkit-config/jackson2/projection-aware")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"John Doe","email":"john.doe@example.com"}
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"username":"John Doe","email":"john.doe@example.com"}
                        """));

        verify(jackson2Converter, atLeastOnce()).write(any(), any(), any(), any());
        verify(jackson2Converter, atLeastOnce()).read(any(), any(), any());
    }
}

@RestController
class TestController {
    @PostMapping("/hyperkit-config/jackson2/projection-aware")
    public UserDto postUserDto(@RequestBody UserDto userDto) {
        return userDto;
    }

}