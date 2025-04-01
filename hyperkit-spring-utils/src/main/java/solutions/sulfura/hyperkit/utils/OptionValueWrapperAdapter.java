package solutions.sulfura.hyperkit.utils;

import io.vavr.control.Option;

public class OptionValueWrapperAdapter implements ValueWrapperAdapter<Option<?>> {

    @Override
    public Option<?> empty() {
        return Option.none();
    }

    @Override
    public Option<?> wrap(Object value) {
        return Option.some(value);
    }

    @Override
    public Object unwrap(Option<?> valueWrapper) {
        return valueWrapper.getOrNull();
    }

    @Override
    public boolean isEmpty(Option<?> valueWrapper) {
        return valueWrapper.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class getWrapperClass() {
        return Option.class;
    }

}
