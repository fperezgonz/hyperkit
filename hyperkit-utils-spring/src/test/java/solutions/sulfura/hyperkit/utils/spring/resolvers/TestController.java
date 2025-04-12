package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Test controller for integration testing of the SortArgumentResolver
 */
@RestController
@RequestMapping("/test")
public class TestController {

    public static class SortOrderData {

        public String name;
        public Sort.Direction direction;

        public SortOrderData(Sort.Order order) {
            this.name = order.getProperty();
            this.direction = order.getDirection();
        }

        public SortOrderData() {
        }
    }

    @GetMapping("/sort")
    public List<SortOrderData> testSort(@RequestParam(value = "sort", required = false) Sort sort) {

        if (sort == null) {
            return null;
        }

        return sort.get()
                .map(SortOrderData::new)
                .toList();

    }

}
