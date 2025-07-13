package solutions.sulfura.hyperkit.utils.spring.resolvers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import solutions.sulfura.hyperkit.utils.spring.resolvers.TestController.SortOrderData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for the SortArgumentResolver.
 * Tests that SortArgumentResolver and SortConverter are properly registered and can resolve Sort parameters in controller methods.
 */
@WebMvcTest(TestController.class)
public class SortArgumentResolverIntegrationTest {


    static TypeReference<List<SortOrderData>> orderDataTypeRef = new TypeReference<>() {};

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Should resolve Sort parameter with field:direction format")
    public void testSortArgumentResolverWithFieldDirectionFormat() throws Exception {
        // Given a request with sort parameter in field:direction format

        // When the request is executed
        MvcResult result = mockMvc.perform(get("/test/sort")
                .param("sort", "name:asc,age:desc"))
                .andExpect(status().isOk())
                .andReturn();

        // Then the response should contain the sort information
        String content = result.getResponse().getContentAsString();
        List<SortOrderData> parsedContent = objectMapper.readValue(content, orderDataTypeRef);

        // Check that the response contains the property names
        assert(parsedContent.size() == 2);
        assert(parsedContent.stream().anyMatch(order -> order.name.equals("name")));
        assert(parsedContent.stream().anyMatch(order -> order.direction.equals(Sort.Direction.ASC)));
        assert(parsedContent.stream().anyMatch(order -> order.name.equals("age")));
        assert(parsedContent.stream().anyMatch(order -> order.direction.equals(Sort.Direction.DESC)));
    }

    @Test
    @DisplayName("Should resolve Sort parameter with +/- prefix format")
    public void testSortArgumentResolverWithPrefixFormat() throws Exception {
        // Given a request with sort parameter in +/- prefix format

        // When the request is executed
        MvcResult result = mockMvc.perform(get("/test/sort")
                .param("sort", "+name,-age"))
                .andExpect(status().isOk())
                .andReturn();

        // Then the response should contain the sort information
        String content = result.getResponse().getContentAsString();
        List<SortOrderData> parsedContent = objectMapper.readValue(content, orderDataTypeRef);

        // Check that the response contains the property names
        assert(parsedContent.size() == 2);
        assert(parsedContent.stream().anyMatch(order -> order.name.equals("name")));
        assert(parsedContent.stream().anyMatch(order -> order.direction.equals(Sort.Direction.ASC)));
        assert(parsedContent.stream().anyMatch(order -> order.name.equals("age")));
        assert(parsedContent.stream().anyMatch(order -> order.direction.equals(Sort.Direction.DESC)));
    }

    @Test
    @DisplayName("Should return unsorted when sort parameter is empty")
    public void testSortArgumentResolverWithEmptyParameter() throws Exception {
        // Given a request with empty sort parameter

        // When the request is executed
        MvcResult result = mockMvc.perform(get("/test/sort")
                .param("sort", ""))
                .andExpect(status().isOk())
                .andReturn();

        // Then the response should indicate that the sort is unsorted
        String content = result.getResponse().getContentAsString();
        List<SortOrderData> parsedContent = objectMapper.readValue(content, orderDataTypeRef);
        assertTrue(parsedContent.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when sort parameter is not provided and treatNullAsUnsorted is true")
    public void testSortArgumentResolverWithNullParameterAndTreatNullAsUnsortedTrue() throws Exception {
        // Given a request without sort parameter
        // The default behavior is treatNullAsUnsorted=true

        // When the request is executed
        MvcResult result = mockMvc.perform(get("/test/sort"))
                .andExpect(status().isOk())
                .andReturn();

        // Then the response should indicate that the sort is unsorted
        String content = result.getResponse().getContentAsString();
        assertEquals("", content);
    }

}
