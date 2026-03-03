package solutions.sulfura.hyperkit.dsl.projections;

import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectionCache {

    protected ConcurrentHashMap<CacheKey, Optional<DtoProjection<?>>> dtoProjectionCache = new ConcurrentHashMap<>();

    public ProjectionCache() {
    }

    public Optional<DtoProjection<?>> get(CacheKey cacheKey) {
        return dtoProjectionCache.get(cacheKey);
    }

    public Optional<DtoProjection<?>> get(Class<?> dtoClass, String namespace, String projectionString) {
        return this.get(new CacheKey(dtoClass, namespace, projectionString));
    }

    public Optional<DtoProjection<?>> get(DtoProjectionSpec projectionSpec) {
        return this.get(projectionSpec.projectedClass(), projectionSpec.namespace(), projectionSpec.value());
    }

    public Optional<DtoProjection<?>> put(Class<?> dtoClass,
                                          String namespace,
                                          String projectionString,
                                          DtoProjection<?> dtoProjection) {
        return dtoProjectionCache.put(
                new CacheKey(dtoClass, namespace, projectionString),
                Optional.ofNullable(dtoProjection)
        );
    }

    public Optional<DtoProjection<?>> put(CacheKey cacheKey, DtoProjection<?> dtoProjection) {
        return dtoProjectionCache.put(cacheKey, Optional.ofNullable(dtoProjection));
    }

    public Optional<DtoProjection<?>> put(DtoProjectionSpec projectionSpec, DtoProjection<?> dtoProjection) {
        return this.put(projectionSpec.projectedClass(),
                projectionSpec.namespace(),
                projectionSpec.value(),
                dtoProjection
        );
    }

    public record CacheKey(Class<?> dtoClass, String namespace, String projectionString) {
    }

}
