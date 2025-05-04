package solutions.sulfura.hyperkit.utils.spring.resolvers;

import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;

import static solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils.getMethodProjectionAnnotationOrMetaAnnotation;

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
                                  @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getMethod() == null) {
            return null;
        }

        // Get the method's return type annotation
        DtoProjectionSpec projectionAnnotation = getMethodProjectionAnnotationOrMetaAnnotation(parameter.getMethod());

        if (projectionAnnotation == null) {
            return null;
        }

        // Parse the projection specification and create a DtoProjection instance
        //noinspection rawtypes,unchecked
        return ProjectionDsl.parse(projectionAnnotation.value(), (Class<DtoProjection>) parameter.getParameterType());

    }

}