package solutions.sulfura.hyperkit.utils.serialization.value_wrapper.deserialization;

import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.ValueInstantiator;
import tools.jackson.databind.deser.std.ReferenceTypeDeserializer;
import tools.jackson.databind.jsontype.TypeDeserializer;

public class ValueWrapperDeserializer extends ReferenceTypeDeserializer<ValueWrapper<?>> {

    public ValueWrapperDeserializer(JavaType fullType, ValueInstantiator vi, TypeDeserializer typeDeser, ValueDeserializer<?> deser) {
        super(fullType, vi, typeDeser, deser);
    }

    @Override
    protected ReferenceTypeDeserializer<ValueWrapper<?>> withResolved(TypeDeserializer typeDeser, ValueDeserializer<?> valueDeser) {
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
