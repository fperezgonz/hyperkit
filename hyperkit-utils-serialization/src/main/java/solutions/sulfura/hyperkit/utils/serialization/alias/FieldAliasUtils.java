package solutions.sulfura.hyperkit.utils.serialization.alias;

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

    public static FieldConf findFieldConfForPropertyByFieldAlias(DtoProjection<?> projection, String fieldName) {

        FieldConf fieldConf = null;

        // TODO Search by property instead of field
        // TODO Use a cache to improve performance

        // If the field name matches another property's field alias, use that property
        fieldConf = Arrays.stream(projection.getClass().getFields())
                // Only FieldConf fields
                .filter(field -> FieldConf.class.isAssignableFrom(field.getType()))
                .map(f -> {
                    try {
                        return ((FieldConf) f.get(projection));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                // Filter by alias matching property name
                .filter(dtoFieldConf -> dtoFieldConf != null && Objects.equals(dtoFieldConf.getFieldAlias(), fieldName))
                .findFirst()
                .orElse(null);

        if (fieldConf != null) {
            return fieldConf;
        }

        try {
            fieldConf = (FieldConf) projection.getClass().getField(fieldName).get(projection);
        } catch (Exception ignored) {
        }

        if (fieldConf != null && (fieldConf.getFieldAlias() == null || Objects.equals(fieldConf.getFieldAlias(), fieldName))) {
            return fieldConf;
        }

        return null;

    }
}
