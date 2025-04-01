package solutions.sulfura.hyperkit.dtos.projection;

import io.vavr.control.Option;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.ListOperation;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;

import java.util.Collection;

public class ProjectionUtils {

    public static <T> Option<T> getProjectedValue(Option<T> value, FieldConf fieldConf) {

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

    public static <T> Option<T> getProjectedValue(Option<T> value, DtoFieldConf<?> fieldConf) {

        value = evaluatePresenceAndNullValues(value, fieldConf);

        if (value.isEmpty()) {
            return Option.none();
        }

        Object nestedVal = value.getOrNull();

        DtoProjection dtoProjection = fieldConf.dtoProjection;

        if (nestedVal == null){

            return Option.some(null);

        } else if (nestedVal instanceof Collection<?>) {

            applyProjectionToCollection((Collection<?>) nestedVal, dtoProjection);

        } else if (nestedVal instanceof Dto) {

            dtoProjection.applyProjectionTo((Dto) nestedVal);

        } else{

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

    public static <T> Option<T> evaluatePresenceAndNullValues(Option<T> value, FieldConf fieldConf) {

        if (fieldShouldBeIgnored(fieldConf)) {
            return Option.none();
        }

        if (fieldMustBePresent(fieldConf) && (value == null || value.isEmpty())) {
            throw new DtoProjectionException("Mandatory field not present in dto");
        }

        if (value == null) {
            return Option.none();
        }

        return value;

    }

}
