package solutions.sulfura.hyperkit.utils.serialization.jackson3.value_wrapper.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.databind.introspect.Annotated;
import tools.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * It doesn't introspect annotations, this is a hack to suppress properties of type ValueWrapper to avoid using BeanSerializerModifier that would conflict with other BeanPropertyWriters
 */
public class EmptyValueWrapperSuppressorIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public JsonInclude.Value findPropertyInclusion(MapperConfig<?> config, Annotated a) {
        JsonInclude.Value defaultInclusion = super.findPropertyInclusion(config, a);

        if (a.getRawType() == ValueWrapper.class) {
            return JsonInclude.Value.construct(JsonInclude.Include.NON_EMPTY, null);
        }

        return defaultInclusion;
    }

}