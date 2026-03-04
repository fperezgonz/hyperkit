package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionAnnotationCache;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionCache;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.utils.spring.ProjectableHolder;

import java.lang.reflect.Type;
import java.util.Optional;

import static solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils.applyProjection;

/**
 * Applies projections defined in @DtoProjectionSpec annotations to request bodies.<br>
 * If a parameter is annotated (or meta-annotated) with @RequestBody and @DtoProjectionSpec, this advice applies the projection to the parameter.
 */
@SuppressWarnings({"NullableProblems", "rawtypes"})
@ControllerAdvice
public class DtoProjectionRequestBodyAdvice implements RequestBodyAdvice {

    private final ProjectionCache projectionCache;
    private final ProjectionAnnotationCache projectionAnnotationCache;

    public DtoProjectionRequestBodyAdvice(Optional<ProjectionCache> projectionCache,
                                          Optional<ProjectionAnnotationCache> projectionAnnotationCache) {
        this.projectionCache = projectionCache.orElse(null);
        this.projectionAnnotationCache = projectionAnnotationCache.orElse(null);
    }

    @Override
    public boolean supports(MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        return getProjectionAnnotation(parameter) != null;

    }

    private DtoProjectionSpec getProjectionAnnotation(MethodParameter parameter) {
        if (projectionAnnotationCache != null) {
            return projectionAnnotationCache.getParameterAnnotation(parameter.getParameter());
        }
        var annotationInfo = ProjectionUtils.getAnnotationInfo(parameter.getParameter(), DtoProjectionSpec.class);
        return annotationInfo == null ? null : annotationInfo.targetAnnotation;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        DtoProjectionSpec projectionAnnotation = getProjectionAnnotation(parameter);

        try {

            if (body instanceof Dto<?> dto) {
                return applyProjection(dto, projectionAnnotation, projectionCache);
            }

            if (body instanceof ProjectableHolder projectableHolder) {

                var projectables = projectableHolder.listProjectables();

                for (var projectable : projectables) {

                    if (!(projectable instanceof Dto<?> dto)) {
                        throw new RuntimeException("Unsupported projectable type: " + projectable.getClass() + ". Only classes that extend Dto are supported");
                    }

                    applyProjection(dto, projectionAnnotation, projectionCache);

                }

                return projectableHolder;

            }

            return body;

        } catch (RuntimeException e) {

            throw new RuntimeException("Failed applying projections to argument. Class: " + parameter.getMethod().getDeclaringClass() +
                    ", method: " + parameter.getMethod().getName() +
                    ", parameter:" + parameter.getParameterName()
                    , e);

        }

    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

}
