package solutions.sulfura.hyperkit.utils.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class SchemaBuilderUtilsTest {

    static class A<X, Y> { Y value; }
    static class B<P,Z, P2> extends A<String, Z> {}
    static class C<Z> extends B<Boolean, Integer, Z> {}
    static class B2<Z> extends A<Z, String> {}

    @Test
    public void typeResolutionTest() throws Exception {

        // Given
        Field field = A.class.getDeclaredField("value");

        // When
        Type resolved = SchemaBuilderUtils.resolveTypeVariableForField(C.class, field, ((TypeVariable<?>) field.getGenericType()));
        // Then
        Assertions.assertEquals(Integer.class, resolved);

        // When
        resolved = SchemaBuilderUtils.resolveTypeVariableForField(B2.class, field, ((TypeVariable<?>) field.getGenericType()));
        // Then
        Assertions.assertEquals(String.class, resolved);

        // When
        resolved = SchemaBuilderUtils.resolveTypeVariableForField(B.class, field, ((TypeVariable<?>) field.getGenericType()));
        // Then
        Assertions.assertNull(resolved);
    }

}
