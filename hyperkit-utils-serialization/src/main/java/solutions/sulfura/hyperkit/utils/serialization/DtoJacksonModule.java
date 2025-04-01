package solutions.sulfura.hyperkit.utils.serialization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.module.SimpleModule;
import solutions.sulfura.hyperkit.dtos.Dto;

public class DtoJacksonModule extends SimpleModule {

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(Dto.class, DtoMixin.class);
    }

    public static abstract class DtoMixin {
        @JsonIgnore
        public abstract Class<?> getSourceClass();
    }

}
