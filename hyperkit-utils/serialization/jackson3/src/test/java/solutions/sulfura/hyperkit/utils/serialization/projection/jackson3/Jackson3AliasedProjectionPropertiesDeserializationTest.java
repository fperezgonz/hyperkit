package solutions.sulfura.hyperkit.utils.serialization.projection.jackson3;

import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.serialization.DeserializationProvider;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.projection.AliasedProjectionPropertiesDeserializationTest;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

import static solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

/**
 * Tests for deserialization of projected dtos with field name aliasing.
 */
public class Jackson3AliasedProjectionPropertiesDeserializationTest extends AliasedProjectionPropertiesDeserializationTest {

    @Override
    protected DeserializationProvider getDeserializationProvider() {
        return new Jackson2DeserializationProvider();
    }

    private static class Jackson2DeserializationProvider implements DeserializationProvider {

        JsonMapper jsonMapper = null;

        public Jackson2DeserializationProvider() {
            jsonMapper = JsonMapper.builder()
                    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
                    .addModule(new DtoJacksonModule())
                    .addModule(new ValueWrapperJacksonModule())
                    .addModule(new ProjectedDtoJacksonModule())
                    .build();
        }

        @Override
        public <T> T read(String jsonString, Class<T> clazz, DtoProjection<?> projection) {
            return jsonMapper.readerFor(clazz).withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection).readValue(jsonString);
        }
    }

}
