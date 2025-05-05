package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.spring.ProjectableHolder;

import java.lang.reflect.Type;

/**
 * Applies projections defined in @DtoProjectionSpec annotations to request bodies.<br>
 * If a parameter is annotated (or meta-annotated) with @RequestBody and @DtoProjectionSpec, this advice applies the projection to the parameter.
 */
@SuppressWarnings({"NullableProblems", "rawtypes"})
@Component
@ControllerAdvice
public class DtoProjectionRequestBodyAdvice implements RequestBodyAdvice {

    public DtoProjectionRequestBodyAdvice() {
    }

    @Override
    public boolean supports(MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        if (parameter.hasParameterAnnotation(DtoProjectionSpec.class)) {
            return true;
        }

        // Check if the parameter is meta-annotated with DtoProjectionSpec
        for (var annotation : parameter.getParameterAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(DtoProjectionSpec.class)) {
                return true;
            }
        }

        return false;

    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        DtoProjectionSpec projectionAnnotation = parameter.getParameterAnnotation(DtoProjectionSpec.class);

        // If the parameter is not directly annotated, check if it has a meta-annotation
        if (projectionAnnotation == null) {
            for (var annotation : parameter.getParameterAnnotations()) {
                projectionAnnotation = annotation.annotationType().getAnnotation(DtoProjectionSpec.class);
                if (projectionAnnotation != null) {
                    break;
                }
            }
        }

        try {

            if (body instanceof Dto<?> dto) {

                //noinspection ReassignedVariable,DataFlowIssue
                return applyProjection(dto, projectionAnnotation);
            }

            if (body instanceof ProjectableHolder projectableHolder) {

                var projectables = projectableHolder.listProjectables();

                for (var projectable : projectables) {
                    if (!(projectable instanceof Dto<?> dto)) {
                        throw new RuntimeException("Unsupported projectable type: " + projectable.getClass() + ". Only classes that extend Dto are supported");
                    }

                    //noinspection ReassignedVariable,DataFlowIssue
                    applyProjection(dto, projectionAnnotation);
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

    @NonNull
    public Dto<?> applyProjection(Dto<?> dto, @NonNull DtoProjectionSpec projectionAnnotation) {

        Class<? extends DtoProjection> projectionClass = findProjectionClass(dto);

        if (projectionClass == null) {
            throw new RuntimeException("Failed to find projection class for DTO of type: " + dto.getClass());
        }

        DtoProjection projection = ProjectionDsl.parse(projectionAnnotation.value(), projectionClass);

        //noinspection unchecked
        projection.applyProjectionTo(dto);

        return dto;
    }

    /**
     * Finds the projection class for the given DTO.
     *
     * @param dto The DTO to find the projection class for
     * @return The projection class, or null if not found
     */
    @SuppressWarnings("unchecked")
    private Class<? extends DtoProjection> findProjectionClass(Dto<?> dto) {
        Class<?> dtoClass = dto.getClass();

        // Look for a nested class that extends DtoProjection
        for (Class<?> nestedClass : dtoClass.getDeclaredClasses()) {
            if (DtoProjection.class.isAssignableFrom(nestedClass)) {
                return (Class<? extends DtoProjection>) nestedClass;
            }
        }

        return null;
    }

}
