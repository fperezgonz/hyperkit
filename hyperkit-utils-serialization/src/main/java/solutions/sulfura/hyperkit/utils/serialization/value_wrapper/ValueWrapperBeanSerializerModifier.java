package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import solutions.sulfura.hyperkit.dtos.ValueWrapperAdapter;

import java.util.List;

public class ValueWrapperBeanSerializerModifier extends BeanSerializerModifier {

    private final ValueWrapperAdapter<?> adapter;

    public ValueWrapperBeanSerializerModifier(ValueWrapperAdapter<?> adapter) {
        this.adapter = adapter;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {

        for (int i = 0; i < beanProperties.size(); ++i) {

            final BeanPropertyWriter writer = beanProperties.get(i);
            JavaType type = writer.getType();
            if (type.isTypeOrSubTypeOf(adapter.getWrapperClass())) {
                beanProperties.set(i, new ValueWrapperBeanPropertyWriter(writer, adapter));
            }

        }

        return beanProperties;

    }
}
