package solutions.sulfura.hyperkit.utils.spring.jackson3;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import solutions.sulfura.hyperkit.dsl.projections.*;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils.AnnotationInfo;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectReader;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.json.JsonMapper;

import java.lang.annotation.Annotation;

import static solutions.sulfura.hyperkit.utils.serialization.jackson3.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

public class ProjectionAwareJacksonJsonConverter
        extends JacksonJsonHttpMessageConverter {

    public final CachedProjectionParser dtoCachedProjectionParser;
    public final ProjectionAnnotationCache projectionAnnotationCache;

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        // This converter is only meant for DTO serialization with projections.
        // Defer String and byte[] to their dedicated converters.
        if (clazz == String.class || clazz == byte[].class) {
            return false;
        }
        return super.canWrite(clazz, mediaType);
    }

    @Override
    public boolean canWrite(ResolvableType targetType, Class<?> valueClass, @Nullable MediaType mediaType) {
        Class<?> clazz = targetType.toClass();
        if (clazz == byte[].class || clazz == String.class) {
            return false;
        }
        return super.canWrite(targetType, valueClass, mediaType);
    }

    public ProjectionAwareJacksonJsonConverter(JsonMapper objectMapper,
                                               CachedProjectionParser cachedProjectionParser,
                                               ProjectionAnnotationCache projectionAnnotationCache) {
        super(objectMapper);
        this.dtoCachedProjectionParser = cachedProjectionParser;
        this.projectionAnnotationCache = projectionAnnotationCache;
    }

    private DtoProjection<?> getProjectionForAnnotationInfo(AnnotationInfo<Annotation, DtoProjectionSpec> projectionAnnotationInfo) {
        if (projectionAnnotationInfo == null) {
            return null;
        }

        DtoProjectionSpec projectionSpec = projectionAnnotationInfo.targetAnnotation;

        if (dtoCachedProjectionParser != null) {
            return dtoCachedProjectionParser.get(projectionSpec.projectedClass(), projectionSpec.namespace(), projectionSpec.value());
        }

        return ProjectionDsl.parse(projectionAnnotationInfo.targetAnnotation);

    }

    private DtoProjection<?> getResponseProjectionForCurrentHandler() {

        HandlerMethod handler = getHandlerMethodForCurrentRequest();

        if (handler == null) {
            return null;
        }

        AnnotationInfo<Annotation, DtoProjectionSpec> projectionAnnotationInfo = getProjectionAnnotationInfoForReturnType(handler);

        return getProjectionForAnnotationInfo(projectionAnnotationInfo);

    }

    private DtoProjection<?> getRequestProjectionForCurrentHandler() {

        HandlerMethod handler = getHandlerMethodForCurrentRequest();

        if (handler == null) {
            return null;
        }

        AnnotationInfo<Annotation, DtoProjectionSpec> projectionAnnotationInfo = getProjectionAnnotationInfoForBodyParameters(handler);

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
    protected ObjectWriter customizeWriter(ObjectWriter writer, @Nullable JavaType javaType, @Nullable MediaType contentType) {

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

        return (HandlerMethod) attr.getRequest()
                .getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

    }

    @Nullable
    protected AnnotationInfo<Annotation, DtoProjectionSpec> getProjectionAnnotationInfoForReturnType(@NonNull HandlerMethod handler) {
        return projectionAnnotationCache != null
                ? projectionAnnotationCache.getReturnTypeAnnotationInfo(handler.getMethod())
                : ProjectionUtils.getReturnTypeAnnotationInfo(handler.getMethod(), DtoProjectionSpec.class);
    }

    @Nullable
    protected AnnotationInfo<Annotation, DtoProjectionSpec> getProjectionAnnotationInfoForBodyParameters(@NonNull HandlerMethod handler) {

        for (MethodParameter methodParameter : handler.getMethodParameters()) {

            if (!methodParameter.hasParameterAnnotation(RequestBody.class)) {
                continue;
            }

            if (projectionAnnotationCache != null) {
                return projectionAnnotationCache.getParameterAnnotationInfo(methodParameter.getParameter());
            }

            return ProjectionUtils.getAnnotationInfo(methodParameter.getParameter(), DtoProjectionSpec.class);

        }

        return null;

    }
}
