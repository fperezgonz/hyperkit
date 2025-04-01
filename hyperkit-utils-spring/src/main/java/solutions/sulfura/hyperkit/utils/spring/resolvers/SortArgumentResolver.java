package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SortArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // Resolving only parameters of type org.springframework.data.domain.Sort
        return parameter.getParameterType().equals(Sort.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        String sortParamName = "";
        var requestParamAnn = parameter.getParameterAnnotation(RequestParam.class);

        if (requestParamAnn != null) {

            sortParamName = requestParamAnn.name();

            if (sortParamName.isEmpty()) {
                sortParamName = requestParamAnn.value();
            }

        }

        //Default parameter name
        if (sortParamName.isEmpty()) {
            sortParamName = "sort";
        }

        String sortParam = webRequest.getParameter(sortParamName);

        if (sortParam == null || sortParam.isEmpty()) {
            if (requestParamAnn != null) {
                sortParam = requestParamAnn.defaultValue();
                // Check if it's the default "magic" value used by Spring
                if ("\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n".equals(sortParam)) {
                    sortParam = null;
                }
            }
        }

        if (sortParam == null || sortParam.isEmpty()) {

            if (requestParamAnn != null && requestParamAnn.required()) {
                return null;
//                throw new IllegalArgumentException("Missing required parameter: " + sortParamName);
            }

            return Sort.unsorted();

        }

        //Parses order definition in the forms field:direction,... (e.g.: "name:asc,age:desc") or +-field (e.g.:"+name,-age")
        List<Sort.Order> orders = Arrays.stream(sortParam.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(fieldSpec -> {
                    Sort.Direction direction = Sort.Direction.ASC;
                    String property = fieldSpec;

                    if (fieldSpec.startsWith("-")) {
                        direction = Sort.Direction.DESC;
                        property = fieldSpec.substring(1).trim();
                    } else if (fieldSpec.startsWith("+")) {
                        property = fieldSpec.substring(1).trim();
                    } else if (fieldSpec.contains(":")) {
                        // handle original format "field,direction"
                        String[] parts = fieldSpec.split(":");
                        property = parts[0].trim();
                        if (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())) {
                            direction = Sort.Direction.DESC;
                        }
                    }

                    return new Sort.Order(direction, property);
                })
                .collect(Collectors.toList());

        return Sort.by(orders);

    }
}