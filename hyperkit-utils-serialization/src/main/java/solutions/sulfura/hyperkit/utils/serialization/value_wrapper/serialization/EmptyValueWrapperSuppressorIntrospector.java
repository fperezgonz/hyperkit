package solutions.sulfura.hyperkit.utils.serialization.value_wrapper.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;

/**
 * It doesn't introspect annotations, this is a hack to suppress properties of type ValueWrapper to avoid using BeanSerializerModifier that would conflict with other BeanPropertyWriters
 */
public class EmptyValueWrapperSuppressorIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public JsonInclude.Value findPropertyInclusion(Annotated a) {
        JsonInclude.Value defaultInclusion = super.findPropertyInclusion(a);

        if (a.getRawType() == ValueWrapper.class) {
            return JsonInclude.Value.construct(JsonInclude.Include.NON_EMPTY, null);
        }

        return defaultInclusion;
    }

}