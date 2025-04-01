package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import solutions.sulfura.hyperkit.dtos.ValueWrapperAdapter;

public class ValueWrapperDeserializer extends ReferenceTypeDeserializer<Object> {

    private final ValueWrapperAdapter adapter;

    public ValueWrapperDeserializer(JavaType fullType, ValueInstantiator vi, TypeDeserializer typeDeser, JsonDeserializer<?> deser, ValueWrapperAdapter adapter) {
        super(fullType, vi, typeDeser, deser);
        this.adapter = adapter;
    }

    @Override
    protected ReferenceTypeDeserializer<Object> withResolved(TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser) {
        return new ValueWrapperDeserializer(_fullType, _valueInstantiator, typeDeser, valueDeser, adapter);
    }

    @Override
    public Object getNullValue(DeserializationContext ctxt) {
        return adapter.wrap(null);
    }

    @Override
    public Object referenceValue(Object contents) {
        return adapter.wrap(contents);
    }

    @Override
    public Object updateReference(Object reference, Object contents) {
        return adapter.wrap(contents);
    }

    @Override
    public Object getReferenced(Object reference) {
        return adapter.unwrap(reference);
    }

    @Override
    public Object getAbsentValue(DeserializationContext ctxt) {
        return adapter.empty();
    }

}
