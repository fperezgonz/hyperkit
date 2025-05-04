package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.utils.spring.StdDtoRequestBody;
import solutions.sulfura.hyperkit.utils.spring.StdDtoResponseBody;

import java.util.List;

/**
 * Test controller for integration testing of SortArgumentResolver and DtoProjectionArgumentResolver
 */
@RestController
public class TestController {

    public static class SortOrderData {

        public String name;
        public Sort.Direction direction;

        public SortOrderData(Sort.Order order) {
            this.name = order.getProperty();
            this.direction = order.getDirection();
        }

        @SuppressWarnings("unused")
        public SortOrderData() {
        }
    }

    @GetMapping("/test/sort")
    public List<SortOrderData> testSort(@RequestParam(value = "sort", required = false) Sort sort) {

        if (sort == null) {
            return null;
        }

        return sort.get()
                .map(SortOrderData::new)
                .toList();

    }

    // Endpoints for projection tests

    /**
     * Applies a projection {name, age} to the request body and returns the result
     */
    @PostMapping("/test/test-dtos/")
    public HttpEntity<StdDtoResponseBody<TestDto>> testArgumentProjection(
            @DtoProjectionSpec(projectedClass = TestDto.class, value = "name, age")
            @RequestBody
            StdDtoRequestBody<TestDto> testDtoRequestBody) {

        StdDtoResponseBody<TestDto> testDtoResponseBody = new StdDtoResponseBody<>();
        testDtoResponseBody.setData(testDtoRequestBody.getData());

        return new HttpEntity<>(testDtoResponseBody);

    }

    /**
     * Applies a projection {name, age} to the result
     */
    @GetMapping("/test/test-dtos/")
    @DtoProjectionSpec(projectedClass = TestDto.class, value = "id, name")
    public HttpEntity<StdDtoResponseBody<TestDto>> testArgumentProjection() {

        StdDtoResponseBody<TestDto> testDtoResponseBody = new StdDtoResponseBody<>();
        testDtoResponseBody.setData(List.of(new TestDto(1L, "Test Dto", 25)));

        return new HttpEntity<>(testDtoResponseBody);

    }

}
