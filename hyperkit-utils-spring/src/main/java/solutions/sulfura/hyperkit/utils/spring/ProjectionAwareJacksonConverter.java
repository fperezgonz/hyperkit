package solutions.sulfura.hyperkit.utils.spring;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import solutions.sulfura.hyperkit.dsl.projections.ProjectionCache;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;

import java.lang.annotation.Annotation;
import java.util.Optional;

import static solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

public class ProjectionAwareJacksonConverter
        extends MappingJackson2HttpMessageConverter {

    public final ProjectionCache dtoProjectionCache;

    public ProjectionAwareJacksonConverter(ObjectMapper objectMapper, ProjectionCache projectionCache) {
        super(objectMapper);
        this.dtoProjectionCache = projectionCache;
    }

    private DtoProjection<?> getProjectionForAnnotationInfo(ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> projectionAnnotationInfo) {
        if (projectionAnnotationInfo == null) {
            return null;
        }

        DtoProjectionSpec projectionSpec = projectionAnnotationInfo.targetAnnotation;

        @SuppressWarnings("OptionalAssignedToNull")
        Optional<DtoProjection<?>> projectionOptional = null;

        if (dtoProjectionCache != null) {
            projectionOptional = dtoProjectionCache.get(projectionSpec.projectedClass(), projectionSpec.namespace(), projectionSpec.value());
        }

        //noinspection OptionalAssignedToNull
        if (projectionOptional != null && projectionOptional.isEmpty()) {
            return null;
        }

        //noinspection OptionalAssignedToNull
        if (projectionOptional != null) {
            return projectionOptional.get();
        }

        DtoProjection<?> projection = ProjectionDsl.parse(projectionAnnotationInfo.targetAnnotation);

        if (dtoProjectionCache != null) {
            dtoProjectionCache.put(projectionSpec.projectedClass(), projectionSpec.namespace(), projectionSpec.value(), projection);
        }

        return projection;
    }

    private DtoProjection<?> getResponseProjectionForCurrentHandler() {

        HandlerMethod handler = getHandlerMethodForCurrentRequest();

        if (handler == null) {
            return null;
        }

        // TODO Use an annotation cache to improve performance?
        ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> projectionAnnotationInfo = getProjectionAnnotationInfoForReturnType(handler);

        return getProjectionForAnnotationInfo(projectionAnnotationInfo);

    }

    private DtoProjection<?> getRequestProjectionForCurrentHandler() {

        HandlerMethod handler = getHandlerMethodForCurrentRequest();

        if (handler == null) {
            return null;
        }

        // TODO Use an annotation cache to improve performance?
        ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> projectionAnnotationInfo = getProjectionAnnotationInfoForBodyParameters(handler);

        return getProjectionForAnnotationInfo(projectionAnnotationInfo);

    }

    /**
     * Adds the request body projection to the reader's attributes
     */
    @Override
    protected ObjectReader customizeReader(ObjectReader reader, JavaType javaType) {

        reader = super.customizeReader(reader, javaType);

        DtoProjection<?> projection = getRequestProjectionForCurrentHandler();

        if (projection == null) {
            return reader;
        }

        return reader.withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection);
    }

    /**
     * Adds the response body projection to the writer's attributes
     */
    @Override
    protected ObjectWriter customizeWriter(ObjectWriter writer, JavaType javaType, MediaType contentType) {

        writer = super.customizeWriter(writer, javaType, contentType);

        DtoProjection<?> projection = getResponseProjectionForCurrentHandler();

        if (projection == null) {
            return writer;
        }

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
