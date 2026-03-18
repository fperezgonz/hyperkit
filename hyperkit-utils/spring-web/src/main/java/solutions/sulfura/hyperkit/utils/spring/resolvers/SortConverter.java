package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SortConverter implements Converter<String, Sort> {

    protected Sort.Order mapFieldSpecToOrder(String fieldSpec) {

        Sort.Direction direction = Sort.Direction.ASC;
        String property = fieldSpec;

        if (fieldSpec.startsWith("-")) {
            direction = Sort.Direction.DESC;
            property = fieldSpec.substring(1).trim();
        } else if (fieldSpec.startsWith("+")) {
            property = fieldSpec.substring(1).trim();
        } else if (fieldSpec.contains(":")) {
            String[] parts = fieldSpec.split(":");
            property = parts[0].trim();
            if (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())) {
                direction = Sort.Direction.DESC;
            }
        }

        return new Sort.Order(direction, property);

    }

    @Override
    @NonNull
    public Sort convert(@NonNull String sortSpec) {

        if (sortSpec.isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = Arrays.stream(sortSpec.split(","))
                .map(String::trim)
                .filter((s) -> !s.isEmpty())
                .map(this::mapFieldSpecToOrder)
                .toList();

        return Sort.by(orders);
    }
}
