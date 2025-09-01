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

        ProjectedSchemaBuilder.SchemaCacheKey schemaCacheKey = ProjectedSchemaBuilder.SchemaCacheKey.valueOf(stackData);
        Schema<?> resultingSchema = stackData.schemaCache.get(schemaCacheKey);

        // If there is a matching schema in the cache, return the cached result
        if (resultingSchema != null) {
            boolean schemaHasChanged = !resultingSchema.equals(stackData.schema);
            return new SchemaCreationResult(resultingSchema, stackData.schemaProcessingCounts, schemaHasChanged);
        }

        for (StackProcessor stackProcessor : stackProcessors) {
            if (stackProcessor.canProcess(stackData)) {
                SchemaCreationResult result = stackProcessor.processStack(stackData, stackProcessors);
                stackData.schemaCache.put(schemaCacheKey, result.resultingSchema);
                return result;
            }
        }

        // If it was not processed and there is no matching schema in the cache, return the original schema (no schema was replicated)
        stackData.schemaCache.put(schemaCacheKey, stackData.schema);
        return new SchemaCreationResult(stackData.schema, stackData.schemaProcessingCounts, false);

    }

    public static SchemaCreationResult buildProjectedSchemas(
            OpenAPI openApi,
            Schema<?> schema,
            Type schemaTargetType,
            DtoProjection<?> projection,
            Class<? extends Dto> projectedClass,
            String rootNamespace,
            ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> rootProjectionAnnotationInfo,
            List<StackProcessor> stackProcessors,
            Map<SchemaCacheKey, Schema<?>> schemaCache) {

        ProjectedSchemaBuilder.StackData firstStack = new StackData(openApi,
                schema,
                schemaTargetType,
                projection,
                projectedClass,
                rootProjectionAnnotationInfo,
                rootNamespace,
                null,
                schemaCache);

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
         * True if any of the resulting schema is different from the original property schema
         */
        public boolean schemaHasChanged;
        /**
         * Stores the number of times a type has been processed within the current shema. Used in naming strategies to avoid collisions
         */
        public Map<String, Integer> schemaProcessingCounts = new HashMap<>();

        public SchemaCreationResult(Schema<?> resultingSchema,
                                    @NonNull Map<String, Schema<?>> newNamedSchemas,
                                    @NonNull Set<Schema<?>> newAnonymousSchemas,
                                    @NonNull Map<String, Integer> schemaProcessingCounts,
                                    boolean schemaHasChanged) {

            this.resultingSchema = resultingSchema;
            this.newNamedSchemas = newNamedSchemas;
            this.newAnonymousSchemas = newAnonymousSchemas;
            this.schemaProcessingCounts = schemaProcessingCounts;
            this.schemaHasChanged = schemaHasChanged;

        }

        public SchemaCreationResult(@NonNull Schema<?> resultingSchema,
                                    @NonNull Map<String, Integer> schemaProcessingCounts,
                                    boolean schemaHasChanged) {
            this(resultingSchema, new HashMap<>(), new HashSet<>(), schemaProcessingCounts, schemaHasChanged);
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
        public Map<SchemaCacheKey, Schema<?>> schemaCache = new HashMap<>();

        public StackData(@NonNull OpenAPI openApi,
                         @NonNull Schema<?> schema,
                         @NonNull Type schemaTargetType,
                         @NonNull DtoProjection<?> projection,
                         @NonNull Class<? extends Dto> projectedClass,
                         @NonNull AnnotationInfo<Annotation, DtoProjectionSpec> rootProjectionAnnotationInfo,
                         @NonNull String currentNamespace,
                         Map<String, Integer> schemaProcessingCounts,
                         @NonNull Map<SchemaCacheKey, Schema<?>> schemaCache) {

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

            this.schemaCache = schemaCache;

        }

        public void importSchemaProcessingCounts(StackData stackData) {
            this.schemaProcessingCounts.putAll(stackData.schemaProcessingCounts);
        }

    }

    public static class SchemaCacheKey {
        String namespace;
        Schema<?> schema;
        Type schemaTargetType;
        DtoProjection<?> projection;
        Class<? extends Dto> projectedClass;

        public SchemaCacheKey() {
        }

        public static SchemaCacheKey valueOf(StackData stackData) {
            SchemaCacheKey result = new SchemaCacheKey();
            result.namespace = stackData.currentNamespace;
            result.schema = stackData.schema;
            result.schemaTargetType = stackData.schemaTargetType;
            result.projection = stackData.projection;
            result.projectedClass = stackData.projectedClass;
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            SchemaCacheKey that = (SchemaCacheKey) o;
            return Objects.equals(namespace, that.namespace)
                    && Objects.equals(schema, that.schema)
                    && Objects.equals(schemaTargetType, that.schemaTargetType)
                    && Objects.equals(projection, that.projection)
                    && Objects.equals(projectedClass, that.projectedClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(namespace, schema, schemaTargetType, projection, projectedClass);
        }
    }

    public interface StackProcessor {

        boolean canProcess(StackData stackData);

        @NonNull
        SchemaCreationResult processStack(StackData stackData, List<ProjectedSchemaBuilder.StackProcessor> stackProcessors);

    }

}
