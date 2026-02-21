package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapter;

public class ValueWrapperJacksonModule extends SimpleModule {

    public ValueWrapperJacksonModule() {
        super();
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

        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(new ValueWrapperSerializer());
        context.addSerializers(serializers);
        context.appendAnnotationIntrospector(new EmptyValueWrapperSuppressorIntrospector());

        context.addDeserializers(new ValueWrapperDeserializers());
        context.addTypeModifier(new ValueWrapperTypeModifier());

    }

}
