package solutions.sulfura.hyperkit.utils.serialization.projection.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.serialization.DeserializationProvider;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.projection.AliasedProjectionPropertiesDeserializationTest;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;

import java.io.IOException;

import static solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

/**
 * Tests for deserialization of projected dtos with field name aliasing.
 */
public class Jackson2AliasedProjectionPropertiesDeserializationTest extends AliasedProjectionPropertiesDeserializationTest {

    @Override
    protected DeserializationProvider getDeserializationProvider() {
        return new Jackson2DeserializationProvider();
    }

    private static class Jackson2DeserializationProvider implements DeserializationProvider {

        ObjectMapper objectMapper = new ObjectMapper();

        public Jackson2DeserializationProvider() {
            objectMapper.registerModule(new DtoJacksonModule());
            objectMapper.registerModule(new ValueWrapperJacksonModule());
            objectMapper.registerModule(new ProjectedDtoJacksonModule());
        }

        @Override
        public <T> T read(String jsonString, Class<T> clazz, DtoProjection<?> projection) {
            try {
                return objectMapper.reader().withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection).readValue(jsonString, clazz);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
