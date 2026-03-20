package solutions.sulfura.hyperkit.utils.serialization.jackson3.value_wrapper.serialization;

import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class ValueWrapperSerializer extends ValueSerializer<ValueWrapper<?>> {

    public ValueWrapperSerializer() {
        super();
    }

    @Override
    public void serialize(ValueWrapper<?> objects, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {

        if (objects == null || objects.isEmpty()) {
            jsonGenerator.writeNull();
            return;
        }

        jsonGenerator.writePOJO(objects.get());

    }

    @Override
    public Class handledType() {
        return ValueWrapper.class;
    }


    @Override
    public boolean isEmpty(SerializationContext provider, ValueWrapper<?> value) {
        return value == null || value.isEmpty();
    }

}
