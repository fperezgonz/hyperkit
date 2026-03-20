package solutions.sulfura.hyperkit.utils.serialization.jackson2.value_wrapper.deserialization;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ReferenceType;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;

public class ValueWrapperDeserializers extends Deserializers.Base{

    public ValueWrapperDeserializers() {
        super();
    }

    @Override
    public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType,
                                                         DeserializationConfig config, BeanDescription beanDesc,
                                                         TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer) throws JsonMappingException {

        if(ValueWrapper.class.isAssignableFrom(refType.getRawClass())){
            return new ValueWrapperDeserializer(refType, null, contentTypeDeserializer,contentDeserializer);
        }

        return super.findReferenceDeserializer(refType, config, beanDesc, contentTypeDeserializer, contentDeserializer);
    }

}
