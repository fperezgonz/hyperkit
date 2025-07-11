package solutions.sulfura.hyperkit.utils.spring.openapi.schemabuilder.stackprocessors;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.jspecify.annotations.NonNull;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.SchemaCreationResult;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackProcessor;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackData;

import java.util.List;

import static solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.buildSchemaForStack;
import static solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils.findReferencedModel;

public class SchemaReferenceStackProcessor implements StackProcessor {
    @Override
    public boolean canProcess(StackData stackData) {

        return stackData.schema.get$ref() != null;
    }

    @Override
    @NonNull
    public SchemaCreationResult processStack(StackData stackData, List<StackProcessor> stackProcessors) {

        OpenAPI openAPI = stackData.openApi;
        Schema<?> schema = stackData.schema;
        Schema<?> referencedSchema = findReferencedModel(openAPI, schema);
        StackData referencedStackData = new StackData(stackData.openApi, referencedSchema, stackData.schemaTargetType, stackData.projection, stackData.projectedClass, stackData.rootProjectionAnnotationInfo);
        return buildSchemaForStack(referencedStackData, stackProcessors);

    }
}
