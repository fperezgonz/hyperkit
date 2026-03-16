package solutions.sulfura.hyperkit.utils.serialization.projection.jackson3;

import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.SerializationProvider;
import solutions.sulfura.hyperkit.utils.serialization.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.projection.AliasedProjectionPropertiesSerializationTest;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

import static solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

/**
 * Tests for serialization of projected dtos with field name aliasing.
 */
public class Jackson3AliasedProjectionPropertiesSerializationTest extends AliasedProjectionPropertiesSerializationTest {

    @Override
    protected SerializationProvider getSerializationProvider() {
        return new Jackson3SerializationProvider();
    }

    private static class Jackson3SerializationProvider implements SerializationProvider {

        JsonMapper jsonMapper = null;

        public Jackson3SerializationProvider() {
            jsonMapper = JsonMapper.builder()
                    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
                    .addModule(new DtoJacksonModule())
                    .addModule(new ValueWrapperJacksonModule())
                    .addModule(new ProjectedDtoJacksonModule())
                    .build();
        }

        @Override
        public String write(Object object, DtoProjection<?> projection) {
            return jsonMapper.writer().withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection).writeValueAsString(object);
        }
    }

}
