package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.utils.spring.ProjectableHolder;

import java.util.ArrayList;

import static solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils.getMethodProjectionAnnotationOrMetaAnnotation;

/**
 * Applies projections defined in @DtoProjectionSpec annotations to response bodies.<br>
 * If a method is annotated (or meta-annotated) with @DtoProjectionSpec, this advice applies the projection to the response body.
 */
@Component
@ControllerAdvice
public class DtoProjectionResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final DtoProjectionRequestBodyAdvice dtoProjectionRequestBodyAdvice;

    public DtoProjectionResponseBodyAdvice(DtoProjectionRequestBodyAdvice dtoProjectionRequestBodyAdvice) {
        this.dtoProjectionRequestBodyAdvice = dtoProjectionRequestBodyAdvice;
    }

    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {

        if (returnType.getMethod() == null) {
            return false;
        }

        return getMethodProjectionAnnotationOrMetaAnnotation(returnType.getMethod()) != null;

    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        if (returnType.getMethod() == null) {
            return body;
        }

        DtoProjectionSpec projectionAnnotation = getMethodProjectionAnnotationOrMetaAnnotation(returnType.getMethod());

        if (projectionAnnotation == null) {
            throw new RuntimeException("Failed to find projection annotation for method: " + returnType.getMethod().getName());
        }

        try {

            if (body instanceof Dto<?> dto) {
                return dtoProjectionRequestBodyAdvice.applyProjection(dto, projectionAnnotation);
            }

            //noinspection rawtypes
            if (body instanceof ProjectableHolder projectableHolder) {
                var projectables = projectableHolder.getProjectables();
                var projectionResults = new ArrayList<Dto<?>>();

                for (var projectable : projectables) {
                    if (!(projectable instanceof Dto<?> dto)) {
                        throw new RuntimeException("Unsupported projectable type: " + projectable.getClass() + ". Only classes that extend Dto are supported");
                    }

                    projectionResults.add(dtoProjectionRequestBodyAdvice.applyProjection(dto, projectionAnnotation));
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