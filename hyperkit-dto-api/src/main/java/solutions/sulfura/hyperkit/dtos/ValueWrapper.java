package solutions.sulfura.hyperkit.dtos;

import java.util.Objects;

/**
 * A wrapper for a value that may be present or absent
 *
 * @param <T> the type of the wrapped value
 */
public class ValueWrapper<T> {
    private final T value;
    private final boolean empty;

    /**
     * Creates a new ValueWrapper with the given value.
     *
     * @param value the value to wrap
     */
    public ValueWrapper(T value) {
        this.value = value;
        this.empty = false;
    }

    private ValueWrapper() {
        this.value = null;
        this.empty = true;
    }

    /**
     * Creates an empty ValueWrapper.
     *
     * @param <T> the type of the wrapped value
     * @return an empty ValueWrapper
     */
    public static <T> ValueWrapper<T> empty() {
        return new ValueWrapper<>();
    }

    /**
     * Creates a ValueWrapper with the given value.
     *
     * @param value the value to wrap
     * @param <T> the type of the wrapped value
     * @return a ValueWrapper containing the given value
     */
    public static <T> ValueWrapper<T> of(T value) {
        return new ValueWrapper<>(value);
    }

    /**
     * Returns the wrapped value.
     *
     * @return the wrapped value
     */
    public T get() {
        return value;
    }

    /**
     * Returns whether this ValueWrapper is empty.
     *
     * @return true if this ValueWrapper is empty, false otherwise
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Returns whether this ValueWrapper is not empty.
     *
     * @return true if this ValueWrapper is not empty, false otherwise
     */
    public boolean isPresent() {
        return !empty;
    }

    /**
     * Returns the wrapped value or null if this ValueWrapper is empty.
     *
     * @return the wrapped value or null
     */
    public T getOrNull() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueWrapper<?> that = (ValueWrapper<?>) o;
        return empty == that.empty && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, empty);
    }
}