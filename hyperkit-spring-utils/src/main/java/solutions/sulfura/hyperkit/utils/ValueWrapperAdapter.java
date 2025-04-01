package solutions.sulfura.hyperkit.utils;

public interface ValueWrapperAdapter<T> {

    T empty();

    T wrap(Object value);

    Object unwrap(T valueWrapper);

    boolean isEmpty(T valueWrapper);

    default boolean isSupportedWrapperType(Class<?> wrapperType) {
        return getWrapperClass().isAssignableFrom(wrapperType);
    }

    default boolean isSupportedWrapperInstance(Object value) {
        return value != null && isSupportedWrapperType(value.getClass());
    }

    Class<T> getWrapperClass();

}
