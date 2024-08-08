package solutions.sulfura.gend.dtos.projection;

import io.vavr.control.Option;
import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.ListOperation;
import solutions.sulfura.gend.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;

import java.util.Collection;

public class ProjectionUtils {

    public static <T> Option<T> getProjectedValue(Option<T> value, FieldConf fieldConf) {

        if (fieldConf instanceof DtoFieldConf<?>) {
            return getProjectedValue(value, (DtoFieldConf<?>) fieldConf);
        }

        if (shouldBeIgnored(fieldConf)) {
            return Option.none();
        }

        if (fieldConf.getPresence() == FieldConf.Presence.MANDATORY && (value == null || value.isEmpty())) {
            throw new DtoProjectionException("Mandatory field not present in dto");
        }

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

    public static <T> Option<T> getProjectedValue(Option<T> value, DtoFieldConf fieldConf) {

        if (shouldBeIgnored(fieldConf)) {
            return Option.none();
        }

        if (fieldConf.getPresence() == FieldConf.Presence.MANDATORY && (value == null || value.isEmpty())) {
            throw new DtoProjectionException("Mandatory field not present in dto");
        }

        Object nestedVal = value.getOrNull();

        DtoProjection dtoProjection = fieldConf.dtoProjection;

        if (nestedVal instanceof Collection<?>) {


            applyProjectionToCollection((Collection<?>) nestedVal, dtoProjection);

        } else if (nestedVal instanceof Dto) {

            dtoProjection.applyProjectionTo((Dto) nestedVal);

        } else {

            throw new DtoProjectionException("Unable to apply projection of type " + dtoProjection.getClass() + " to type " + nestedVal.getClass());

        }

        return value;

    }

    public static boolean shouldBeIgnored(FieldConf fieldConf) {
        return fieldConf == null || fieldConf.getPresence() == FieldConf.Presence.IGNORED;
    }

}
