package solutions.sulfura.hyperkit.utils.serialization.value_wrapper.jackson3;

import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.serialization.DeserializationProvider;
import solutions.sulfura.hyperkit.utils.serialization.jackson3.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.SerializationProvider;
import solutions.sulfura.hyperkit.utils.serialization.jackson3.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperSerializationTest;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

import static solutions.sulfura.hyperkit.utils.serialization.jackson3.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

/**
 * Tests for serialization and deserialization of value wrappers.
 */
@SuppressWarnings("ALL")
public class Jackson3ValueWrapperSerializationTest extends ValueWrapperSerializationTest {

    @Override
    protected SerializationProvider getSerializationProvider() {
        return new Jackson2SerializationProvider();
    }

    @Override
    protected DeserializationProvider getDeserializationProvider() {
        return new Jackson3DeserializationProvider();
    }

    private static class Jackson3DeserializationProvider implements DeserializationProvider {

        JsonMapper jsonMapper = null;

        public Jackson3DeserializationProvider() {
            jsonMapper = JsonMapper.builder()
                    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .addModule(new ValueWrapperJacksonModule())
                    .addModule(new DtoJacksonModule())
                    .build();
        }

        @Override
        public <T> T read(String jsonString, Class<T> clazz, DtoProjection<?> projection) {
            return jsonMapper.readerFor(clazz).withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection).readValue(jsonString);
        }
    }

    private static class Jackson2SerializationProvider implements SerializationProvider {

        JsonMapper jsonMapper = null;

        public Jackson2SerializationProvider() {
            jsonMapper = JsonMapper.builder()
                    .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
                    .addModule(new ValueWrapperJacksonModule())
                    .addModule(new DtoJacksonModule())
                    .build();
        }

        @Override
        public String write(Object object, DtoProjection<?> projection) {
            return jsonMapper.writer().withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection).writeValueAsString(object);
        }
    }

}
