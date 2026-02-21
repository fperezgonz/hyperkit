package solutions.sulfura.hyperkit.utils.serialization.value_wrapper.deserialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;

public class ValueWrapperDeserializer extends ReferenceTypeDeserializer<ValueWrapper<?>> {

    public ValueWrapperDeserializer(JavaType fullType, ValueInstantiator vi, TypeDeserializer typeDeser, JsonDeserializer<?> deser) {
        super(fullType, vi, typeDeser, deser);
    }

    @Override
    protected ReferenceTypeDeserializer<ValueWrapper<?>> withResolved(TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser) {
        return new ValueWrapperDeserializer(_fullType, _valueInstantiator, typeDeser, valueDeser);
    }

    @Override
    public ValueWrapper<?> getNullValue(DeserializationContext ctxt) {
        return ValueWrapper.of(null);
    }

    @Override
    public ValueWrapper<?> referenceValue(Object contents) {
        return ValueWrapper.of(contents);
    }

    @Override
    public ValueWrapper<?> updateReference(ValueWrapper<?> reference, Object contents) {
        return ValueWrapper.of(contents);
    }

    @Override
    public Object getReferenced(ValueWrapper<?> reference) {
        return reference.get();
    }

    @Override
    public Object getAbsentValue(DeserializationContext ctxt) {
        return ValueWrapper.empty();
    }

}
