package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils.AnnotationInfo;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;


public class ProjectedSchemaBuilder {

    @NonNull
    public static SchemaCreationResult buildSchemaForStack(StackData stackData, List<StackProcessor> stackProcessors) {

        for (StackProcessor stackProcessor : stackProcessors) {
            if (stackProcessor.canProcess(stackData)) {
                return stackProcessor.processStack(stackData, stackProcessors);
            }
        }

        // If it was not processed, return the original schema (no schema was replicated)
        return new SchemaCreationResult(stackData.schema, stackData.schemaProcessingCounts);

    }

    public static SchemaCreationResult buildProjectedSchemas(
            OpenAPI openApi,
            Schema<?> schema,
            Type schemaTargetType,
            DtoProjection<?> projection,
            Class<? extends Dto> projectedClass,
            String rootNamespace,
            ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> rootProjectionAnnotationInfo,
            List<StackProcessor> stackProcessors) {

        ProjectedSchemaBuilder.StackData firstStack = new StackData(openApi,
                schema,
                schemaTargetType,
                projection,
                projectedClass,
                rootProjectionAnnotationInfo,
                rootNamespace,
                null);

        return buildSchemaForStack(firstStack, stackProcessors);

    }

    public static class SchemaCreationResult {

        public Schema<?> resultingSchema;
        /**
         * These are necessary schemas built to support the resulting schema
         */
        public Map<String, Schema<?>> newNamedSchemas;
        /**
         * These are necessary schemas built to support the resulting schema
         */
        public Set<Schema<?>> newAnonymousSchemas;
        /**
         * Stores the number of times a type has been processed within the current shema. Used in naming strategies to avoid collisions
         */
        public Map<String, Integer> schemaProcessingCounts = new HashMap<>();

        public SchemaCreationResult(Schema<?> resultingSchema, @NonNull Map<String, Schema<?>> newNamedSchemas, @NonNull Set<Schema<?>> newAnonymousSchemas, @NonNull Map<String, Integer> schemaProcessingCounts) {
            this.resultingSchema = resultingSchema;
            this.newNamedSchemas = newNamedSchemas;
            this.newAnonymousSchemas = newAnonymousSchemas;
            this.schemaProcessingCounts = schemaProcessingCounts;
        }

        public SchemaCreationResult(@NonNull Schema<?> resultingSchema, @NonNull Map<String, Integer> schemaProcessingCounts) {
            this(resultingSchema, new HashMap<>(), new HashSet<>(), schemaProcessingCounts);
        }

        public void importSchemaProcessingCounts(StackData stackData) {
            this.schemaProcessingCounts.putAll(stackData.schemaProcessingCounts);
        }

        public int increaseSchemaProcessingCount(String schemaName) {

            if (schemaName != null && !schemaName.isBlank()) {

                int count = schemaProcessingCounts.getOrDefault(schemaName, 0);
                schemaProcessingCounts.put(schemaName, ++count);

                return count;

            }

            return 0;

        }

        public boolean hasNewSchemas() {
            return !newNamedSchemas.isEmpty() || !newAnonymousSchemas.isEmpty();
        }

    }

    public static class StackData {
        @NonNull
        public OpenAPI openApi;
        @NonNull
        public Schema<?> schema;
        @NonNull
        public Type schemaTargetType;
        @NonNull
        public DtoProjection<?> projection;
        @NonNull
        public Class<? extends Dto> projectedClass;
        @NonNull
        public String currentNamespace = "";
        @NonNull
        public AnnotationInfo<Annotation, DtoProjectionSpec> rootProjectionAnnotationInfo;
        /**
         * Stores the number of times a type has been processed within the current shema. Used in naming strategies to avoid collisions
         */
        public Map<String, Integer> schemaProcessingCounts = new HashMap<>();

        public StackData(@NonNull OpenAPI openApi,
                         @NonNull Schema<?> schema,
                         @NonNull Type schemaTargetType,
                         @NonNull DtoProjection<?> projection,
                         @NonNull Class<? extends Dto> projectedClass,
                         @NonNull AnnotationInfo<Annotation, DtoProjectionSpec> rootProjectionAnnotationInfo,
                         @NonNull String currentNamespace,
                         Map<String, Integer> schemaProcessingCounts) {
            this.openApi = openApi;
            this.schema = schema;
            this.schemaTargetType = schemaTargetType;
            this.projection = projection;
            this.projectedClass = projectedClass;
            this.rootProjectionAnnotationInfo = rootProjectionAnnotationInfo;
            this.currentNamespace = currentNamespace;
            if (schemaProcessingCounts != null) {
                this.schemaProcessingCounts.putAll(schemaProcessingCounts);
            }
        }

        public void importSchemaProcessingCounts(StackData stackData) {
            this.schemaProcessingCounts.putAll(stackData.schemaProcessingCounts);
        }

    }

    public interface StackProcessor {

        boolean canProcess(StackData stackData);

        @NonNull
        SchemaCreationResult processStack(StackData stackData, List<ProjectedSchemaBuilder.StackProcessor> stackProcessors);

    }

}
