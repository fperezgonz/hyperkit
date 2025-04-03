package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapter;

public class ValueWrapperBeanPropertyWriter extends BeanPropertyWriter {

    @SuppressWarnings("rawtypes")
    private final ValueWrapperAdapter adapter;

    protected ValueWrapperBeanPropertyWriter(BeanPropertyWriter base, ValueWrapperAdapter<?> adapter) {
        super(base);
        this.adapter = adapter;
    }

    protected ValueWrapperBeanPropertyWriter(ValueWrapperBeanPropertyWriter base, PropertyName newName, ValueWrapperAdapter<?> adapter) {
        super(base, newName);
        this.adapter = adapter;
    }

    @Override
    protected BeanPropertyWriter _new(PropertyName newName) {
        return new ValueWrapperBeanPropertyWriter(this, newName, adapter);
    }

    @Override
    public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper) {
        return new UnwrappingValueWrapperBeanPropertyWriter(this, unwrapper, adapter);
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator g, SerializerProvider prov) throws Exception {

        Object value = get(bean);

        //noinspection unchecked
        if (value == null || adapter.isEmpty(value)) {
            return;
        }

        super.serializeAsField(bean, g, prov);

    }

}
