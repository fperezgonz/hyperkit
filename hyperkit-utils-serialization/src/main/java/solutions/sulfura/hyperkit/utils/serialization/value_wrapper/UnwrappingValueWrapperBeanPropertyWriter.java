package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapter;

public class UnwrappingValueWrapperBeanPropertyWriter extends UnwrappingBeanPropertyWriter {

    @SuppressWarnings("rawtypes")
    private final ValueWrapperAdapter adapter;

    public UnwrappingValueWrapperBeanPropertyWriter(BeanPropertyWriter base,
                                                    NameTransformer transformer, ValueWrapperAdapter<?> adapter) {
        super(base, transformer);
        this.adapter = adapter;
    }

    protected UnwrappingValueWrapperBeanPropertyWriter(UnwrappingValueWrapperBeanPropertyWriter base,
                                                       NameTransformer transformer, SerializedString name, ValueWrapperAdapter<?> adapter) {
        super(base, transformer, name);
        this.adapter = adapter;
    }

    @Override
    protected UnwrappingBeanPropertyWriter _new(NameTransformer transformer, SerializedString newName) {
        return new UnwrappingValueWrapperBeanPropertyWriter(this, transformer, newName, adapter);
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {

        Object value = get(bean);

        //noinspection unchecked
        if (value == null || adapter.isEmpty(value)) {
            return;
        }

        super.serializeAsField(bean, gen, prov);

    }
}
