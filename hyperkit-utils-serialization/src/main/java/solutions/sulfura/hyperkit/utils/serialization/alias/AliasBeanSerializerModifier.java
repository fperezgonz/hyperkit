package solutions.sulfura.hyperkit.utils.serialization.alias;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import solutions.sulfura.hyperkit.dtos.Dto;

import java.util.List;

public class AliasBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        if (!Dto.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return beanProperties;
        }
        System.out.println(beanDesc.getBeanClass());

        beanProperties.replaceAll(AliasBeanPropertyWriter::new);
        System.out.println(beanProperties.getFirst().getClass());

        return beanProperties;
    }
}
