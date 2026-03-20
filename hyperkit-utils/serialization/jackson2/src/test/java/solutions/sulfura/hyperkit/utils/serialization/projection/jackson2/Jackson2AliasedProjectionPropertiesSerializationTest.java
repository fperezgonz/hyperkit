package solutions.sulfura.hyperkit.utils.serialization.projection.jackson2;

import com.fasterxml.jackson.databind.ObjectMapper;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.serialization.jackson2.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.SerializationProvider;
import solutions.sulfura.hyperkit.utils.serialization.jackson2.alias.ProjectedDtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.projection.AliasedProjectionPropertiesSerializationTest;
import solutions.sulfura.hyperkit.utils.serialization.jackson2.value_wrapper.ValueWrapperJacksonModule;

import java.io.IOException;

import static solutions.sulfura.hyperkit.utils.serialization.jackson2.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

/**
 * Tests for serialization of projected dtos with field name aliasing.
 */
public class Jackson2AliasedProjectionPropertiesSerializationTest extends AliasedProjectionPropertiesSerializationTest {

    @Override
    protected SerializationProvider getSerializationProvider() {
        return new Jackson2SerializationProvider();
    }

    private static class Jackson2SerializationProvider implements SerializationProvider {

        ObjectMapper objectMapper = new ObjectMapper();

        public Jackson2SerializationProvider() {
            objectMapper.registerModule(new DtoJacksonModule());
            objectMapper.registerModule(new ValueWrapperJacksonModule());
            objectMapper.registerModule(new ProjectedDtoJacksonModule());
        }

        @Override
        public String write(Object object, DtoProjection<?> projection) {
            try {
                return objectMapper.writer().withAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection).writeValueAsString(object);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
