package solutions.sulfura.hyperkit.utils.serialization.alias.deserialization;

import solutions.sulfura.hyperkit.dtos.Dto;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.BeanDeserializerBuilder;
import tools.jackson.databind.deser.SettableBeanProperty;
import tools.jackson.databind.deser.ValueDeserializerModifier;
import tools.jackson.databind.deser.bean.BeanDeserializer;

import java.util.Iterator;

public class AliasBeanDeserializerModifier extends ValueDeserializerModifier {

    @Override
    public BeanDeserializerBuilder updateBuilder(DeserializationConfig config, BeanDescription.Supplier beanDesc, BeanDeserializerBuilder builder) {
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
    public ValueDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription.Supplier beanDesc, ValueDeserializer<?> deserializer) {
        if (deserializer instanceof BeanDeserializer beanDeserializer && Dto.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new AliasBeanDeserializer(beanDeserializer);
        }
        return deserializer;
    }
}
