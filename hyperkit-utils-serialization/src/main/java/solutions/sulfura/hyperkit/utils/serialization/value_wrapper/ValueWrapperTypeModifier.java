package solutions.sulfura.hyperkit.utils.serialization.value_wrapper;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;
import solutions.sulfura.hyperkit.dtos.ValueWrapperAdapter;

import java.lang.reflect.Type;

public class ValueWrapperTypeModifier extends TypeModifier {

    private final ValueWrapperAdapter<?> adapter;

    public ValueWrapperTypeModifier(ValueWrapperAdapter<?> adapter) {
        this.adapter = adapter;
    }

    @Override
    public JavaType modifyType(JavaType type, Type jdkType, TypeBindings bindings, TypeFactory typeFactory)
    {
        final Class<?> raw = type.getRawClass();
        if (adapter.isSupportedWrapperType(raw)) {
            //Turn this type into a reference type
            return ReferenceType.upgradeFrom(type, type.containedTypeOrUnknown(0));
        }
        return type;
    }
}