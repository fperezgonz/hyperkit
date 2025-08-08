package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import solutions.sulfura.hyperkit.dtos.Dto;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * Resolves a TypeVariable to its concrete type by walking the type hierarchy
     *
     * @param targetType            The parameterized type under analysis
     * @param propertyPrimaryMember The field or method declaring the type variable
     * @param propertyType          The type variable to resolve
     * @return The resolved concrete type, or null if it cannot be resolved
     */
    public static Type resolveTypeVariableForField(Type targetType, Member propertyPrimaryMember, TypeVariable<?> propertyType) {

        Class<?> declaringClass = propertyPrimaryMember.getDeclaringClass();

        if (declaringClass == targetType) {
            return null;
        }

        Class<?> rawAuxType = getRawType(targetType);

        List<ParameterizedType> typeHierarchy = new ArrayList<>();

        if(targetType instanceof ParameterizedType){
            typeHierarchy.add((ParameterizedType) targetType);
        }

        // Build the hierarchy
        while (rawAuxType != declaringClass) {

            Type auxType = rawAuxType.getGenericSuperclass();
            if (auxType instanceof ParameterizedType parameterizedAuxType) {
                typeHierarchy.add(parameterizedAuxType);
            }

            rawAuxType = getRawType(auxType);

        }


        Type resolvedType = propertyType;

        // Walk the hierarchy from top to bottom replacing the type argument until a concrete type is found
        for (int i = typeHierarchy.size() - 1; i >= 0; i--) {

            ParameterizedType currentType = typeHierarchy.get(i);
            Type[] typeArguments = currentType.getActualTypeArguments();

            if (!(currentType.getRawType() instanceof Class<?> currentClass)) {
                // Could not resolve
                return null;
            }

            TypeVariable<?>[] typeParameters = currentClass.getTypeParameters();
            int argumentIdx = getTypeArgumentIndex(typeParameters, resolvedType);

            if(argumentIdx == -1){
                return null;
            }

            resolvedType = typeArguments[argumentIdx];

            if (!(resolvedType instanceof TypeVariable)) {
                return resolvedType;
            }

        }

        return null;

    }

}
