package solutions.sulfura.hyperkit.dsl.projections;

import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;

import java.util.concurrent.ConcurrentHashMap;

public class CachedProjectionParser {

    protected ConcurrentHashMap<CacheKey, DtoProjection<Dto<?>>> dtoProjectionCache = new ConcurrentHashMap<>();

    public CachedProjectionParser() {
    }

    public DtoProjection<?> get(CacheKey cacheKey) {
        return dtoProjectionCache.get(cacheKey);
    }

    public DtoProjection<Dto<?>> get(Class<?> dtoClass, String namespace, String projectionString) {
        CacheKey cacheKey = new CacheKey(dtoClass, namespace, projectionString);
        return dtoProjectionCache.computeIfAbsent(cacheKey, key -> {
            Class<? extends DtoProjection<Dto<?>>> projectionClass = ProjectionUtils.findDefaultProjectionClass(dtoClass);
            return ProjectionDsl.parse(projectionString, projectionClass);
        });
    }

    public DtoProjection<?> get(DtoProjectionSpec projectionSpec) {
        return this.get(projectionSpec.projectedClass(), projectionSpec.namespace(), projectionSpec.value());
    }

    public record CacheKey(Class<?> dtoClass, String namespace, String projectionString) {
    }

}
