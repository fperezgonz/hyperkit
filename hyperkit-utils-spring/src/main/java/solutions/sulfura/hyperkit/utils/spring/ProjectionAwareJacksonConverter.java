package solutions.sulfura.hyperkit.utils.spring;

import com.fasterxml.jackson.databind.*;
import org.jspecify.annotations.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;

import java.lang.annotation.Annotation;

import static solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

public class ProjectionAwareJacksonConverter
        extends MappingJackson2HttpMessageConverter {

    public ProjectionAwareJacksonConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    /**
     * Adds the request body projection to the reader's attributes
     */
    @Override
    protected ObjectReader customizeReader(ObjectReader reader, JavaType javaType) {

        reader = super.customizeReader(reader, javaType);

        HandlerMethod handler = getHandlerMethodForCurrentRequest();

        if (handler == null) {
            return reader;
        }

        ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> projectionAnnotationInfo = getProjectionAnnotationInfoForBodyParameters(handler);

        if (projectionAnnotationInfo == null) {
            return reader;
        }

        Class<? extends DtoProjection> projectionClass = solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils.findDefaultProjectionClass(projectionAnnotationInfo.targetAnnotation.projectedClass());
        DtoProjection<?> projection = ProjectionDsl.parse(projectionAnnotationInfo.targetAnnotation, projectionClass);
        return reader.withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection);
    }

    /**
     * Adds the response body projection to the writer's attributes
     */
    @Override
    protected ObjectWriter customizeWriter(ObjectWriter writer, JavaType javaType, MediaType contentType) {

        writer = super.customizeWriter(writer, javaType, contentType);

        HandlerMethod handler = getHandlerMethodForCurrentRequest();

        if (handler == null) {
            return writer;
        }

        ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> projectionAnnotationInfo = getProjectionAnnotationInfoForReturnType(handler);

        if (projectionAnnotationInfo == null) {
            return writer;
        }

        Class<? extends DtoProjection> projectionClass = solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils.findDefaultProjectionClass(projectionAnnotationInfo.targetAnnotation.projectedClass());

        DtoProjection<?> projection = ProjectionDsl.parse(projectionAnnotationInfo.targetAnnotation, projectionClass);
        return writer.withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection);


    }

    @Nullable
    protected HandlerMethod getHandlerMethodForCurrentRequest() {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attr == null) {
            return null;
        }

        HandlerMethod handler = (HandlerMethod) attr.getRequest()
                .getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

        if (handler == null) {
            return null;
        }

        return handler;

    }

    @Nullable
    protected ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> getProjectionAnnotationInfoForReturnType(@NonNull HandlerMethod handler) {
        return ProjectionUtils.getReturnTypeAnnotationInfo(handler.getMethod(), DtoProjectionSpec.class);
    }

    @Nullable
    protected ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> getProjectionAnnotationInfoForBodyParameters(@NonNull HandlerMethod handler) {

        for (MethodParameter methodParameter : handler.getMethodParameters()) {
            if (!methodParameter.hasParameterAnnotation(RequestBody.class)) {
                continue;
            }
            return ProjectionUtils.getAnnotationInfo(methodParameter.getParameter(), DtoProjectionSpec.class);

        }

        return null;

    }
}
