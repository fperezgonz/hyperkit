package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.serialization.ValueWrapperAdapterImpl;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for serialization and deserialization of value wrappers.
 */
@SuppressWarnings("ALL")
public class ValueWrapperSerializationTest {

    private ObjectMapper objectMapper;
    private ValueWrapperAdapterImpl adapter;

    @BeforeEach
    void setUp() {
        adapter = new ValueWrapperAdapterImpl();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ValueWrapperJacksonModule(adapter));
    }

    @Test
    void testSerializeString() throws IOException {
        // Given a wrapper with the string "test"
        ValueWrapper<String> wrapper = ValueWrapper.of("test");

        // When serializing it
        String json = objectMapper.writeValueAsString(wrapper);

        // Then the JSON must be "test"
        assertEquals("\"test\"", json);
    }

    @Test
    void testDeserializeString() throws IOException {
        // Given the json string "test"
        String json = "\"test\"";

        // When deserializing it
        ValueWrapper<String> deserialized = objectMapper.readValue(json, ValueWrapper.class);

        // Then the deserialized value must contain "test"
        assertEquals("test", deserialized.get());
    }

    @Test
    void testSerializeNumber() throws IOException {
        // Given a wrapper initialized with the number 42
        ValueWrapper<Integer> wrapper = ValueWrapper.of(42);

        // When serializing the wrapper into JSON format
        String json = objectMapper.writeValueAsString(wrapper);

        // Then the generated JSON string must be "42"
        assertEquals("42", json);
    }

    @Test
    void testDeserializeNumber() throws IOException {
        // Given the JSON string 42
        String json = "42";

        // When deserializing the JSON string
        ValueWrapper<Integer> deserialized = objectMapper.readValue(json, ValueWrapper.class);

        // Then the deserialized wrapper's value must equal 42
        assertEquals(42, deserialized.get());
    }

    @Test
    void testSerializeNull() throws IOException {
        // Given
        ValueWrapper<Object> wrapper = ValueWrapper.of(null);

        // When
        String json = objectMapper.writeValueAsString(wrapper);

        // Then
        assertEquals("null", json);
    }

    @Test
    void testDeserializeNull() throws IOException {
        // Given the JSON string "null"
        String json = "null";

        // When deserializing the JSON string
        ValueWrapper<Object> deserialized = objectMapper.readValue(json, ValueWrapper.class);

        // Then the deserialized wrapper's value must be null
        assertNull(deserialized.get());
    }

    @Test
    void testSerializeEmpty() throws IOException {
        // Given an empty wrapper
        ValueWrapper<Object> wrapper = ValueWrapper.empty();

        // When serializing the empty wrapper into JSON format
        String json = objectMapper.writeValueAsString(wrapper);

        // Then the resulting JSON must be an empty string
        assertEquals("", json);
    }

    @Test
    void testSerializeComplex() throws IOException {
        // Given a wrapper containing a TestPerson
        TestPerson person = new TestPerson("John", 30);
        ValueWrapper<TestPerson> wrapper = ValueWrapper.of(person);

        // When serializing the wrapper into JSON
        String json = objectMapper.writeValueAsString(wrapper);

        // Then the resulting JSON must represent the TestPerson object
        assertEquals("{\"name\":\"John\",\"age\":30}", json);
    }



    @Test
    void testDeserializeComplex() throws IOException {
        // Given the serialized json
        String json = "{\"name\":\"John\",\"age\":30}";

        // When
        ValueWrapper<?> deserialized = objectMapper.readValue(json, ValueWrapper.class);

        // Then
        // When deserializing, Jackson creates a LinkedHashMap instead of a TestPerson object
        @SuppressWarnings("unchecked")
        Map<String, Object> deserializedMap = (Map<String, Object>) deserialized.get();
        assertEquals("John", deserializedMap.get("name"));
        assertEquals(30, deserializedMap.get("age"));
    }

    @Test
    void testSerializeNestedWrapper() throws IOException {
        // Given a nested wrapper where an outer wrapper contains an inner wrapper with the string "inner"
        ValueWrapper<String> innerWrapper = ValueWrapper.of("inner");
        ValueWrapper<ValueWrapper<String>> outerWrapper = ValueWrapper.of(innerWrapper);

        // When serializing the nested wrapper into JSON format
        String json = objectMapper.writeValueAsString(outerWrapper);

        // Then the inner wrapper is unwrapped, and the outer wrapper is serialized directly as the string "inner"
        assertEquals("\"inner\"", json);
    }

    @Test
    void testSerializeEmptyString() throws IOException {
        // Given a wrapper containing an empty string
        ValueWrapper<String> wrapper = ValueWrapper.of("");

        // When serializing the empty string wrapper
        String json = objectMapper.writeValueAsString(wrapper);

        // Then the serialized JSON must be an empty string representation
        assertEquals("\"\"", json);
    }

    @Test
    void testDeserializeEmptyString() throws IOException {
        // Given a JSON string representing an empty string
        String json = "\"\"";

        // When deserializing the JSON string
        ValueWrapper<String> deserialized = objectMapper.readValue(json, ValueWrapper.class);

        // Then the deserialized value must be an empty string
        assertEquals("", deserialized.get());
    }

    @Test
    void testDeserializeFromRawValue() throws IOException {
        // Given
        String json = "42";

        // When
        ValueWrapper<Integer> deserialized = objectMapper.readValue(json, ValueWrapper.class);

        // Then
        assertEquals(42, deserialized.get());
    }

    @Test
    void testDeserializeFromEmptyJson() throws IOException {
        // Given an empty JSON string
        String json = "";

        // When attempting to deserialize the empty JSON
        // Then it must throw an IOException as the input is invalid
        assertThrows(IOException.class, () -> objectMapper.readValue(json, ValueWrapper.class));
    }

    @Test
    void testDeserializeAbsentProperty() throws IOException {
        // Given a JSON object with no wrapper property
        String json = "{\"otherProperty\":\"value\"}";

        // When deserializing, the wrapper must be initialized as empty
        TestValueWrapperContainer deserialized = objectMapper.readValue(json, TestValueWrapperContainer.class);

        // Then the wrapper must be empty since no property is present in the JSON
        assertTrue(deserialized.getWrapper().isEmpty());
    }

    @Test
    void testDeserializeObjectWithValueWrapper() throws IOException {
        // Given a JSON object containing a ValueWrapper property
        String json = "{ \"wrapper\": \"testValue\" }";

        // When deserializing the JSON string into a container object
        TestValueWrapperContainer deserialized = objectMapper.readValue(json, TestValueWrapperContainer.class);

        // Then the deserialized wrapper must contain "testValue"
        assertEquals("testValue", deserialized.getWrapper().get());
    }

    @Test
    void testSerializeObjectWithValueWrapper() throws IOException {
        // Given a container object with a ValueWrapper containing "wrappedValue"
        TestValueWrapperContainer container = new TestValueWrapperContainer();
        container.setWrapper(ValueWrapper.of("wrappedValue"));

        // When serializing the container
        String json = objectMapper.writeValueAsString(container);

        // Then the resulting JSON must contain the property with the same value
        assertEquals("{\"wrapper\":\"wrappedValue\"}", json);
    }

    @Test
    void testSerializeObjectWithEmptyValueWrapper() throws IOException {
        // Given a container object with a ValueWrapper containing "wrappedValue"
        TestValueWrapperContainer container = new TestValueWrapperContainer();
        container.setWrapper(ValueWrapper.of("wrappedValue"));

        // When serializing the container
        String json = objectMapper.writeValueAsString(container);

        // Then the resulting JSON must contain the property with the same value
        assertEquals("{\"wrapper\":\"wrappedValue\"}", json);
    }


    /**
     * Test class for testing presence or absence of a property of type ValueWrapper
     */
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
    private static class TestValueWrapperContainer {
        private ValueWrapper<String> wrapper;

        public TestValueWrapperContainer() {
            // Default constructor for Jackson
            // Initialize wrapper with an empty ValueWrapper
            this.wrapper = ValueWrapper.empty();
        }

        public ValueWrapper<String> getWrapper() {
            return wrapper;
        }

        public void setWrapper(ValueWrapper<String> wrapper) {
            this.wrapper = wrapper;
        }
    }

    /**
     * Test class for complex object serialization/deserialization.
     */
    private static class TestPerson {
        private String name;
        private int age;

        public TestPerson() {
            // Default constructor for Jackson
        }

        public TestPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
