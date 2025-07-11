package solutions.sulfura.hyperkit.utils.spring.openapi.schemabuilder.stackprocessors;

import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.SchemaCreationResult;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackData;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackProcessor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.buildSchemaForStack;
import static solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils.isObjectType;


/**
 * This processor unwraps parameterized types (like List, Set or ValueWrapper), assuming the wrapped type is the first of their type parameters
 */
public class TypeReferenceStackProcessor implements StackProcessor {

    private final Set<Class<?>> referenceTypes;

    public TypeReferenceStackProcessor(Set<Class<?>> referenceTypes) {
        this.referenceTypes = referenceTypes;
    }

    public TypeReferenceStackProcessor(Class<?>... referenceTypes) {
        this.referenceTypes = new HashSet<>(Arrays.asList(referenceTypes));
    }

    @Override
    public boolean canProcess(StackData stackData) {

        if (!isObjectType(stackData.schema)) {
            return false;
        }

        if (!(stackData.schemaTargetType instanceof ParameterizedType parameterizedType)) {
            return false;
        }

        return referenceTypes.contains(parameterizedType.getRawType());

    }

    @Override
    @NonNull
    public SchemaCreationResult processStack(StackData stackData, List<StackProcessor> stackProcessors) {

        ParameterizedType parameterizedType = (ParameterizedType) stackData.schemaTargetType;
        Type unwrappedTargetType = parameterizedType.getActualTypeArguments()[0];

        StackData nextStack = new StackData(stackData.openApi,
                stackData.schema,
                unwrappedTargetType,
                stackData.projection,
                stackData.projectedClass,
                stackData.rootProjectionAnnotationInfo);

        return buildSchemaForStack(nextStack, stackProcessors);

    }

}
