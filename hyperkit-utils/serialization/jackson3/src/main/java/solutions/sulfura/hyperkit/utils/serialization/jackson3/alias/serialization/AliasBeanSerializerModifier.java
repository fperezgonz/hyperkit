package solutions.sulfura.hyperkit.utils.serialization.jackson3.alias.serialization;

import solutions.sulfura.hyperkit.dtos.Dto;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.ser.BeanPropertyWriter;
import tools.jackson.databind.ser.ValueSerializerModifier;

import java.util.List;

public class AliasBeanSerializerModifier extends ValueSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription.Supplier beanDesc, List<BeanPropertyWriter> beanProperties) {
        if (!Dto.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return beanProperties;
        }

        beanProperties.replaceAll(AliasBeanPropertyWriter::new);

        return beanProperties;
    }
}
