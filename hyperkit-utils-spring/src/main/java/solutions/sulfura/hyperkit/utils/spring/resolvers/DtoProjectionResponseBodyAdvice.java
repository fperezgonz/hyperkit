package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionAnnotationCache;
import solutions.sulfura.hyperkit.dsl.projections.CachedProjectionParser;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.utils.spring.ProjectableHolder;

import java.util.Optional;

/**
 * Applies projections defined in @DtoProjectionSpec annotations to response bodies.<br>
 * If a method is annotated (or meta-annotated) with @DtoProjectionSpec, this advice applies the projection to the response body.
 */
@ControllerAdvice
public class DtoProjectionResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final CachedProjectionParser cachedProjectionParser;
    private final ProjectionAnnotationCache projectionAnnotationCache;

    public DtoProjectionResponseBodyAdvice(Optional<CachedProjectionParser> projectionCache,
                                           Optional<ProjectionAnnotationCache> projectionAnnotationCache) {
        this.cachedProjectionParser = projectionCache.orElse(null);
        this.projectionAnnotationCache = projectionAnnotationCache.orElse(null);
    }

    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {

        return getProjectionAnnotation(returnType) != null;

    }

    private DtoProjectionSpec getProjectionAnnotation(MethodParameter returnType) {

        if (returnType.getMethod() == null) {
            return null;
        }

        if (projectionAnnotationCache != null) {
            return projectionAnnotationCache.getReturnTypeAnnotation(returnType.getMethod());
        }

        return ProjectionUtils.getMethodProjectionAnnotationOrMetaAnnotation(returnType.getMethod());

    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {

        DtoProjectionSpec projectionAnnotation = getProjectionAnnotation(returnType);

        if (projectionAnnotation == null) {
            throw new RuntimeException("Failed to find projection annotation for method: " + returnType.getMethod().getName());
        }

        try {

            if (body instanceof Dto<?> dto) {
                return ProjectionUtils.applyProjection(dto, projectionAnnotation, cachedProjectionParser);
            }

            //noinspection rawtypes
            if (body instanceof ProjectableHolder projectableHolder) {
                var projectables = projectableHolder.listProjectables();

                for (var projectable : projectables) {
                    if (!(projectable instanceof Dto<?> dto)) {
                        throw new RuntimeException("Unsupported projectable type: " + projectable.getClass() + ". Only classes that extend Dto are supported");
                    }

                    ProjectionUtils.applyProjection(dto, projectionAnnotation, cachedProjectionParser);
                }

                return projectableHolder;
            }

            return body;
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed applying projections to response. Class: " + returnType.getMethod().getDeclaringClass() +
                    ", method: " + returnType.getMethod().getName(), e);
        }
    }
}