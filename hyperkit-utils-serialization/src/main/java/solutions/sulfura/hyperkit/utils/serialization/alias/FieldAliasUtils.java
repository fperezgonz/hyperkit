package solutions.sulfura.hyperkit.utils.serialization.alias;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;

import java.util.Arrays;
import java.util.Objects;

public class FieldAliasUtils {

    /**
     * @return null if the property was not found
     */
    public static FieldConf findFieldConfForProperty(DtoProjection<?> projection, String fieldName) {

        // TODO Search by property instead of field
        // TODO Use a cache to improve performance
        try {

            var fieldConf = (FieldConf) projection.getClass().getField(fieldName).get(projection);

            return fieldConf;

        } catch (Exception ignored) {
            return null;
        }

    }

    @Nullable
    public static FieldConfData findFieldConfForPropertyByFieldAlias(@NonNull DtoProjection<?> projection, @NonNull String fieldName) {

        FieldConfData fieldConfData = null;

        // TODO Search by property instead of field
        // TODO Use a cache to improve performance

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
            return fieldConfData;
        }

        // If there are no matches by alias, try matching by property name

        try {
            fieldConfData = new FieldConfData(fieldName, (FieldConf) projection.getClass().getField(fieldName).get(projection));
        } catch (Exception ignored) {
        }

        if (fieldConfData == null || fieldConfData.fieldConf == null) {
            return null;
        }

        if (fieldConfData.fieldConf.getFieldAlias() == null || Objects.equals(fieldConfData.fieldConf.getFieldAlias(), fieldName)) {
            return fieldConfData;
        }

        return null;

    }

    public record FieldConfData(@NonNull String fieldName, @Nullable FieldConf fieldConf) {
    }

}
