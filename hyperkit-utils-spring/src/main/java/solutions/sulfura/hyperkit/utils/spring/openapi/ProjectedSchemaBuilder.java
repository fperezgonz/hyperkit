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
        return new SchemaCreationResult(stackData.schema);

    }

    static SchemaCreationResult buildProjectedSchemas(
            OpenAPI openApi,
            Schema<?> schema,
            Type schemaTargetType,
            DtoProjection<?> projection,
            Class<? extends Dto> projectedClass,
            ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> rootProjectionAnnotationInfo,
            List<StackProcessor> stackProcessors) {

        ProjectedSchemaBuilder.StackData firstStack = new ProjectedSchemaBuilder.StackData(openApi,
                schema,
                schemaTargetType,
                projection,
                projectedClass,
                rootProjectionAnnotationInfo);

        return buildSchemaForStack(firstStack, stackProcessors);

    }

    public static class SchemaCreationResult {
        public Schema<?> resultingSchema;
        /**
         * These are necessary schemas built to support the resulting schema
         */
        public Map<String, Schema<?>> newNamedSchemas;
        public Set<Schema<?>> newAnonymousSchemas;

        public SchemaCreationResult(Schema<?> resultingSchema, Map<String, Schema<?>> newNamedSchemas, Set<Schema<?>> newAnonymousSchemas) {
            this.resultingSchema = resultingSchema;
            this.newNamedSchemas = newNamedSchemas;
            this.newAnonymousSchemas = newAnonymousSchemas;
        }

        public SchemaCreationResult(Schema<?> resultingSchema) {
            this(resultingSchema, new HashMap<>(), new HashSet<>() );
        }

        public boolean hasNewSchemas() {
            return !newNamedSchemas.isEmpty() || !newAnonymousSchemas.isEmpty();
        }

    }

    public static class StackData {
        public OpenAPI openApi;
        public Schema<?> schema;
        public Type schemaTargetType;
        public DtoProjection<?> projection;
        public Class<? extends Dto> projectedClass;
        public ProjectionUtils.AnnotationInfo<Annotation, DtoProjectionSpec> rootProjectionAnnotationInfo;

        public StackData(@NonNull OpenAPI openApi,
                         @NonNull Schema<?> schema,
                         @NonNull Type schemaTargetType,
                         @NonNull DtoProjection<?> projection,
                         @NonNull Class<? extends Dto> projectedClass,
                         @NonNull AnnotationInfo<Annotation, DtoProjectionSpec> rootProjectionAnnotationInfo) {
            this.openApi = openApi;
            this.schema = schema;
            this.schemaTargetType = schemaTargetType;
            this.projection = projection;
            this.projectedClass = projectedClass;
            this.rootProjectionAnnotationInfo = rootProjectionAnnotationInfo;
        }
    }

    public interface StackProcessor {

        boolean canProcess(StackData stackData);

        @NonNull
        SchemaCreationResult processStack(StackData stackData, List<ProjectedSchemaBuilder.StackProcessor> stackProcessors);

    }

}
