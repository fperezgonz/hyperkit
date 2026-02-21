package solutions.sulfura.hyperkit.utils.serialization.value_wrapper.serialization;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;

import java.io.IOException;

@SuppressWarnings("unchecked")
public class ValueWrapperSerializer extends JsonSerializer<ValueWrapper<?>> {

    public ValueWrapperSerializer() {
        super();
    }

    @Override
    public void serialize(ValueWrapper<?> objects, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        if (objects == null || objects.isEmpty()) {
            jsonGenerator.writeNull();
            return;
        }

        jsonGenerator.writeObject(objects.get());

    }

    @Override
    public Class handledType() {
        return ValueWrapper.class;
    }


    @Override
    public boolean isEmpty(SerializerProvider provider, ValueWrapper<?> value) {
        return value == null || value.isEmpty();
    }

    @Deprecated
    @Override
    public boolean isEmpty(ValueWrapper<?> value) {
        return value == null || value.isEmpty();
    }
}
