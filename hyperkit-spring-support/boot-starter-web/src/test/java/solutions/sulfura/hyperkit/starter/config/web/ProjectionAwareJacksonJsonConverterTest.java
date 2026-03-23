package solutions.sulfura.hyperkit.starter.config.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import solutions.sulfura.hyperkit.utils.spring.jackson3.ProjectionAwareJacksonJsonConverter;
import solutions.sulfura.hyperkit.utils.test.model.dtos.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(HyperKitWebAutoConfig.class)
public class ProjectionAwareJacksonJsonConverterTest {


    @MockitoSpyBean
    ProjectionAwareJacksonJsonConverter converter;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldUseProjectionAwareJacksonJsonConverter() throws Exception {
        mockMvc.perform(post("/hyperkit-config/jackson3/projection-aware")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"John Doe","email":"john.doe@example.com"}
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"username":"John Doe","email":"john.doe@example.com"}
                        """));

        verify(converter, atLeastOnce()).write(any(), any(), any(), any(), any());
        verify(converter, atLeastOnce()).read(any(), any(), any());
    }
}

@RestController
class TestController {
    @PostMapping("/hyperkit-config/jackson3/projection-aware")
    public UserDto postUserDto(@RequestBody UserDto userDto) {
        return userDto;
    }

}