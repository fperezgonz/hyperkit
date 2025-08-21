package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.dtos.Dto;

import java.lang.reflect.*;
import java.util.*;

public class SchemaBuilderUtils {

    public static Schema<?> findReferencedModel(OpenAPI openApi, Schema<?> schema) {

        if (schema.get$ref() == null) {
            return schema;
        }

        String referencedSchemaName = schema.get$ref().substring(schema.get$ref().lastIndexOf('/') + 1);

        return openApi.getComponents().getSchemas().get(referencedSchemaName);

    }

    public static boolean isObjectType(Schema<?> schema) {

        if (Objects.equals(schema.getType(), "object")) {
            return true;
        }

        return schema.getTypes() != null && schema.getTypes().contains("object");

    }

    public static Class<?> getRawType(Type type) {

        if (type instanceof AnnotatedType annotatedType) {
            type = annotatedType.getType();
        }

        if (type instanceof ParameterizedType parameterizedType) {
            return (Class<?>) parameterizedType.getRawType();
        }

        if (type instanceof Class<?> clazz) {
            return clazz;
        }

        return null;

    }

    public static Schema<?> getSchemaForField(Schema<?> schema, String fieldName) {

        if (schema.getProperties() == null) {
            return null;
        }

        return (Schema<?>) schema.getProperties().get(fieldName);

    }

    public static boolean isDtoType(Type type) {

        Class<?> rawType = null;

        if (type instanceof ParameterizedType parameterizedType) {
            rawType = (Class<?>) parameterizedType.getRawType();
        } else if (type instanceof Class<?> clazz) {
            rawType = clazz;
        }

        if (rawType == null) {
            return false;
        }

        return Dto.class.isAssignableFrom(rawType);

    }

    public static int getTypeArgumentIndex(TypeVariable<?>[] typeArguments, Type typeVariable) {
        for (int i = 0; i < typeArguments.length; i++) {
            if (typeArguments[i].equals(typeVariable)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Builds the type hierarchy from bottom type to top type and returns it. The results are ordered from bottom to top
     */
    private static List<ParameterizedType> buildTypeHierarchy(Type bottomType, Class<?> topClass) {

        if (topClass == bottomType) {
            return null;
        }

        Class<?> rawAuxType = getRawType(bottomType);

        List<ParameterizedType> typeHierarchy = new ArrayList<>();

        if (bottomType instanceof ParameterizedType) {
            typeHierarchy.add((ParameterizedType) bottomType);
        }

        // Build the hierarchy
        while (rawAuxType != topClass) {

            Type auxType = rawAuxType.getGenericSuperclass();
            if (auxType instanceof ParameterizedType parameterizedAuxType) {
                typeHierarchy.add(parameterizedAuxType);
            }

            rawAuxType = getRawType(auxType);

        }

        return typeHierarchy;

    }

    protected static Type resolveTypeVariable(TypeVariable<?> typeArg, List<ParameterizedType> typeHierarchy, int startIndex) {

        Type resolvedType = typeArg;

        // Walk the hierarchy from top to bottom replacing the type argument until a concrete type is found
        for (int i = startIndex; i >= 0; i--) {

            ParameterizedType currentType = typeHierarchy.get(i);
            Type[] typeArguments = currentType.getActualTypeArguments();

            if (!(currentType.getRawType() instanceof Class<?> currentClass)) {
                // Could not resolve
                return null;
            }

            TypeVariable<?>[] typeParameters = currentClass.getTypeParameters();
            int argumentIdx = getTypeArgumentIndex(typeParameters, resolvedType);

            if (argumentIdx == -1) {
                return null;
            }

            resolvedType = typeArguments[argumentIdx];

            // If we've resolved to a concrete type (not a TypeVariable), process it
            if (!(resolvedType instanceof TypeVariable)) {
                // If the resolved type is a parameterized type, we need to recursively resolve its type arguments
                if (resolvedType instanceof ParameterizedType parameterizedType) {
                    resolvedType = resolveParameterizedType(parameterizedType, typeHierarchy, i - 1);
                }

                return resolvedType;
            }

        }

        return typeArg;

    }

    /**
     * Resolves the type of a generic property for a target type, assuming that the target type or an intermediate type
     * in the hierarchy specifies a concrete type for the type parameter.
     * This method handles both simple type variables and nested parameterized types with type variables. <br>
     * See the tests for concrete examples
     *
     * @param targetType            The parameterized type under analysis
     * @param propertyPrimaryMember The field or method declaring the type that needs resolving
     * @param propertyType          The type that needs resolving
     * @return The resolved concrete type, or null if it cannot be resolved
     */
    public static Type resolveTypeForField(@NonNull Type targetType, @NonNull Member propertyPrimaryMember, @NonNull Type propertyType) {

        Class<?> declaringClass = propertyPrimaryMember.getDeclaringClass();
        List<ParameterizedType> typeHierarchy = buildTypeHierarchy(targetType, declaringClass);

        if (typeHierarchy == null) {
            return null;
        }

        Type resolvedType = null;

        if (propertyType instanceof TypeVariable<?> typeVariable) {
            resolvedType = resolveTypeVariable(typeVariable, typeHierarchy, typeHierarchy.size() - 1);
        } else if (propertyType instanceof ParameterizedType parameterizedType) {
            resolvedType = resolveParameterizedType(parameterizedType, typeHierarchy, typeHierarchy.size() - 1);
        }

        return resolvedType instanceof TypeVariable ? null : resolvedType;

    }

    /**
     * Resolves type variables in a nested parameterized type by walking the type hierarchy
     *
     * @param parameterizedType The parameterized type whose type arguments need to be resolved
     * @param typeHierarchy     The type hierarchy to use for resolution
     * @param startIndex        The index in the hierarchy to start from
     * @return The resolved parameterized type with concrete type arguments
     */
    private static Type resolveParameterizedType(
            ParameterizedType parameterizedType,
            List<ParameterizedType> typeHierarchy,
            int startIndex) {

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        // Create a new array to hold the resolved type arguments
        Type[] resolvedTypeArgs = new Type[actualTypeArguments.length];

        // Resolve each type argument
        for (int j = 0; j < actualTypeArguments.length; j++) {

            Type typeArg = actualTypeArguments[j];

            // If the type argument is itself a parameterized type, recursively resolve it
            if (typeArg instanceof ParameterizedType nestedType) {
                resolvedTypeArgs[j] = resolveParameterizedType(nestedType, typeHierarchy, startIndex);
                continue;
            }

            // If it is not a TypeVariable, use the type as is
            if (!(typeArg instanceof TypeVariable<?> typeVar)) {
                resolvedTypeArgs[j] = typeArg;
                continue;
            }

            Type resolvedTypeArg = resolveTypeVariable(typeVar, typeHierarchy, startIndex);
            resolvedTypeArgs[j] = resolvedTypeArg;

        }

        return new ParameterizedTypeImpl(resolvedTypeArgs, parameterizedType);

    }

    public static class ParameterizedTypeImpl implements ParameterizedType {

        private final Type[] actualTypeArguments;
        private final Type rawType;
        private final Type ownerType;

        public ParameterizedTypeImpl(@NonNull Type[] actualTypeArguments, @NonNull ParameterizedType parameterizedPropertyType) {
            this.actualTypeArguments = actualTypeArguments;
            this.rawType = parameterizedPropertyType.getRawType();
            this.ownerType = parameterizedPropertyType.getOwnerType();
        }

        @Override
        @NonNull
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        @NonNull
        public Type getRawType() {
            return rawType;
        }

        @Override
        public Type getOwnerType() {
            return ownerType;
        }

    }

}
