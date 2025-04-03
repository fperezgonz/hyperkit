package solutions.sulfura.hyperkit.dtos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueWrapperTest {

    @Test
    @DisplayName("empty() should create an empty ValueWrapper")
    void empty_shouldCreateEmptyValueWrapper() {
        // Given: We need an empty ValueWrapper

        // When: We call the empty() factory method
        ValueWrapper<String> wrapper = ValueWrapper.empty();

        // Then: The wrapper should be empty
        assertTrue(wrapper.isEmpty());
        assertFalse(wrapper.isPresent());
        assertThrows(IllegalStateException.class, wrapper::get);
        assertNull(wrapper.getOrNull());
    }

    @Test
    @DisplayName("of(null) should create a non-empty ValueWrapper with a null value")
    void of_withNull_shouldCreateNonEmptyValueWrapperWithNullValue() {
        // Given: We have a null value

        // When: We create a ValueWrapper with the null value
        ValueWrapper<String> wrapper = ValueWrapper.of(null);

        // Then: The wrapper should be non-empty but contain null
        assertFalse(wrapper.isEmpty());
        assertTrue(wrapper.isPresent());
        assertNull(wrapper.get());
        assertNull(wrapper.getOrNull());
    }

    @Test
    @DisplayName("of() should create a non-empty ValueWrapper with the given value")
    void of_shouldCreateNonEmptyValueWrapper() {
        // Given: We have a value
        String value = "test";

        // When: We create a ValueWrapper with the value
        ValueWrapper<String> wrapper = ValueWrapper.of(value);

        // Then: The wrapper should contain the value
        assertFalse(wrapper.isEmpty());
        assertTrue(wrapper.isPresent());
        assertEquals(value, wrapper.get());
        assertEquals(value, wrapper.getOrNull());
    }

    @Test
    @DisplayName("equals() should return true for equal ValueWrappers")
    void equals_shouldReturnTrueForEqualValueWrappers() {
        // Given: We have two ValueWrappers with the same value
        ValueWrapper<String> wrapper1 = ValueWrapper.of("test");
        ValueWrapper<String> wrapper2 = ValueWrapper.of("test");

        // When/Then: They should be equal
        assertEquals(wrapper1, wrapper2);
        assertEquals(wrapper1.hashCode(), wrapper2.hashCode());
    }

    @Test
    @DisplayName("equals() should return false for different ValueWrappers")
    void equals_shouldReturnFalseForDifferentValueWrappers() {
        // Given: We have two ValueWrappers with different values
        ValueWrapper<String> wrapper1 = ValueWrapper.of("test1");
        ValueWrapper<String> wrapper2 = ValueWrapper.of("test2");

        // When/Then: They should not be equal
        assertNotEquals(wrapper1, wrapper2);
    }

    @Test
    @DisplayName("equals() should return false when comparing empty and non-empty ValueWrappers")
    void equals_shouldReturnFalseForEmptyAndNonEmptyValueWrappers() {
        // Given: We have an empty and a non-empty ValueWrapper
        ValueWrapper<String> emptyWrapper = ValueWrapper.empty();
        ValueWrapper<String> nonEmptyWrapper = ValueWrapper.of("test");

        // When/Then: They should not be equal
        assertNotEquals(emptyWrapper, nonEmptyWrapper);
    }

    @Test
    @DisplayName("equals() should return true for two empty ValueWrappers")
    void equals_shouldReturnTrueForTwoEmptyValueWrappers() {
        // Given: We have two empty ValueWrappers
        ValueWrapper<String> emptyWrapper1 = ValueWrapper.empty();
        ValueWrapper<String> emptyWrapper2 = ValueWrapper.empty();

        // When/Then: They should be equal
        assertEquals(emptyWrapper1, emptyWrapper2);
        assertEquals(emptyWrapper1.hashCode(), emptyWrapper2.hashCode());
    }

    @Test
    @DisplayName("equals() should return false when comparing with null or different type")
    void equals_shouldReturnFalseWhenComparingWithNullOrDifferentType() {
        // Given: We have a ValueWrapper
        ValueWrapper<String> wrapper = ValueWrapper.of("test");

        // When/Then: Comparing with null or different type should return false
        assertNotEquals(null, wrapper);
        assertNotEquals("test", wrapper);
    }
}
