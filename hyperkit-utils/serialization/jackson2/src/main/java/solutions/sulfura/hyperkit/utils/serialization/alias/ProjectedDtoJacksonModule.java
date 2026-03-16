package solutions.sulfura.hyperkit.utils.serialization.alias;

import com.fasterxml.jackson.databind.module.SimpleModule;
import solutions.sulfura.hyperkit.utils.serialization.alias.deserialization.AliasBeanDeserializerModifier;
import solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanSerializerModifier;

public class ProjectedDtoJacksonModule extends SimpleModule {

    public ProjectedDtoJacksonModule() {
        super();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addBeanSerializerModifier(new AliasBeanSerializerModifier());
        context.addBeanDeserializerModifier(new AliasBeanDeserializerModifier());
    }

}
