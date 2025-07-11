package solutions.sulfura.hyperkit.dtos.projection;

import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;

import java.util.Collection;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ProjectionUtils {
    /**
     * Searches within a {@link Dto} class for a nested class that extends {@link DtoProjection}.
     * <p>
     * Expects the {@link Dto} class to contain an inner {@link DtoProjection} class similar to the one defined in the default template for generating Dtos
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Class<? extends DtoProjection> findDefaultProjectionClass(Class<?> dtoClass) {

        // Look for a nested class that extends DtoProjection
        for (Class<?> nestedClass : dtoClass.getDeclaredClasses()) {
            if (DtoProjection.class.isAssignableFrom(nestedClass)) {
                return (Class<? extends DtoProjection>) nestedClass;
            }
        }

        return null;
    }

    public static <T> ValueWrapper<T> getProjectedValue(ValueWrapper<T> value, FieldConf fieldConf) {

        if (fieldConf instanceof DtoFieldConf<?>) {
            return getProjectedValue(value, (DtoFieldConf<?>) fieldConf);
        }

        value = evaluatePresenceAndNullValues(value, fieldConf);

        return value;

    }

    public static void applyProjectionToCollection(Collection<?> collection, DtoProjection dtoProjection) {

        for (Object elem : collection) {

            if (elem instanceof ListOperation) {

                dtoProjection.applyProjectionTo(((ListOperation<Dto>) elem).getValue());

            } else if (elem instanceof Dto) {

                dtoProjection.applyProjectionTo((Dto) elem);

            } else {

                throw new DtoProjectionException("Unsupported list element type " + elem.getClass());

            }

        }

    }

    public static <T> ValueWrapper<T> getProjectedValue(ValueWrapper<T> value, DtoFieldConf<?> fieldConf) {

        value = evaluatePresenceAndNullValues(value, fieldConf);

        if (value.isEmpty()) {
            return ValueWrapper.empty();
        }

        Object nestedVal = value.getOrNull();

        DtoProjection dtoProjection = fieldConf.dtoProjection;

        if (nestedVal == null) {

            return ValueWrapper.of(null);

        } else if (nestedVal instanceof Collection<?>) {

            applyProjectionToCollection((Collection<?>) nestedVal, dtoProjection);

        } else if (nestedVal instanceof Dto) {

            dtoProjection.applyProjectionTo((Dto) nestedVal);

        } else {

            throw new DtoProjectionException("Unable to apply projection of type " + dtoProjection.getClass() + " to type " + nestedVal.getClass());

        }

        return value;

    }

    public static boolean fieldMustBePresent(FieldConf fieldConf) {
        return fieldConf.getPresence() == FieldConf.Presence.MANDATORY;
    }

    public static boolean fieldShouldBeIgnored(FieldConf fieldConf) {
        return fieldConf == null || fieldConf.getPresence() == FieldConf.Presence.IGNORED;
    }

    public static <T> ValueWrapper<T> evaluatePresenceAndNullValues(ValueWrapper<T> value, FieldConf fieldConf) {

        if (fieldShouldBeIgnored(fieldConf)) {
            return ValueWrapper.empty();
        }

        if (fieldMustBePresent(fieldConf) && (value == null || value.isEmpty())) {
            throw new DtoProjectionException("Mandatory field not present in dto");
        }

        if (value == null) {
            return ValueWrapper.empty();
        }

        return value;

    }

}
