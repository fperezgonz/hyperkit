package solutions.sulfura.hyperkit.utils.serialization.value_wrapper.jackson2;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.serialization.DeserializationProvider;
import solutions.sulfura.hyperkit.utils.serialization.DtoJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.SerializationProvider;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperJacksonModule;
import solutions.sulfura.hyperkit.utils.serialization.value_wrapper.ValueWrapperSerializationTest;

import java.io.IOException;

import static solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

/**
 * Tests for serialization and deserialization of value wrappers.
 */
@SuppressWarnings("ALL")
public class Jackson2ValueWrapperSerializationTest extends ValueWrapperSerializationTest {

    private ObjectMapper objectMapper;

    @Override
    protected SerializationProvider getSerializationProvider() {
        return new Jackson2SerializationProvider();
    }

    @Override
    protected DeserializationProvider getDeserializationProvider() {
        return new Jackson2DeserializationProvider();
    }

    private static class Jackson2DeserializationProvider implements DeserializationProvider {

        ObjectMapper objectMapper = new ObjectMapper();

        public Jackson2DeserializationProvider() {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new ValueWrapperJacksonModule());
            objectMapper.registerModule(new DtoJacksonModule());
            // Add Jackson standard modules to test compatibility issues
            objectMapper.registerModule(new Jdk8Module());
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.registerModule(new ParameterNamesModule());
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

    private static class Jackson2SerializationProvider implements SerializationProvider {

        ObjectMapper objectMapper = new ObjectMapper();

        public Jackson2SerializationProvider() {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new ValueWrapperJacksonModule());
            objectMapper.registerModule(new DtoJacksonModule());
            // Add Jackson standard modules to test compatibility issues
            objectMapper.registerModule(new Jdk8Module());
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.registerModule(new ParameterNamesModule());
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
