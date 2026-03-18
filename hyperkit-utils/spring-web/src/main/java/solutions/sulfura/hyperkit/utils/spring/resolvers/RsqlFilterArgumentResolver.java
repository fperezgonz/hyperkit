package solutions.sulfura.hyperkit.utils.spring.resolvers;

import io.github.perplexhub.rsql.RSQLJPASupport;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import solutions.sulfura.hyperkit.utils.spring.ParameterUtils;

/**
 * Resolves RSQL filter strings to JPA Specifications.
 * This resolver supports method parameters of type Specification.class.
 */
@Component
public class RsqlFilterArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // Resolving only parameters of type org.springframework.data.jpa.domain.Specification
        return Specification.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        String filterParamName = ParameterUtils.getParameterName(parameter);
        var requestParamAnn = parameter.getParameterAnnotation(RequestParam.class);

        // Default parameter name
        if (filterParamName == null || filterParamName.isEmpty()) {
            filterParamName = "filter";
        }

        String filterParam = webRequest.getParameter(filterParamName);

        if (filterParam == null || filterParam.isEmpty()) {
            if (requestParamAnn != null) {
                filterParam = requestParamAnn.defaultValue();
                // Check if it's the default "magic" value used by Spring
                if ("\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n".equals(filterParam)) {
                    filterParam = null;
                }
            }
        }

        if (filterParam == null || filterParam.isEmpty()) {
            if (requestParamAnn != null && requestParamAnn.required()) {
                return null;
            }
            return null;
        }

        // Convert RSQL filter string to JPA Specification
        return RSQLJPASupport.toSpecification(filterParam, true);

    }

}
