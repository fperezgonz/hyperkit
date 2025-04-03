package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapter;

public class ValueWrapperJacksonModule extends SimpleModule {

    private final ValueWrapperAdapter<?> adapter;

    public ValueWrapperJacksonModule(ValueWrapperAdapter<?> adapter) {
        super();
        this.adapter = adapter;
    }

    @Override
    public String getModuleName() {
        return "ValueWrapperJacksonModule";
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {

        context.addBeanSerializerModifier(new ValueWrapperBeanSerializerModifier(adapter));

        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(new ValueWrapperSerializer(adapter));
        context.addSerializers(serializers);

        context.addDeserializers(new ValueWrapperDeserializers(adapter));
        context.addTypeModifier(new ValueWrapperTypeModifier(adapter));

    }

}
