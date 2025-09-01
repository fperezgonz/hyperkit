package solutions.sulfura.hyperkit.utils.spring.openapi.schemabuilder.stackprocessors;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.SchemaCreationResult;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackData;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackProcessor;
import solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.buildSchemaForStack;
import static solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils.getRawType;
import static solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils.isObjectType;

public class DefaultObjectStackProcessor implements StackProcessor {

    protected String getNameForCurrentType(StackData stackData, Map<String, Integer> schemaProcessingCounts) {

        Schema<?> schema = stackData.schema;
        Type schemaTargetType = stackData.schemaTargetType;
        Class<?> targetClass = getRawType(schemaTargetType);

        // Generate a name for the projected schema
        String projectedSchemaName = schema.getName();

        if (projectedSchemaName == null || projectedSchemaName.isBlank()) {
            projectedSchemaName = targetClass.getSimpleName();
        }

        if (!stackData.currentNamespace.isBlank()) {
            projectedSchemaName = stackData.currentNamespace + "_" + projectedSchemaName;
        }

        if (schemaProcessingCounts.getOrDefault(schema.getName(), 0) > 0) {
            projectedSchemaName += "_" + schemaProcessingCounts.get(schema.getName());
        }


        return projectedSchemaName;
    }

    /**
     * Rebuilds each property schema in the target type schema using the stack info
     */
    @NonNull
    protected PropertySchemaCreationResult buildPropertySchemas(StackData stackData, List<StackProcessor> stackProcessors) {

        Schema<?> schema = stackData.schema;

        if (schema.getProperties() == null) {
            throw new IllegalArgumentException("Schema " + schema.getName() + " has no properties");
        }

        Type schemaTargetType = stackData.schemaTargetType;
        BeanDescription beanDescription = Json.mapper().getSerializationConfig().introspect(Json.mapper().constructType(schemaTargetType));
        Map<String, BeanPropertyDefinition> beanProperties = beanDescription.findProperties().stream()
                .collect(Collectors.toMap(BeanPropertyDefinition::getName, propDef -> propDef));

        Map<String, Integer> schemaProcessingCounts = new HashMap<>();
        HashMap<String, SchemaCreationResult> schemaCreationResults = new HashMap<>();
        Set<String> removedProperties = new HashSet<>();

        //noinspection rawtypes
        for (Map.Entry<String, Schema> entry : schema.getProperties().entrySet()) {

            String propertyName = entry.getKey();
            Schema<?> oldFieldSchema = entry.getValue();

            if (!beanProperties.containsKey(propertyName)) {
                removedProperties.add(propertyName);
                continue;
            }

            BeanPropertyDefinition beanPropertyDefinition = beanProperties.get(propertyName);
            Member propertyPrimaryMember = beanPropertyDefinition.getPrimaryMember().getMember();

            Type propertyType = null;

            if ((propertyPrimaryMember instanceof Field field)) {
                propertyType = field.getGenericType();
            } else if (propertyPrimaryMember instanceof Method method) {

                //Getter
                if (method.getParameterCount() == 0) {
                    propertyType = method.getGenericReturnType();
                }

                // Setter
                else if (method.getParameterCount() == 1) {
                    propertyType = method.getGenericParameterTypes()[0];
                }

            }

            if (propertyType instanceof TypeVariable<?> ||
                    (propertyType instanceof ParameterizedType && schemaTargetType instanceof ParameterizedType)
            ) {
                propertyType = SchemaBuilderUtils.resolveTypeForField(schemaTargetType, propertyPrimaryMember, propertyType);
            }

            if (propertyType == null) {
                throw new RuntimeException("Unsupported property " + propertyName + " on " + schemaTargetType);
            }

            StackData fieldStackData = new StackData(
                    stackData.openApi,
                    oldFieldSchema,
                    propertyType,
                    stackData.projection,
                    stackData.projectedClass,
                    stackData.rootProjectionAnnotationInfo,
                    stackData.currentNamespace,
                    schemaProcessingCounts,
                    stackData.schemaCache);

            var schemaCreationResult = buildSchemaForStack(fieldStackData, stackProcessors);

            schemaCreationResults.put(propertyName, schemaCreationResult);
            schemaProcessingCounts.putAll(schemaCreationResult.schemaProcessingCounts);
        }

        // Use stackData.schemaProcessingCounts to pop the namespace
        return new PropertySchemaCreationResult(schemaCreationResults, stackData.schemaProcessingCounts, removedProperties);

    }

