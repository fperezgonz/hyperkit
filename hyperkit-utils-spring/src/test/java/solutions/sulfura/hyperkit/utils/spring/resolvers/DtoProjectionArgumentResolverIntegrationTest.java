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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for the SortArgumentResolver.
 * Tests that SortArgumentResolver and SortConverter are properly registered and can resolve Sort parameters in controller methods.
 */
@WebMvcTest(TestController.class)
public class DtoProjectionArgumentResolverIntegrationTest {

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
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        // Then the response should contain the sort information
        String content = result.getResponse().getContentAsString();
        StdDtoResponseBody<TestDto> parsedResponse = objectMapper.readValue(content, stdDtoResponseBodyTypeRef);

        // Check that the response contains the property names
        assertEquals(1, parsedResponse.getData().size());
        assertEquals(ValueWrapper.empty(), parsedResponse.getData().getFirst().id);
        assertEquals(testDtoRequestBody.getData().getFirst().age, parsedResponse.getData().getFirst().age);
        assertEquals(testDtoRequestBody.getData().getFirst().name, parsedResponse.getData().getFirst().name);
    }

//    @Test
//    @DisplayName("Should resolve Sort parameter with +/- prefix format")
//    public void testSortArgumentResolverWithPrefixFormat() throws Exception {
//        // Given a request with sort parameter in +/- prefix format
//
//        // When the request is executed
//        MvcResult result = mockMvc.perform(get("/test/test-dto")
//                        .param("sort", "+name,-age"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Then the response should contain the sort information
//        String content = result.getResponse().getContentAsString();
//        List<SortOrderData> parsedContent = objectMapper.readValue(content, orderDataTypeRef);
//
//        // Check that the response contains the property names
//        assert (parsedContent.size() == 2);
//        assert (parsedContent.stream().anyMatch(order -> order.name.equals("name")));
//        assert (parsedContent.stream().anyMatch(order -> order.direction.equals(Sort.Direction.ASC)));
//        assert (parsedContent.stream().anyMatch(order -> order.name.equals("age")));
//        assert (parsedContent.stream().anyMatch(order -> order.direction.equals(Sort.Direction.DESC)));
//    }
//
//    @Test
//    @DisplayName("Should return unsorted when sort parameter is empty")
//    public void testSortArgumentResolverWithEmptyParameter() throws Exception {
//        // Given a request with an empty sort parameter
//
//        // When the request is executed
//        MvcResult result = mockMvc.perform(get("/test/test-dto")
//                        .param("sort", ""))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Then the response should indicate that the sort is unsorted
//        String content = result.getResponse().getContentAsString();
//        List<SortOrderData> parsedContent = objectMapper.readValue(content, orderDataTypeRef);
//        assertTrue(parsedContent.isEmpty());
//    }
//
//    @Test
//    @DisplayName("Should return empty list when sort parameter is not provided and treatNullAsUnsorted is true")
//    public void testSortArgumentResolverWithNullParameterAndTreatNullAsUnsortedTrue() throws Exception {
//        // Given a request without sort parameter
//        // The default behavior is treatNullAsUnsorted=true
//
//        // When the request is executed
//        MvcResult result = mockMvc.perform(get("/test/test-dto"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Then the response should indicate that the sort is unsorted
//        String content = result.getResponse().getContentAsString();
//        assertEquals("", content);
//    }

}
