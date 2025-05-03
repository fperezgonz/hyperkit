package solutions.sulfura.hyperkit.utils.spring.resolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjectionException;
import solutions.sulfura.hyperkit.utils.spring.ProjectableHolder;

import java.util.ArrayList;

/**
 * Resolves method arguments applying projections defined in @DtoProjectionSpec annotations.<br>
 * If a parameter is annotated (or meta-annotated) with @DtoProjectionSpec, this resolver applies the projection to the argument.
 */
@SuppressWarnings("ALL")
@Component
public class DtoProjectionArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    public DtoProjectionArgumentResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

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
    public Object resolveArgument(@Nonnull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        if (servletRequest == null) {
            throw new RuntimeException("Failed to resolve DTO projection. No HttpServletRequest found");
        }

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

        if (projectionAnnotation == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Class<? extends Dto<?>> projectedClass = (Class<? extends Dto<?>>) projectionAnnotation.projectedClass();

        var javaType = objectMapper.getTypeFactory().constructType(parameter.getGenericParameterType());

        // Parse the request body
        Object parameterValue = objectMapper.readValue(servletRequest.getReader(), javaType);

        try {

            if (parameterValue instanceof Dto<?> dto) {
                return applyProjection(dto, projectionAnnotation);
            }

            if (parameterValue instanceof ProjectableHolder projectableHolder) {

                var projectables = projectableHolder.getProjectables();
                var projectionResults = new ArrayList<Dto<?>>();

                for (var projectable : projectables) {

                    if (!(projectable instanceof Dto<?> dto)) {
                        throw new RuntimeException("Unsupported projectable type: " + projectable.getClass() + ". Only classes that extend Dto are supported");
                    }

                    projectionResults.add(applyProjection(dto, projectionAnnotation));

                }

                return projectableHolder;

            }

            throw new RuntimeException("Unsupported parameter type: " + parameterValue.getClass());

        } catch (RuntimeException e) {

            throw new RuntimeException("Failed applying projections to argument. Class: " + parameter.getMethod().getDeclaringClass() +
                    ", method: " + parameter.getMethod().getName() +
                    ", parameter:" + parameter.getParameterName()
                    , e);

        }
    }


    @NonNull
    public Dto<?> applyProjection(Dto<?> dto, @NonNull DtoProjectionSpec projectionAnnotation) {

        Class<? extends DtoProjection> projectionClass = findProjectionClass(dto);

        if (projectionClass == null) {
            throw new RuntimeException("Failed to find projection class for DTO of type: " + dto.getClass());
        }

        DtoProjection projection = ProjectionDsl.parse(projectionAnnotation.value(), projectionClass);

        try {
            projection.applyProjectionTo(dto);
        } catch (DtoProjectionException e) {
            throw e;
        }

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
