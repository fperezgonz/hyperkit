package solutions.sulfura.hyperkit.utils.serialization.value_wrapper.deserialization;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;

import java.lang.reflect.Type;

public class ValueWrapperTypeModifier extends TypeModifier {

    public ValueWrapperTypeModifier() {
    }

    @Override
    public JavaType modifyType(JavaType type, Type jdkType, TypeBindings bindings, TypeFactory typeFactory) {

        if (ValueWrapper.class.isAssignableFrom(type.getRawClass())) {
            //Turn this type into a reference type
            return ReferenceType.upgradeFrom(type, type.containedTypeOrUnknown(0));
        }

        return type;
    }
}