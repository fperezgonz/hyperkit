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

    public ParameterizedType buildPropertyType(@NonNull ParameterizedType projectionHolderType, @NonNull ParameterizedType parameterizedPropertyType) {

        Type[] typeArgs = projectionHolderType.getActualTypeArguments();

        if (typeArgs.length == 0) {
            return parameterizedPropertyType;
        }

        Type[] actualTypeArguments = Arrays.copyOf(typeArgs, typeArgs.length);

        for (int i = 0; i < actualTypeArguments.length; i++) {

            if (!(actualTypeArguments[i] instanceof TypeVariable)) {
                continue;
            }

            for (Type type : typeArgs) {

                if (actualTypeArguments[i].getTypeName().equals(type.getTypeName())) {
                    actualTypeArguments[i] = type;
                    break;
                }

            }

        }

        return new SchemaBuilderUtils.ParameterizedTypeImpl(actualTypeArguments, parameterizedPropertyType);

    }

    /**
     * Rebuilds each property schema in the target type schema using the stack info
     */
    @NonNull
    protected PropertySchemaCreationResult buildPropertySchemas(StackData stackData, List<StackProcessor> stackProcessors) {

        Schema<?> schema = stackData.schema;
        Type schemaTargetType = stackData.schemaTargetType;
        BeanDescription beanDescription = Json.mapper().getSerializationConfig().introspect(Json.mapper().constructType(schemaTargetType));
        Map<String, BeanPropertyDefinition> beanProperties = beanDescription.findProperties().stream()
                .collect(Collectors.toMap(BeanPropertyDefinition::getName, propDef -> propDef));
        // New nested namespace for properties
        String newNamespace = getNameForCurrentType(stackData, stackData.schemaProcessingCounts);
        Map<String, Integer> schemaProcessingCounts = new HashMap<>();

        HashMap<String, SchemaCreationResult> schemaCreationResults = new HashMap<>();

        schema.getProperties().forEach((propertyName, oldFieldSchema) -> {

                    if (!beanProperties.containsKey(propertyName)) {
                        return;
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

                    if (propertyType instanceof ParameterizedType parameterizedPropertyType
                            && schemaTargetType instanceof ParameterizedType projectionHolderType) {
                        propertyType = buildPropertyType(projectionHolderType, parameterizedPropertyType);
                    } else if (propertyType instanceof TypeVariable<?> typeVariable) {
                        propertyType = SchemaBuilderUtils.resolveTypeVariableForField(schemaTargetType, propertyPrimaryMember, typeVariable);
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
                            newNamespace,
                            schemaProcessingCounts);

                    var schemaCreationResult = buildSchemaForStack(fieldStackData, stackProcessors);
                    schemaCreationResults.put(propertyName, schemaCreationResult);
                    schemaProcessingCounts.putAll(schemaCreationResult.schemaProcessingCounts);
                }

        );

        // Use stackData.schemaProcessingCounts to pop the namespace
        return new PropertySchemaCreationResult(schemaCreationResults, stackData.schemaProcessingCounts);

    }

    protected SchemaCreationResult buildNewSchemaForTargetType(StackData stackData,
                                                               Map<String, Schema<?>> propertiesSchemas,
                                                               Map<String, Schema<?>> newNamedSchemas,
                                                               Set<Schema<?>> newAnonymousSchemas,
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
        propertiesSchemas.forEach(projectedSchema::addProperty);

        // Build the schema creation result
        // Create a new schema with only the projected fields
        SchemaCreationResult result = new SchemaCreationResult(projectedSchema, schemaProcessingCounts);
        result.newNamedSchemas.putAll(newNamedSchemas);
        result.newAnonymousSchemas.addAll(newAnonymousSchemas);
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

        PropertySchemaCreationResult propertySchemaCreationResult = buildPropertySchemas(stackData, stackProcessors);
        var schemaCreationResults = propertySchemaCreationResult.propertySchemas;

        // Collect new schemas

        Map<String, Schema<?>> newNamedSchemas = schemaCreationResults.values().stream()
                .flatMap(schemaCreationResult -> schemaCreationResult.newNamedSchemas.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Set<Schema<?>> newAnonymousSchemas = schemaCreationResults.values().stream()
                .flatMap(schemaCreationResult -> schemaCreationResult.newAnonymousSchemas.stream())
                .collect(Collectors.toSet());

        boolean newSchemasCreated = !newNamedSchemas.isEmpty() || !newAnonymousSchemas.isEmpty();
        boolean propertiesRemoved = stackData.schema.getProperties().size() != schemaCreationResults.size();

        if (!newSchemasCreated && !propertiesRemoved) {
            return new SchemaCreationResult(stackData.schema, stackData.schemaProcessingCounts);
        }

        Map<String, Schema<?>> propertiesSchemas = schemaCreationResults.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().resultingSchema));

        return buildNewSchemaForTargetType(stackData, propertiesSchemas, newNamedSchemas, newAnonymousSchemas, propertySchemaCreationResult.schemaProcessingCounts);

    }

    protected static class PropertySchemaCreationResult {
        @NonNull
        Map<String, SchemaCreationResult> propertySchemas;
        @NonNull
        Map<String, Integer> schemaProcessingCounts;

        public PropertySchemaCreationResult(@NonNull Map<String, SchemaCreationResult> propertySchemas, @NonNull Map<String, Integer> schemaProcessingCounts) {
            this.propertySchemas = propertySchemas;
            this.schemaProcessingCounts = schemaProcessingCounts;
        }
    }

}
