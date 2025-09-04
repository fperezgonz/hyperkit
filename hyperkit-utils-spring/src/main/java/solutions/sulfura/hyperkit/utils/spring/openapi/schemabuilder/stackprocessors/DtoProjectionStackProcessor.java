package solutions.sulfura.hyperkit.utils.spring.openapi.schemabuilder.stackprocessors;

import io.swagger.v3.oas.models.media.Schema;
import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.SchemaCreationResult;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackData;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackProcessor;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils.findDefaultProjectionClass;
import static solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.buildSchemaForStack;
import static solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils.*;

/**
 * Applies projections to schemas
 */
public class DtoProjectionStackProcessor extends DefaultObjectStackProcessor {

    @Override
    @NonNull
    protected PropertySchemaCreationResult buildPropertySchemas(StackData stackData, List<StackProcessor> stackProcessors) {

        // If the projection applies to the current schema, create a new schema with the projection applied
        Schema<?> schema = stackData.schema;
        Type projectedType = stackData.schemaTargetType;
        // New nested namespace for properties
        String newNamespace = getNameForCurrentType(stackData, stackData.schemaProcessingCounts);
        Map<String, Integer> schemaProcessingCounts = new HashMap<>();
        //noinspection unchecked
        Class<? extends Dto<?>> projectedClass = (Class<? extends Dto<?>>) getRawType(projectedType);

        if (projectedClass == null) {
            throw new RuntimeException("Projecting type " + projectedType + " is not supported (only ParameterizedType and Class are supported");
        }

        //noinspection rawtypes
        Class<? extends DtoProjection> projectionClass = findDefaultProjectionClass(projectedClass);

        if (projectionClass == null) {
            return new PropertySchemaCreationResult(new HashMap<>(), schemaProcessingCounts, new HashSet<>());
            // TODO log warning or error
        }

        DtoProjection<?> projection = stackData.projection;

        // Add only the fields that are included in the projection
        Map<String, SchemaCreationResult> schemaCreationResults = new HashMap<>();

        for (Field projectionField : projectionClass.getDeclaredFields()) {

            if (!projectionField.accessFlags().contains(AccessFlag.PUBLIC)) {
                continue;
            }

            if (!FieldConf.class.isAssignableFrom(projectionField.getType())) {
                continue;
            }

            try {

                FieldConf fieldConf = (FieldConf) projectionField.get(projection);

                if (fieldConf == null || fieldConf.getPresence() == FieldConf.Presence.IGNORED) {
                    continue;
                }

                Schema<?> oldFieldSchema = getSchemaForField(schema, projectionField.getName());
                if (oldFieldSchema == null) {
                    continue;
                    //TODO log info
                }

                Field dtoField;

                if (stackData.schemaTargetType instanceof ParameterizedType parameterizedType) {
                    dtoField = ((Class<?>) parameterizedType.getRawType()).getDeclaredField(projectionField.getName());
                } else if (stackData.schemaTargetType instanceof Class<?> clazz) {
                    dtoField = clazz.getDeclaredField(projectionField.getName());
                } else {
                    throw new RuntimeException("Unsupported field type");
                }

                if (!dtoField.accessFlags().contains(AccessFlag.PUBLIC)) {
                    continue;
                }

                DtoProjection<?> fieldStackProjection = stackData.projection;

                if (fieldConf instanceof DtoFieldConf<?> dtoFieldConf) {
                    fieldStackProjection = dtoFieldConf.dtoProjection;
                }

                StackData fieldStackData = new StackData(stackData.openApi,
                        oldFieldSchema,
                        dtoField.getGenericType(),
                        fieldStackProjection,
                        projectedClass,
                        stackData.rootProjectionAnnotationInfo,
                        newNamespace,
                        schemaProcessingCounts,
                        stackData.schemaCache);

                SchemaCreationResult fieldSchemaResult = buildSchemaForStack(fieldStackData, stackProcessors);

                if (fieldSchemaResult.resultingSchema != oldFieldSchema) {
                    fieldSchemaResult.schemaHasChanged = true;
                }

                schemaCreationResults.put(projectionField.getName(), fieldSchemaResult);
                schemaProcessingCounts.putAll(fieldSchemaResult.schemaProcessingCounts);

            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }

        }

        HashSet<String> removedProperties = new HashSet<>();

        if (schema.getProperties() != null) {
            schema.getProperties().keySet().stream()
                    .filter(key -> !schemaCreationResults.containsKey(key))
                    .collect(Collectors.toCollection(() -> removedProperties));
        }

        return new PropertySchemaCreationResult(schemaCreationResults, stackData.schemaProcessingCounts, removedProperties);

    }

    @Override
    public boolean canProcess(StackData stackData) {
        return isObjectType(stackData.schema)
                && isDtoType(stackData.schemaTargetType);
    }

}
