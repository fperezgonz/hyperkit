package solutions.sulfura.hyperkit.utils.serialization.alias;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import solutions.sulfura.hyperkit.dtos.Dto;

import java.util.Iterator;

public class AliasBeanDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public BeanDeserializerBuilder updateBuilder(DeserializationConfig config, BeanDescription beanDesc, BeanDeserializerBuilder builder) {
        if (!Dto.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return super.updateBuilder(config, beanDesc, builder);
        }

        Iterator<SettableBeanProperty> it = builder.getProperties();
        while (it.hasNext()) {
            SettableBeanProperty property = it.next();
            builder.addOrReplaceProperty(new AliasSettableBeanProperty(property), true);
        }

        return builder;
    }

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
        if (deserializer instanceof BeanDeserializer beanDeserializer && Dto.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new AliasBeanDeserializer(beanDeserializer);
        }
        return deserializer;
    }
}
