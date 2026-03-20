package solutions.sulfura.hyperkit.utils.serialization.jackson3.value_wrapper.deserialization;

import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.type.ReferenceType;
import tools.jackson.databind.type.TypeBindings;
import tools.jackson.databind.type.TypeFactory;
import tools.jackson.databind.type.TypeModifier;

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