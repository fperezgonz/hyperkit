package solutions.sulfura.hyperkit.dsl.projections;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FieldAliasUtils {

    public static ConcurrentHashMap<CacheKey, Optional<FieldConf>> fieldConfByPropertyNameAndProjection = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<CacheKey, Optional<FieldConfData>> fieldConfByPropertyAliasAndProjection = new ConcurrentHashMap<>();

    boolean cacheEnabled;

    public FieldAliasUtils(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public FieldConf findFieldConfForProperty(DtoProjection<?> projection, String fieldName) {
        return findFieldConfForProperty(projection, fieldName, cacheEnabled);
    }

    public FieldConfData findFieldConfForPropertyByFieldAlias(@NonNull DtoProjection<?> projection, @NonNull String fieldName) {
        return findFieldConfForPropertyByFieldAlias(projection, fieldName, cacheEnabled);
    }

    /**
     * @return null if the property was not found
     */
    public static FieldConf findFieldConfForProperty(DtoProjection<?> projection, String fieldName, boolean useCache) {

        if (useCache) {
            var fieldConfOptional = fieldConfByPropertyNameAndProjection.get(new CacheKey(fieldName, projection));
            if (fieldConfOptional != null && fieldConfOptional.isPresent()) {
                return fieldConfOptional.get();
            }
        }

        try {

            var fieldConf = (FieldConf) projection.getClass().getField(fieldName).get(projection);

            if (useCache) {
                fieldConfByPropertyNameAndProjection.put(new CacheKey(fieldName, projection), Optional.ofNullable(fieldConf));
            }

            return fieldConf;

        } catch (Exception ignored) {

            if (useCache) {
                fieldConfByPropertyNameAndProjection.put(new CacheKey(fieldName, projection), Optional.empty());
            }
            return null;
        }

    }

    @Nullable
    public static FieldConfData findFieldConfForPropertyByFieldAlias(@NonNull DtoProjection<?> projection, @NonNull String fieldName, boolean useCache) {

        if (useCache) {
            var fieldConfDataOptional = fieldConfByPropertyAliasAndProjection.get(new CacheKey(fieldName, projection));
            if (fieldConfDataOptional != null && fieldConfDataOptional.isPresent()) {
                return fieldConfDataOptional.get();
            }
        }

        FieldConfData fieldConfData = null;

        // If the field name matches another property's field alias, use that property
        fieldConfData = Arrays.stream(projection.getClass().getFields())
                // Only FieldConf fields
                .filter(field -> FieldConf.class.isAssignableFrom(field.getType()))
                .map(f -> {
                    try {
                        return new FieldConfData(f.getName(), (FieldConf) f.get(projection));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                // Filter by alias matching property name
                .filter(fcData -> fcData.fieldConf != null && Objects.equals(fcData.fieldConf.getFieldAlias(), fieldName))
                .findFirst()
                .orElse(null);

        if (fieldConfData != null) {
            if (useCache) {
                fieldConfByPropertyAliasAndProjection.put(new CacheKey(fieldName, projection), Optional.of(fieldConfData));
            }
            return fieldConfData;
        }

        // If there are no matches by alias, try matching by property name

        try {
            fieldConfData = new FieldConfData(fieldName, (FieldConf) projection.getClass().getField(fieldName).get(projection));
        } catch (Exception ignored) {
        }

        if (fieldConfData == null || fieldConfData.fieldConf == null) {
            if (useCache) {
                fieldConfByPropertyAliasAndProjection.put(new CacheKey(fieldName, projection), Optional.empty());
            }
            return null;
        }

        if (fieldConfData.fieldConf.getFieldAlias() == null || Objects.equals(fieldConfData.fieldConf.getFieldAlias(), fieldName)) {
            if (useCache) {
                fieldConfByPropertyAliasAndProjection.put(new CacheKey(fieldName, projection), Optional.of(fieldConfData));
            }
            return fieldConfData;
        }

        if (useCache) {
            fieldConfByPropertyAliasAndProjection.put(new CacheKey(fieldName, projection), Optional.empty());
        }
        return null;

    }

    public record FieldConfData(@NonNull String fieldName, @Nullable FieldConf fieldConf) {
    }

    public record CacheKey(@NonNull String name, @NonNull DtoProjection<?> projection) {
    }

}
