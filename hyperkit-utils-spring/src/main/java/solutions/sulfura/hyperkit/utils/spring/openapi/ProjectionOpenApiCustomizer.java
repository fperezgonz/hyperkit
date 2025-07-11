package solutions.sulfura.hyperkit.utils.spring.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionDsl;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils;
import solutions.sulfura.hyperkit.dsl.projections.ProjectionUtils.AnnotationInfo;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.SchemaCreationResult;

import static solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils.findDefaultProjectionClass;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Customizes the OpenAPI specification to apply projections to the generated models.
 * This customizer modifies the schema of request and response types based on the projections applied to them.
 */
@Component
public class ProjectionOpenApiCustomizer implements OpenApiCustomizer {

    private final Map<String, Schema<?>> projectedSchemas = new HashMap<>();
    private final Map<OperationMediaType, AnnotationInfo<Annotation, DtoProjectionSpec>> schemaProjections = new HashMap<>();
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final List<ProjectedSchemaBuilder.StackProcessor> stackProcessors;

    @Autowired
    public ProjectionOpenApiCustomizer(RequestMappingHandlerMapping requestMappingHandlerMapping, List<ProjectedSchemaBuilder.StackProcessor> stackProcessors1) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.stackProcessors = stackProcessors1;
    }

    @Override
    public void customise(OpenAPI openApi) {

        if (openApi.getPaths() == null) {
            return;
        }

        schemaProjections.clear();
        // First, collect all operations and their handler methods
        schemaProjections.putAll(collectOperationHandlerMethods(openApi));

        // Then, process each operation
        openApi.getPaths().forEach((path, pathItem) -> {
            if (pathItem.getGet() != null) {
                customizeOperation(openApi, path, pathItem, pathItem.getGet());
            }
            if (pathItem.getPost() != null) {
                customizeOperation(openApi, path, pathItem, pathItem.getPost());
            }
            if (pathItem.getPut() != null) {
                customizeOperation(openApi, path, pathItem, pathItem.getPut());
            }
            if (pathItem.getDelete() != null) {
                customizeOperation(openApi, path, pathItem, pathItem.getDelete());
            }
            if (pathItem.getPatch() != null) {
                customizeOperation(openApi, path, pathItem, pathItem.getPatch());
            }
        });

        // Add all projected schemas to the components section
        if (openApi.getComponents() == null) {
            return;
        }

        if (openApi.getComponents().getSchemas() == null) {
            openApi.getComponents().setSchemas(new HashMap<>());
        }

        openApi.getComponents().getSchemas().putAll(projectedSchemas);
    }

    /**
     * Collects all operations and their handler methods.
     * This is used to associate OpenAPI operations with Spring MVC handler methods.
     */
    private Map<OperationMediaType, AnnotationInfo<Annotation, DtoProjectionSpec>> collectOperationHandlerMethods(OpenAPI openApi) {

        Map<OperationMediaType, AnnotationInfo<Annotation, DtoProjectionSpec>> result = new HashMap<>();

        // Get all handler methods
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

        // For each path and operation in the OpenAPI spec
        openApi.getPaths().forEach((path, pathItem) -> {
            if (pathItem.getGet() != null) {
                HandlerMethod handlerMethod = findHandlerMethodForOperation("GET", path, handlerMethods);
                var records = getProjectionAnnotationInfoForHandlerMethodSchemas(pathItem.getGet(), handlerMethod);
                result.putAll(records);
            }
            if (pathItem.getPost() != null) {
                HandlerMethod handlerMethod = findHandlerMethodForOperation("POST", path, handlerMethods);
                var records = getProjectionAnnotationInfoForHandlerMethodSchemas(pathItem.getGet(), handlerMethod);
                result.putAll(records);
            }
            if (pathItem.getPut() != null) {
                HandlerMethod handlerMethod = findHandlerMethodForOperation("PUT", path, handlerMethods);
                var records = getProjectionAnnotationInfoForHandlerMethodSchemas(pathItem.getGet(), handlerMethod);
                result.putAll(records);
            }
            if (pathItem.getDelete() != null) {
                HandlerMethod handlerMethod = findHandlerMethodForOperation("DELETE", path, handlerMethods);
                var records = getProjectionAnnotationInfoForHandlerMethodSchemas(pathItem.getGet(), handlerMethod);
                result.putAll(records);
            }
            if (pathItem.getPatch() != null) {
                HandlerMethod handlerMethod = findHandlerMethodForOperation("PATCH", path, handlerMethods);
                var records = getProjectionAnnotationInfoForHandlerMethodSchemas(pathItem.getGet(), handlerMethod);
                result.putAll(records);
            }
        });

        return result;

    }

    protected Map<OperationMediaType, AnnotationInfo<Annotation, DtoProjectionSpec>> getProjectionAnnotationInfoForHandlerMethodSchemas(Operation operation, HandlerMethod handlerMethod) {

        // Check if the handler method has a DtoProjectionSpec annotation or meta-annotation
        AnnotationInfo<Annotation, DtoProjectionSpec> returnTypeAnnotationInfo = ProjectionUtils.getReturnTypeAnnotationInfo(handlerMethod.getMethod(), DtoProjectionSpec.class);

        Map<OperationMediaType, AnnotationInfo<Annotation, DtoProjectionSpec>> result = new HashMap<>();

        if (returnTypeAnnotationInfo != null && operation.getResponses() != null) {

            operation.getResponses().values().stream()
                    .filter(response -> response.getContent() != null)
                    .flatMap(response -> response.getContent().values().stream())
                    .filter(mediaTypeObject -> mediaTypeObject.getSchema() != null)
                    .forEach(mediaTypeObject -> result.put(new OperationMediaType(operation, mediaTypeObject), returnTypeAnnotationInfo));

        }

        if (operation.getRequestBody() == null || operation.getRequestBody().getContent() == null) {
            return result;
        }

        // Check if any of the parameters has a DtoProjectionSpec annotation
        for (int i = 0; i < handlerMethod.getMethodParameters().length; i++) {

            MethodParameter methodParameter = handlerMethod.getMethodParameters()[i];
            AnnotationInfo<Annotation, DtoProjectionSpec> paramAnnotationInfo = ProjectionUtils.getAnnotationInfo(methodParameter.getParameter(), DtoProjectionSpec.class);

            if (paramAnnotationInfo == null) {
                continue;
            }

            operation.getRequestBody().getContent().values().stream()
                    .filter(mediaTypeObject -> mediaTypeObject.getSchema() != null)
                    .forEach(mediaTypeObject -> result.put(new OperationMediaType(operation, mediaTypeObject), paramAnnotationInfo));

        }

        return result;

    }

    private boolean isHandlerMethod(String httpMethod,
                                    String path,
                                    RequestMappingInfo mappingInfo) {

        // Check if the handler method matches the operation
        return mappingInfo.getMethodsCondition().getMethods().stream()
                .anyMatch(method -> method.name().equals(httpMethod))
                && (mappingInfo.getPatternsCondition() != null
                && mappingInfo.getPatternsCondition().getPatterns().stream()
                .anyMatch(pattern -> pattern.equals(path)))
                || (mappingInfo.getPathPatternsCondition() != null
                && mappingInfo.getPathPatternsCondition().getPatterns().stream()
                .anyMatch(pattern -> pattern.toString().equals(path)));

    }

    /**
     * Finds the handler method for the given operation.
     */
    private HandlerMethod findHandlerMethodForOperation(String httpMethod,
                                                        String path,
                                                        Map<RequestMappingInfo, HandlerMethod> handlerMethods) {

        HandlerMethod handlerMethod = null;

        // Search for the handler method
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {

            RequestMappingInfo mappingInfo = entry.getKey();
            HandlerMethod candidateHandlerMethod = entry.getValue();

            if (isHandlerMethod(httpMethod, path, mappingInfo)) {
                handlerMethod = candidateHandlerMethod;
                break;
            }

        }

        return handlerMethod;

    }

    private void customizeOperation(OpenAPI openApi, String path, PathItem pathItem, Operation operation) {
        // Customize request body
        if (operation.getRequestBody() != null) {
            customizeRequestBody(openApi, path, pathItem, operation, operation.getRequestBody());
        }

        // Customize responses
        if (operation.getResponses() != null) {
            operation.getResponses().forEach((statusCode, response) ->
                    customizeResponse(openApi, path, pathItem, operation, response)
            );
        }
    }

    private void customizeRequestBody(OpenAPI openApi, String path, PathItem pathItem, Operation operation, RequestBody requestBody) {
        if (requestBody.getContent() == null) {
            return;
        }

        customizeContent(openApi, path, pathItem, operation, requestBody.getContent());
    }

    private void customizeResponse(OpenAPI openApi, String path, PathItem pathItem, Operation operation, ApiResponse response) {
        if (response.getContent() == null) {
            return;
        }

        customizeContent(openApi, path, pathItem, operation, response.getContent());
    }

    private void customizeContent(OpenAPI openApi, String path, PathItem pathItem, Operation operation, Content content) {
        content.forEach((mediaType, mediaTypeObject) ->
                customizeMediaType(openApi, path, pathItem, operation, mediaTypeObject)
        );
    }

    private void customizeMediaType(OpenAPI openApi, String path, PathItem pathItem, Operation operation, MediaType mediaTypeObject) {

        if (mediaTypeObject.getSchema() == null) {
            return;
        }

        Schema<?> schema = mediaTypeObject.getSchema();

        // Check if this schema has a projection annotation
        AnnotationInfo<Annotation, DtoProjectionSpec> projectionAnnotationInfo = getProjectionSpecForSchema(new OperationMediaType(operation, mediaTypeObject));

        if (projectionAnnotationInfo == null) {
            return;
        }

        if (schema.get$ref() != null) {
            String referencedSchemaName = schema.get$ref().substring(schema.get$ref().lastIndexOf('/') + 1);
            schema = openApi.getComponents().getSchemas().get(referencedSchemaName);
        }

        DtoProjectionSpec projectionSpec = projectionAnnotationInfo.targetAnnotation;
        Class<? extends DtoProjection> projectionClass = findDefaultProjectionClass(projectionSpec.projectedClass());
        DtoProjection<?> projection = ProjectionDsl.parse(projectionSpec.value(), projectionClass);
        SchemaCreationResult schemaCreationResult = ProjectedSchemaBuilder.buildProjectedSchemas(openApi,
                schema,
                projectionAnnotationInfo.annotatedType.getType(),
                projection,
                projectionSpec.projectedClass(),
                projectionAnnotationInfo,
                stackProcessors);
        projectedSchemas.putAll(schemaCreationResult.newNamedSchemas);
        mediaTypeObject.setSchema(schemaCreationResult.resultingSchema);

    }

    private AnnotationInfo<Annotation, DtoProjectionSpec> getProjectionSpecForSchema(OperationMediaType mediaTypeObject) {
        return schemaProjections.get(mediaTypeObject);
    }

    public static class OperationMediaType {
        private final Operation operation;
        private final MediaType mediaType;

        OperationMediaType(Operation operation, MediaType mediaType) {
            this.operation = operation;
            this.mediaType = mediaType;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            OperationMediaType that = (OperationMediaType) o;
            return Objects.equals(operation, that.operation) && Objects.equals(mediaType, that.mediaType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(operation, mediaType);
        }
    }

}
