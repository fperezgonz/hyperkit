package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ReferenceType;
import solutions.sulfura.hyperkit.dtos.ValueWrapperAdapter;

public class ValueWrapperDeserializers extends Deserializers.Base{

    private final ValueWrapperAdapter<?> adapter;

    public ValueWrapperDeserializers(ValueWrapperAdapter<?> adapter) {
        super();
        this.adapter = adapter;
    }

    @Override
    public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType,
                                                         DeserializationConfig config, BeanDescription beanDesc,
                                                         TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer) throws JsonMappingException {

        if(adapter.isSupportedWrapperType(refType.getRawClass())){
            return new ValueWrapperDeserializer(refType, null, contentTypeDeserializer,contentDeserializer, adapter);
        }

        return super.findReferenceDeserializer(refType, config, beanDesc, contentTypeDeserializer, contentDeserializer);
    }

}
