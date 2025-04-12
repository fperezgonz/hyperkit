package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Resolves sort parameters in the forms field1:direction,field2:direction (e.g. name:asc,date:desc = order by name ASC and date DESC)
 * or +-field1,+-field2 (e.g. +name,-date = order by name ASC, and date DESC)<br>
 * <p>
 * Note that Spring tries to resolve parameters annotated with {@link RequestParam} using a {@link Converter} first,
 * and it has a converter for Sort parameters. To cover this case, you must also register the SortConverter
 */
@Component
public class SortArgumentResolver implements HandlerMethodArgumentResolver {

    private final SortConverter converter;

    public SortArgumentResolver(SortConverter converter) {
        this.converter = converter;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // Resolving only parameters of type org.springframework.data.domain.Sort
        return parameter.getParameterType().equals(Sort.class);
    }

    @Override
    public Sort resolveArgument(MethodParameter parameter,
                                ModelAndViewContainer mavContainer,
                                @NonNull NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) {

        String sortParamName = "";
        var requestParamAnn = parameter.getParameterAnnotation(RequestParam.class);

        //Try to resolve the parameter name from the annotation's attributes
        if (requestParamAnn != null) {

            //First try the "name" attribute
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

        //Default value from annotation when the request parameter is null
        if (sortParam == null && requestParamAnn != null) {

            sortParam = requestParamAnn.defaultValue();

            // Check if it's the default "magic" value used by Spring
            if ("\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n".equals(sortParam)) {
                sortParam = null;
            }

        }

        //Handle null value
        if(sortParam == null) {

            //If the parameter is required but not found, throw an exception
            if (requestParamAnn != null && requestParamAnn.required()) {
                throw new IllegalArgumentException("Missing required parameter: " + sortParamName);
            }

            return null;

        }

        //Parses order definition in the forms field:direction,... (e.g.: "name:asc,age:desc") or +-field (e.g.:"+name,-age")
        return converter.convert(sortParam);

    }

}