package solutions.sulfura.hyperkit.utils.serialization.jackson3.value_wrapper.deserialization;

import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.Deserializers;
import tools.jackson.databind.jsontype.TypeDeserializer;
import tools.jackson.databind.type.ReferenceType;

public class ValueWrapperDeserializers extends Deserializers.Base {

    public ValueWrapperDeserializers() {
        super();
    }

    @Override
    public boolean hasDeserializerFor(DeserializationConfig config, Class<?> valueType) {
        return ValueWrapper.class.isAssignableFrom(valueType);
    }

    @Override
    public ValueDeserializer<?> findReferenceDeserializer(ReferenceType refType,
                                                          DeserializationConfig config, BeanDescription.Supplier beanDesc,
                                                          TypeDeserializer contentTypeDeserializer, ValueDeserializer<?> contentDeserializer) {

        if (ValueWrapper.class.isAssignableFrom(refType.getRawClass())) {
            return new ValueWrapperDeserializer(refType, null, contentTypeDeserializer, contentDeserializer);
        }

        return super.findReferenceDeserializer(refType, config, beanDesc, contentTypeDeserializer, contentDeserializer);
    }

}
