package solutions.sulfura.hyperkit.utils.spring.openapi.schemabuilder.stackprocessors;

import io.swagger.v3.oas.models.media.Schema;
import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.SchemaCreationResult;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackData;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackProcessor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import static solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.buildSchemaForStack;

public class ArrayStackProcessor implements StackProcessor {

    protected static Type getItemsType(Type type) {

        if (type instanceof ParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments()[0];
        } else if (type instanceof Class<?> clazz && clazz.isArray()) {
            clazz = clazz.getComponentType();
            return clazz;
        } else {
            // TODO
            throw new IllegalArgumentException("Elements of type " + type + " are not supported");
        }

    }

    @Override
    public boolean canProcess(StackData stackData) {
        return Objects.equals(stackData.schema.getType(), "array");
    }

    @Override
    @NonNull
    public SchemaCreationResult processStack(StackData stackData, List<StackProcessor> stackProcessors) {

        Schema<?> schema = stackData.schema;
        Schema<?> itemsSchema = schema.getItems();

        if (itemsSchema == null) {
            return new SchemaCreationResult(schema, stackData.schemaProcessingCounts);
        }

        if (itemsSchema.get$ref() == null
                && (itemsSchema.getType() == null || itemsSchema.getType().isBlank())
                && (itemsSchema.getTypes() == null || itemsSchema.getTypes().isEmpty())
        ) {
            return new SchemaCreationResult(schema, stackData.schemaProcessingCounts);
        }

        Type itemsType = getItemsType(stackData.schemaTargetType);

        StackData itemsStack = new StackData(stackData.openApi,
                itemsSchema,
                itemsType,
                stackData.projection,
                stackData.projectedClass,
                stackData.rootProjectionAnnotationInfo,
                stackData.currentNamespace,
                stackData.schemaProcessingCounts);

        SchemaCreationResult replicatedItemsSchemaResult = buildSchemaForStack(itemsStack, stackProcessors);

        if (replicatedItemsSchemaResult.resultingSchema == itemsSchema) {
            return new SchemaCreationResult(schema, stackData.schemaProcessingCounts);
        }

        Schema<?> result = new Schema<>();

        result.setType(schema.getType());
        result.setFormat(schema.getFormat());
        result.setItems(replicatedItemsSchemaResult.resultingSchema);

        replicatedItemsSchemaResult.newAnonymousSchemas.add(result);
        replicatedItemsSchemaResult.resultingSchema = result;

        return replicatedItemsSchemaResult;

    }
}
