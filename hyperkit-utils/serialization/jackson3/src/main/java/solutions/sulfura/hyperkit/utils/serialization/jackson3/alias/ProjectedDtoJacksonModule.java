package solutions.sulfura.hyperkit.utils.serialization.jackson3.alias;

import solutions.sulfura.hyperkit.utils.serialization.jackson3.alias.deserialization.AliasBeanDeserializerModifier;
import solutions.sulfura.hyperkit.utils.serialization.jackson3.alias.serialization.AliasBeanSerializerModifier;
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
