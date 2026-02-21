package solutions.sulfura.hyperkit.utils.serialization.alias;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class ProjectedDtoJacksonModule extends SimpleModule {

    public ProjectedDtoJacksonModule() {
        super("ProjectedDtoJacksonModule");
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addBeanSerializerModifier(new AliasBeanSerializerModifier());
        context.addBeanDeserializerModifier(new AliasBeanDeserializerModifier());
    }

}
