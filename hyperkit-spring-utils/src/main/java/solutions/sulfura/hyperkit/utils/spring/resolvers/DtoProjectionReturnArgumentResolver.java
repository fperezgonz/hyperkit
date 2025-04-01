package solutions.sulfura.hyperkit.utils.spring.resolvers;

import jakarta.annotation.Nonnull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;

/**
 * Resolves DtoProjection parameters in controller methods by extracting the projection
 * information from the method's return type annotation.
 */
@Component
public class DtoProjectionReturnArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return DtoProjection.class.isAssignableFrom(parameter.getParameterType());
    }

    /**
     * Resolves DtoProjection parameters by extracting the projection information from
     * the method's return type annotation.
     */
    @Override
    public Object resolveArgument(@Nonnull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                 NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getMethod() == null) {
            return null;
        }

        // Get the method's return type annotation
        DtoProjectionSpec projectionAnnotation = parameter.getMethod().getAnnotation(DtoProjectionSpec.class);

        // If the method itself doesn't have the annotation, check if it has a meta-annotation
        if (projectionAnnotation == null) {
            for (var annotation : parameter.getMethod().getAnnotations()) {
                projectionAnnotation = annotation.annotationType().getAnnotation(DtoProjectionSpec.class);
                if (projectionAnnotation != null) {
                    break;
                }
            }
        }

        if (projectionAnnotation == null) {
            return null;
        }

        // Parse the projection specification and create a DtoProjection instance
        return ProjectionDsl.parse(projectionAnnotation.value(), (Class<DtoProjection>) parameter.getParameterType());

    }

}