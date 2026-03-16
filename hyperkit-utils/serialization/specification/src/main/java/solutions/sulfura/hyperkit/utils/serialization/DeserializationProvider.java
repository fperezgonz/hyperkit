package solutions.sulfura.hyperkit.utils.serialization;

import org.jspecify.annotations.Nullable;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;

public interface DeserializationProvider {

    <T> T read(@Nullable String jsonString, @Nullable Class<T> clazz, @Nullable DtoProjection<?> projection);

}
