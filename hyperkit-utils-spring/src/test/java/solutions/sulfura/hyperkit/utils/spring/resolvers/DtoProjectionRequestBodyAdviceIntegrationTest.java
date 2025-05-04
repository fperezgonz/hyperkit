package solutions.sulfura.hyperkit.utils.spring.resolvers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.StdDtoRequestBody;
import solutions.sulfura.hyperkit.utils.spring.StdDtoResponseBody;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for the SortArgumentResolver.
 * Tests that SortArgumentResolver and SortConverter are properly registered and can resolve Sort parameters in controller methods.
 */
@WebMvcTest(TestController.class)
public class DtoProjectionRequestBodyAdviceIntegrationTest {

    static TypeReference<StdDtoResponseBody<TestDto>> stdDtoResponseBodyTypeRef = new TypeReference<>() {
    };

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Should resolve StdRequest parameter")
    public void testShouldResolveStdRequestParameter() throws Exception {
        // Given a request with a StdRequestBody

        StdDtoRequestBody<TestDto> testDtoRequestBody = new StdDtoRequestBody<>();
        testDtoRequestBody.getData().add(new TestDto(1L, "Test", 25));

        String requestBody = objectMapper.writeValueAsString(testDtoRequestBody);

        // When the request is executed
        MvcResult result = mockMvc.perform(post("/test/test-dtos/")
                        .content(requestBody)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        // Then the response should contain a Dto with the argument projection applied
        String content = result.getResponse().getContentAsString();
        StdDtoResponseBody<TestDto> parsedResponse = objectMapper.readValue(content, stdDtoResponseBodyTypeRef);

        // Check that the response contains the projected values
        assertEquals(1, parsedResponse.getData().size());
        assertEquals(ValueWrapper.empty(), parsedResponse.getData().getFirst().id);
        assertEquals(testDtoRequestBody.getData().getFirst().age, parsedResponse.getData().getFirst().age);
        assertEquals(testDtoRequestBody.getData().getFirst().name, parsedResponse.getData().getFirst().name);
    }

    @Test
    @DisplayName("Should resolve StdResponse return value")
    public void testShouldResolveStdResponseReturnValue() throws Exception {
        // Given a GET request to /test/test-dtos/

        // When the request is executed
        MvcResult result = mockMvc.perform(get("/test/test-dtos/"))
                .andExpect(status().isOk())
                .andReturn();

        // Then the response should contain a Dto with the method projection applied
        String content = result.getResponse().getContentAsString();
        StdDtoResponseBody<TestDto> parsedResponse = objectMapper.readValue(content, stdDtoResponseBodyTypeRef);

        // Check that the response contains the property names
        assertEquals(1, parsedResponse.getData().size());
        assertEquals(1L, parsedResponse.getData().getFirst().id.get());
        assertEquals("Test Dto", parsedResponse.getData().getFirst().name.get());
        assertEquals(ValueWrapper.empty(), parsedResponse.getData().getFirst().age);
    }

}
