package solutions.sulfura.hyperkit.utils.serialization;

import org.jspecify.annotations.Nullable;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;

public interface SerializationProvider {

    String write(@Nullable Object object, @Nullable DtoProjection<?> projection);

}