    protected SchemaCreationResult buildNewSchemaForTargetType(StackData stackData,
                                                               PropertySchemaCreationResult propertySchemaCreationResult,
                                                               Map<String, Integer> schemaProcessingCounts
    ) {

        Schema<?> schema = stackData.schema;

        // If any of the results created a new schema, replicate the schema for the current stack, change the name if necessary and return it
        Schema<?> projectedSchema = new Schema<>();
        projectedSchema.setType(schema.getType());
        projectedSchema.setFormat(schema.getFormat());

        // Generate a name for the projected schema
        String projectedSchemaName = getNameForCurrentType(stackData, schemaProcessingCounts);

        projectedSchema.setName(projectedSchemaName);

        // Add properties to the new schema
        for (Map.Entry<String, SchemaCreationResult> entry : propertySchemaCreationResult.propertySchemas.entrySet()) {

            String propertyName = entry.getKey();
            SchemaCreationResult scr = entry.getValue();
            projectedSchema.addProperty(propertyName, scr.resultingSchema);

        }

        // Build the schema creation result
        // Create a new schema with only the projected fields
        SchemaCreationResult result = new SchemaCreationResult(projectedSchema,
                propertySchemaCreationResult.getNewNamedSchemas(),
                propertySchemaCreationResult.getNewAnonymousSchemas(),
                schemaProcessingCounts,
                propertySchemaCreationResult.isAnyPropertySchemaHasChanged() || propertySchemaCreationResult.isAnyPropertyBeenRemoved());

        result.newNamedSchemas.put(projectedSchemaName, projectedSchema);
        result.increaseSchemaProcessingCount(schema.getName());

        // Return a reference to the projected schema
        Schema<?> refSchema = new Schema<>();
        refSchema.set$ref("#/components/schemas/" + projectedSchemaName);

        result.resultingSchema = refSchema;

        return result;
    }

    @Override
    public boolean canProcess(StackData stackData) {
        return isObjectType(stackData.schema);
    }

    @Override
    @NonNull
    public SchemaCreationResult processStack(StackData stackData, List<StackProcessor> stackProcessors) {

        Schema<?> schema = stackData.schema;

        // If there are no properties, return the original schema
        if (schema.getProperties() == null || schema.getProperties().isEmpty()) {
            return new SchemaCreationResult(schema, stackData.schemaProcessingCounts, false);
        }

        var propertySchemaCreationResult = buildPropertySchemas(stackData, stackProcessors);
        boolean anyPropertySchemaHasChanged = propertySchemaCreationResult.isAnyPropertySchemaHasChanged();
        boolean propertiesHaveBeenRemoved = !propertySchemaCreationResult.removedProperties.isEmpty();

        if (!propertiesHaveBeenRemoved && !anyPropertySchemaHasChanged) {
            return new SchemaCreationResult(schema, stackData.schemaProcessingCounts, false);
        }

        return buildNewSchemaForTargetType(stackData,
                propertySchemaCreationResult,
                propertySchemaCreationResult.schemaProcessingCounts);

    }

    protected static class PropertySchemaCreationResult {
        @NonNull
        Map<String, SchemaCreationResult> propertySchemas;
        @NonNull
        Map<String, Integer> schemaProcessingCounts;
        @NonNull
        Set<String> removedProperties;

        public PropertySchemaCreationResult(@NonNull Map<String, SchemaCreationResult> propertySchemas,
                                            @NonNull Map<String, Integer> schemaProcessingCounts,
                                            @NonNull Set<String> removedProperties) {
            this.propertySchemas = propertySchemas;
            this.schemaProcessingCounts = schemaProcessingCounts;
            this.removedProperties = removedProperties;
        }

        public boolean isAnyPropertySchemaHasChanged() {
            return propertySchemas.values().stream().anyMatch(scr -> scr.schemaHasChanged);
        }

        public boolean isAnyPropertyBeenRemoved() {
            return !removedProperties.isEmpty();
        }

        public Map<String, Schema<?>> getNewNamedSchemas() {
            return propertySchemas.values().stream()
                    .flatMap(schemaCreationResult -> schemaCreationResult.newNamedSchemas.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        public Set<Schema<?>> getNewAnonymousSchemas() {
            return propertySchemas.values().stream()
                    .flatMap(schemaCreationResult -> schemaCreationResult.newAnonymousSchemas.stream())
                    .collect(Collectors.toSet());
        }

    }

}
