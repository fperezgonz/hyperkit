package solutions.sulfura.hyperkit.utils.serialization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import solutions.sulfura.hyperkit.dtos.Dto;
import tools.jackson.databind.module.SimpleModule;

public class DtoJacksonModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.setMixIn(Dto.class, DtoMixin.class);
    }

    public static abstract class DtoMixin {
        @JsonIgnore
        public abstract Class<?> getSourceClass();
    }

}
