package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;

import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.deserialization.ValueWrapperDeserializers;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.deserialization.ValueWrapperTypeModifier;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.serialization.EmptyValueWrapperSuppressorIntrospector;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.serialization.ValueWrapperSerializer;
import tools.jackson.core.Version;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.module.SimpleSerializers;

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
