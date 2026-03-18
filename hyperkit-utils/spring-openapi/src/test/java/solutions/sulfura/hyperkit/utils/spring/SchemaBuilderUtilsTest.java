package solutions.sulfura.hyperkit.utils.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class SchemaBuilderUtilsTest {

    static class A<X, V> { V value; }
    static class B<P,Z, P2> extends A<String, Z> {}
    static class C<Z> extends B<Boolean, Integer, Z> {}
    static class B2<Z> extends A<Z, String> {}

    @Test
    public void typeResolutionTest() throws Exception {

        // Given
        Field field = A.class.getDeclaredField("value");

        // When
        Type resolved = SchemaBuilderUtils.resolveTypeForField(C.class, field, field.getGenericType());
        // Then
        Assertions.assertEquals(Integer.class, resolved);

        // When
        resolved = SchemaBuilderUtils.resolveTypeForField(B2.class, field, field.getGenericType());
        // Then
        Assertions.assertEquals(String.class, resolved);

        // When
        resolved = SchemaBuilderUtils.resolveTypeForField(B.class, field, field.getGenericType());
        // Then
        Assertions.assertNull(resolved);
    }

    static class AN<V2, V> { public V value; public V2 value2; }
    static class BN<P,Z, P2> extends AN<AN<P2, String>, List<List<Z>>> {}
    static class CN<P2> extends BN<P2, Integer, String> {}

    @Test
    public void shouldResolveTypeVariableInParameterizedType() throws NoSuchFieldException {

        // Given
        Field field = AN.class.getDeclaredField("value");
        Field field2 = AN.class.getDeclaredField("value2");

        // When
        Type resolved = SchemaBuilderUtils.resolveTypeForField(CN.class, field, field.getGenericType());

        // Then
        Assertions.assertTrue(List.class.isAssignableFrom((Class<?>) ((ParameterizedType) Objects.requireNonNull(resolved)).getRawType()));
        ParameterizedType nestedListType = (ParameterizedType) ((ParameterizedType) resolved).getActualTypeArguments()[0];
        Assertions.assertEquals(Integer.class, nestedListType.getActualTypeArguments()[0]);


        // When
        resolved = SchemaBuilderUtils.resolveTypeForField(CN.class, field2, field2.getGenericType());

        // Then
        Assertions.assertTrue(AN.class.isAssignableFrom((Class<?>) ((ParameterizedType) Objects.requireNonNull(resolved)).getRawType()));
        Assertions.assertEquals(String.class, ((ParameterizedType) resolved).getActualTypeArguments()[1]);

    }

}
