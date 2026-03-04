package solutions.sulfura.hyperkit.dsl.projections;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils.AnnotationInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A cache for projection annotations and metadata
 */
public class ProjectionAnnotationCache {

    private final Map<Method, Optional<AnnotationInfo<Annotation, DtoProjectionSpec>>> returnTypeAnnotationInfoCache = new ConcurrentHashMap<>();
    private final Map<Parameter, Optional<AnnotationInfo<Annotation, DtoProjectionSpec>>> parameterAnnotationInfoCache = new ConcurrentHashMap<>();

    @Nullable
    public AnnotationInfo<Annotation, DtoProjectionSpec> getReturnTypeAnnotationInfo(@NonNull Method method) {
        return returnTypeAnnotationInfoCache.computeIfAbsent(method,
                        m -> Optional.ofNullable(ProjectionUtils.getReturnTypeAnnotationInfo(m, DtoProjectionSpec.class)))
                .orElse(null);
    }

    @Nullable
    public AnnotationInfo<Annotation, DtoProjectionSpec> getParameterAnnotationInfo(@NonNull Parameter parameter) {
        return parameterAnnotationInfoCache.computeIfAbsent(parameter, param -> Optional.ofNullable(ProjectionUtils.getAnnotationInfo(param, DtoProjectionSpec.class)))
                .orElse(null);
    }

    @Nullable
    public DtoProjectionSpec getReturnTypeAnnotation(@NonNull Method method) {
        return returnTypeAnnotationInfoCache.computeIfAbsent(method,
                        m -> Optional.ofNullable(ProjectionUtils.getReturnTypeAnnotationInfo(m, DtoProjectionSpec.class)))
                .map(annInfo -> annInfo.targetAnnotation)
                .orElse(null);
    }

    @Nullable
    public DtoProjectionSpec getParameterAnnotation(@NonNull Parameter parameter) {
        return parameterAnnotationInfoCache.computeIfAbsent(parameter,
                        p -> Optional.ofNullable(ProjectionUtils.getAnnotationInfo(p, DtoProjectionSpec.class)))
                .map(annInfo -> annInfo.targetAnnotation)
                .orElse(null);
    }
}
