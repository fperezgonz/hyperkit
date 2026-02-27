package solutions.sulfura.hyperkit.utils.spring.openapi.schemabuilder.stackprocessors;

import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.utils.spring.openapi.ProjectedSchemaBuilder.StackData;

import java.util.Map;

import static solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils.isListOperationType;
import static solutions.sulfura.hyperkit.utils.spring.openapi.SchemaBuilderUtils.isObjectType;

public class ListOperationStackProcessor extends DefaultObjectStackProcessor {

    @Override
    protected String getNameForCurrentType(StackData stackData, Map<String, Integer> schemaProcessingCounts) {

        DtoProjection<?> projection = stackData.projection;

        if (projection == null || projection.projectionTypeAlias() == null) {
            return super.getNameForCurrentType(stackData, schemaProcessingCounts);
        }

        String nestedTypeAlias = projection.projectionTypeAlias();
        String schemaName = "ListOperation" + nestedTypeAlias;

        if (!stackData.currentNamespace.isBlank()) {
            schemaName = stackData.currentNamespace + "_" + schemaName;
        }

        if (schemaProcessingCounts.getOrDefault(schemaName, 0) > 0) {
            schemaName += "_" + schemaProcessingCounts.get(schemaName);
        }

        return schemaName;
    }

    @Override
    public boolean canProcess(StackData stackData) {
        return isObjectType(stackData.schema)
                && isListOperationType(stackData.schemaTargetType);
    }


}
