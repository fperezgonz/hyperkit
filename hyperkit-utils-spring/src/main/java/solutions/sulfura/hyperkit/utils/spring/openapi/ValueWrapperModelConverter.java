package solutions.sulfura.hyperkit.utils.spring.openapi;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;

import java.util.Iterator;

/**
 * Unwraps {@link ValueWrapper} types when generating an openapi model
 */
public class ValueWrapperModelConverter implements ModelConverter {

    private static boolean isValueWrapperType(JavaType jType) {
        return ValueWrapper.class.getCanonicalName().equals(jType.getRawClass().getCanonicalName()) || jType.isReferenceType();
    }

    private Schema<?> nextResolver(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
    }

    @Override
    public Schema<?> resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {

        AnnotatedType unwrappedType = ValueWrapperTypeUtils.unwrapReference(type, ValueWrapperModelConverter::isValueWrapperType);

        if (unwrappedType == null) {
            return nextResolver(type, context, chain);
        }

        return context.resolve(unwrappedType);

    }

    @Override
    public boolean isOpenapi31() {
        return true;
    }

}
