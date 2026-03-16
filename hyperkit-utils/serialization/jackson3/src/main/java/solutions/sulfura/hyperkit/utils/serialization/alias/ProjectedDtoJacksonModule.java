package solutions.sulfura.hyperkit.utils.serialization.alias;

import solutions.sulfura.hyperkit.utils.serialization.alias.deserialization.AliasBeanDeserializerModifier;
import solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanSerializerModifier;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.module.SimpleModule;

public class ProjectedDtoJacksonModule extends SimpleModule {

    public ProjectedDtoJacksonModule() {
        super();
    }

    @Override
    public void setupModule(JacksonModule.SetupContext context) {
        context.addSerializerModifier(new AliasBeanSerializerModifier());
        context.addDeserializerModifier(new AliasBeanDeserializerModifier());
    }

}
