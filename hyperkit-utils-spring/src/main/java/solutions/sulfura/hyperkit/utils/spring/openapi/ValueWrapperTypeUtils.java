package solutions.sulfura.hyperkit.utils.spring.openapi;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.util.Json;

import java.util.function.Function;

public class ValueWrapperTypeUtils {


    /**
     * Check if the type is a reference type, returns the unwrapped type in that case, otherwise null
     */
    public static AnnotatedType unwrapReference(AnnotatedType type, Function<JavaType, Boolean> referenceTypeChecker) {

        if (type == null || type.getType() == null) {
            return null;
        }

        final JavaType jType;
        if (type.getType() instanceof JavaType javaType) {
            jType = javaType;
        } else {
            jType = Json.mapper().constructType(type.getType());
        }

        if (!referenceTypeChecker.apply(jType)) {
            return null;
        }

        return new AnnotatedType()
                .type(jType.containedType(0))
                .name(type.getName())
                .parent(type.getParent())
                .jsonUnwrappedHandler(type.getJsonUnwrappedHandler())
                .skipOverride(type.isSkipOverride())
                .schemaProperty(type.isSchemaProperty())
                .ctxAnnotations(type.getCtxAnnotations())
                .resolveAsRef(type.isResolveAsRef())
                .jsonViewAnnotation(type.getJsonViewAnnotation())
                .skipSchemaName(type.isSkipSchemaName())
                .skipJsonIdentity(type.isSkipJsonIdentity())
                .components(type.getComponents())
                .propertyName(type.getPropertyName());

    }

}
