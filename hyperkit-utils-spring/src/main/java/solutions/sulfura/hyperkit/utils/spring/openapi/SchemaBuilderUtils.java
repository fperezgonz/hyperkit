package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import solutions.sulfura.hyperkit.dtos.Dto;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

}
