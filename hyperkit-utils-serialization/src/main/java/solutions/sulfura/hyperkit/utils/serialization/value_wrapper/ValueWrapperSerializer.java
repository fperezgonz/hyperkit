package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import solutions.sulfura.hyperkit.dtos.ValueWrapperAdapter;

import java.io.IOException;

public class ValueWrapperSerializer extends JsonSerializer {

    private final ValueWrapperAdapter adapter;

    public ValueWrapperSerializer(ValueWrapperAdapter<?> adapter) {
        super();
        this.adapter = adapter;
    }

    @Override
    public void serialize(Object objects, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        if (objects != null && adapter.isEmpty(objects)) {
            return;
        }

        jsonGenerator.writeObject(adapter.unwrap(objects));

    }

    @Override
    public Class handledType() {
        return adapter.getWrapperClass();
    }


    @Override
    public boolean isEmpty(SerializerProvider provider, Object value) {
        return value != null && adapter.isEmpty(value);
    }

    @Override
    public boolean isEmpty(Object value) {
        return value != null && adapter.isEmpty(value);
    }
}
