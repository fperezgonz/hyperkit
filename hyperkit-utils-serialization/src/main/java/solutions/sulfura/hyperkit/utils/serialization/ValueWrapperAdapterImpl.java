package solutions.sulfura.hyperkit.utils.serialization;

import solutions.sulfura.hyperkit.dtos.ValueWrapper;

/**
 * Implementation of ValueWrapperAdapter for ValueWrapper
 */
public class ValueWrapperAdapterImpl implements ValueWrapperAdapter<ValueWrapper<?>> {

    @Override
    public ValueWrapper<?> empty() {
        return ValueWrapper.empty();
    }

    @Override
    public ValueWrapper<?> wrap(Object value) {
        return ValueWrapper.of(value);
    }

    @Override
    public Object unwrap(ValueWrapper<?> valueWrapper) {
        return valueWrapper.get();
    }

    @Override
    public boolean isEmpty(ValueWrapper<?> valueWrapper) {
        return valueWrapper.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<ValueWrapper<?>> getWrapperClass() {
        return (Class<ValueWrapper<?>>) (Class<?>) ValueWrapper.class;
    }
}