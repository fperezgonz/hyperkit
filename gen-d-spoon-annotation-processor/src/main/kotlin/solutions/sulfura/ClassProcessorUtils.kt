package solutions.sulfura

import io.vavr.control.Option
import solutions.sulfura.gend.dtos.ListOperation
import solutions.sulfura.gend.dtos.annotations.Dto
import solutions.sulfura.gend.dtos.annotations.DtoFor
import solutions.sulfura.gend.dtos.projection.DtoProjection
import solutions.sulfura.gend.dtos.projection.DtoProjectionException
import solutions.sulfura.gend.dtos.projection.ProjectionFor
import solutions.sulfura.gend.dtos.projection.ProjectionUtils
import solutions.sulfura.gend.dtos.projection.fields.FieldConf
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf
import spoon.SpoonAPI
import spoon.reflect.CtModel
import spoon.reflect.code.*
import spoon.reflect.declaration.*
import spoon.reflect.factory.Factory
import spoon.reflect.reference.CtActualTypeContainer
import spoon.reflect.reference.CtFieldReference
import spoon.reflect.reference.CtTypeParameterReference
import spoon.reflect.reference.CtTypeReference
import spoon.reflect.reference.CtVariableReference
import spoon.reflect.visitor.chain.CtQuery
import java.util.Locale

fun capitalize(s: String): String {
    return s.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun uncapitalize(s: String): String {
    return s.replaceFirstChar { it.lowercase(Locale.getDefault()) }
}

fun collectClasses(model: CtModel, factory: Factory): CtQuery {

    val dtoAnnotationCtType = factory.Type().get<Annotation>(Dto::class.java).reference

    val dtoAnnotatedClassesQuery = model.filterChildren { el: CtElement ->
        el is CtClass<*> && el.getAnnotation(dtoAnnotationCtType) != null
    }

    return dtoAnnotatedClassesQuery

}

fun buildTypeReferenceWithInferredParameters(
    elementType: CtTypeReference<*>,
    typeParamMap: Map<String, CtTypeReference<*>>,
    factory: Factory
): CtTypeReference<*> {

    var result: CtTypeReference<*> = elementType

    if (typeParamMap.isEmpty()) {
        return result
    }

    if (elementType is CtTypeParameterReference) {
        //Replace generic type with the actual type
        result = typeParamMap[elementType.qualifiedName] ?: elementType
    }

    if (elementType.isParameterized) {

        val propertyType = elementType.typeDeclaration
        result = factory.Type().createReference(propertyType, true)

        //Replace parameters with the actual types
        for (i in 0..elementType.actualTypeArguments.size - 1) {
            val sourceFieldTypeArg = elementType.actualTypeArguments[i]
            val mappedTypeArg = typeParamMap[sourceFieldTypeArg.qualifiedName]
            result.actualTypeArguments[i] = mappedTypeArg ?: sourceFieldTypeArg
        }

    }

    return result

}

fun collectProperties(typeReference: CtTypeReference<*>, factory: Factory): List<PropertyData> {

    val result = mutableListOf<PropertyData>()

    val dtoAnnotation = typeReference.getAnnotation<Dto>(Dto::class.java)

    val includedAnnotations =
        if (dtoAnnotation == null) {
            mutableSetOf<Any>()
        } else {
            dtoAnnotation.include
                .map { it.java.canonicalName }
                .toSet()
        }

    val typeParamMap = mutableMapOf<String, CtTypeReference<*>>()

    //Map type parameters to actual types
    if (typeReference.isParameterized && typeReference.actualTypeArguments != null) {

        for (i in 0..typeReference.typeDeclaration.formalCtTypeParameters.size - 1) {
            val ctTypeParam = typeReference.typeDeclaration.formalCtTypeParameters[i]
            val actualTypeArgument = typeReference.actualTypeArguments[i]
            typeParamMap[ctTypeParam.qualifiedName] = actualTypeArgument
        }

    }

    typeReference.declaredFields.forEach filter@{ el: CtElement ->

        //If it does not contain any of the annotations used to mark inclusion
        if (!includedAnnotations.isEmpty() && !el.annotations.any { annotation -> annotation.type.qualifiedName in includedAnnotations }) {
            return@filter
        }

        //Is not a public field
        if (el !is CtFieldReference<*> || !el.modifiers.contains(ModifierKind.PUBLIC)) {
            return@filter
        }

        val typeWithInferredParameters = buildTypeReferenceWithInferredParameters(el.type, typeParamMap, factory)
        result.add(PropertyData(el.simpleName, typeWithInferredParameters))

    }

    typeReference.typeDeclaration.directChildren.forEach filter@{ el: CtElement ->

        //If it does not contain any of the annotations used to mark inclusion
        if (!includedAnnotations.isEmpty() && !el.annotations.any { annotation -> annotation.type.qualifiedName in includedAnnotations }) {
            return@filter
        }

        //Is not a public method
        if (el !is CtMethod<*> || !el.modifiers.contains(ModifierKind.PUBLIC)) {
            return@filter
        }

        val methodName = el.simpleName

        //Is a getter
        if (methodName.startsWith("get") && el.type != factory.Type().voidPrimitiveType()) {

            val typeWithInferredParameters = buildTypeReferenceWithInferredParameters(el.type, typeParamMap, factory)
            result.add(PropertyData(uncapitalize(methodName.substring(3)), typeWithInferredParameters))

        }

        //Is a getter
        if (methodName.startsWith("is") && el.type != factory.Type().voidPrimitiveType()) {

            val typeWithInferredParameters = buildTypeReferenceWithInferredParameters(el.type, typeParamMap, factory)
            result.add(PropertyData(uncapitalize(methodName.substring(2)), typeWithInferredParameters))

        }

        //Is a setter
        if (methodName.startsWith("set") && el.type == factory.Type().voidPrimitiveType() && el.parameters.size == 1) {

            val setterParamType = el.parameters.first().type
            val typeWithInferredParameters =
                buildTypeReferenceWithInferredParameters(setterParamType, typeParamMap, factory)
            result.add(PropertyData(uncapitalize(methodName.substring(3)), typeWithInferredParameters))

        }

        return@filter

    }

    if (typeReference.superclass != null) {
        result.addAll(collectProperties(typeReference.superclass, factory))
    }

    return return result

}

private fun createAnnotation_DtoFor(ctClass: CtClass<*>, spoonApi: SpoonAPI): CtAnnotation<DtoFor> {

    val dtoAnnotationCtType = spoonApi.factory.Annotation().get<DtoFor>(DtoFor::class.java)
    val dtoAnnotation = spoonApi.factory.createAnnotation(dtoAnnotationCtType.reference)
    val ctClassAccess = spoonApi.factory.createClassAccess(ctClass.reference)
    dtoAnnotation.addValue<CtAnnotation<DtoFor>>("value", ctClassAccess)
    return dtoAnnotation
}

private fun createAnnotation_ProjectionFor(
    ctClass: CtClass<*>,
    ctSourceClass: CtClass<*>,
    spoonApi: SpoonAPI
): CtAnnotation<ProjectionFor> {

    val dtoAnnotationCtType = spoonApi.factory.Annotation().get<ProjectionFor>(ProjectionFor::class.java)
    val dtoAnnotation = spoonApi.factory.createAnnotation(dtoAnnotationCtType.reference)
    val ctSourceClassAccess = spoonApi.factory.createClassAccess(ctClass.reference)
    dtoAnnotation.addValue<CtAnnotation<ProjectionFor>>("value", ctSourceClassAccess)
    return dtoAnnotation

}

fun collectAnnotations(ctClass: CtClass<*>, spoonApi: SpoonAPI): List<CtAnnotation<*>> {

    val result = mutableListOf<CtAnnotation<*>>()
    result.add(createAnnotation_DtoFor(ctClass, spoonApi))
    return result

}

fun buildBuilderClass(dtoClass: CtClass<*>, spoonApi: SpoonAPI): CtClass<*>? {

    val result = spoonApi.factory.createClass(dtoClass, "Builder")
    result.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    result.addModifier<CtModifiable>(ModifierKind.STATIC)
    return result;

}

fun isInnerType(ctType: CtTypeReference<*>): CtTypeReference<*>? {

    if (ctType.qualifiedName.contains("$")) {
        return ctType;
    }

    var possibleBinaryName = ctType.qualifiedName
    var lastDotIdx = possibleBinaryName.lastIndexOf('.')

    while (lastDotIdx != -1) {

        possibleBinaryName =
            possibleBinaryName.substring(0, lastDotIdx) + "$" + possibleBinaryName.substring(lastDotIdx + 1)
        val innerType = ctType.factory.Type().createReference<Any>(possibleBinaryName)

        if (innerType != null) {
            return innerType
        }

        lastDotIdx = possibleBinaryName.lastIndexOf('.')

    }

    return null

}

fun implements(vararg typesToTest: CtTypeReference<*>, typeToImplement: String): Boolean {

    for (t in typesToTest) {

        var c = t;

        //If the type declaration cannot be found, the type is likely to be an inner type
        if (c.typeDeclaration == null) {
            c = isInnerType(c) ?: c;
        }


        if (c.qualifiedName == typeToImplement) {
            return true;
        }

        if (c.superclass != null && implements(c.superclass, typeToImplement = typeToImplement)) {
            return true
        }

        if (c.superInterfaces == null || c.superInterfaces.isEmpty()) {
            continue
        }

        for (interf in c.superInterfaces) {
            if (implements(interf, typeToImplement = typeToImplement)) {
                return true;
            }
        }

    }

    return false

}

fun implementsList(vararg typesToTest: CtTypeReference<*>): Boolean {
    return implements(typesToTest = typesToTest, typeToImplement = "java.util.List")
}

fun implementsSet(vararg typesToTest: CtTypeReference<*>): Boolean {
    return implements(typesToTest = typesToTest, typeToImplement = "java.util.Set")
}

fun buildProjectionClass(dtoClass: CtClass<*>, sourceClass: CtClass<*>, spoonApi: SpoonAPI): CtClass<*>? {

    val result = spoonApi.factory.createClass(dtoClass, "Projection")
    val fieldConf = spoonApi.factory.Class().createReference(FieldConf::class.java)
    val listFieldConf = spoonApi.factory.Class().createReference(ListFieldConf::class.java)

    result.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    result.addModifier<CtModifiable>(ModifierKind.STATIC)

    result.addAnnotation<CtAnnotation<ProjectionFor>>(createAnnotation_ProjectionFor(dtoClass, sourceClass, spoonApi))
    //Make it extend the Projection superclass
    val projectionSuperclass = spoonApi.factory.Class().createReference<DtoProjection<*>>(DtoProjection::class.java)
    projectionSuperclass.addActualTypeArgument<CtActualTypeContainer>(dtoClass.reference)
    result.setSuperclass<CtType<*>>(projectionSuperclass)

    //Build Projection fields
    dtoClass.fields.forEach { ctField ->

        val optionalGenericArgType = ctField.type.actualTypeArguments.first()


        val isCollection = optionalGenericArgType.qualifiedName == "java.util.List"
                || optionalGenericArgType.qualifiedName == "java.util.Set"
                || optionalGenericArgType.isArray

        val fieldType: CtTypeReference<*> = if (isCollection) listFieldConf else fieldConf

        result.addField<Any, CtType<*>>(
            spoonApi.factory.createField(
                result,
                setOf(ModifierKind.PUBLIC),
                fieldType,
                ctField.simpleName
            )
        )

    }

    //Add empty constructor
    spoonApi.factory.createConstructor(result, mutableSetOf(ModifierKind.PUBLIC), null, null)
        .setBody<CtBodyHolder>(spoonApi.factory.createCtBlock<CtStatement>(null))

    //Method "public void applyProjectionTo(SourceClassTypesDto dto) throws DtoProjectionException"
    val method_applyProjectionTo = spoonApi.factory.createMethod<Any>()
    method_applyProjectionTo.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    method_applyProjectionTo.setSimpleName<CtMethod<*>>("applyProjectionTo")
    method_applyProjectionTo.setType<CtMethod<*>>(spoonApi.factory.Type().voidPrimitiveType())

    val parameter = spoonApi.factory.createParameter<Any>()
    parameter.setSimpleName<CtNamedElement>("dto")
    parameter.setType<CtTypedElement<*>>(dtoClass.reference)
    method_applyProjectionTo.addParameter<CtExecutable<Any>>(parameter)

    val exception = spoonApi.factory.Type().get<DtoProjectionException>(DtoProjectionException::class.java).reference
    method_applyProjectionTo.addThrownType<CtExecutable<Any>>(exception)

    val methodBody: CtBlock<Int?> = spoonApi.factory.Core().createBlock()

    //Add statements "dto.field = ProjectionUtils.getProjectedValue(dto.field, this.field);" to method body
    result.fields.forEach { ctField ->
        @Suppress("UNCHECKED_CAST")
        ctField as CtField<Option<Any>>

        val projectionUtilsCtClass = spoonApi.factory.Class().createReference(ProjectionUtils::class.java)

        @Suppress("UNCHECKED_CAST")
        val optionCtClass = spoonApi.factory.Class().createReference(Option::class.java) as CtTypeReference<Option<Any>>
        val getProjectedValueMethod = spoonApi.factory.Method().createReference(
            projectionUtilsCtClass,
            true,
            optionCtClass,
            "getProjectedValue",
            mutableListOf<CtTypeReference<*>>(optionCtClass, ctField.type)
        )

        //Field read: "this.field" in "ProjectionUtils.getProjectedValue(dto.field, this.field)"
        val projectionFieldRead = spoonApi.factory.createFieldRead<Option<Any>>()
        val thisAccess = spoonApi.factory.createThisAccess<Any>(result.reference)
        projectionFieldRead.setTarget<CtTargetedExpression<Option<Any>, CtExpression<*>>>(thisAccess)
        projectionFieldRead.setVariable<CtVariableAccess<Option<Any>>>(ctField.reference)

        //Parameter field read: "dto.field" in "ProjectionUtils.getProjectedValue(dto.field, this.field)"
        @Suppress("UNCHECKED_CAST")
        val parameterField =
            parameter.type.getDeclaredOrInheritedField(ctField.simpleName) as CtVariableReference<Option<Any>>
        val parameterAccess = spoonApi.factory.Code().createVariableRead(parameter.reference, false)
        val parameterFieldRead = spoonApi.factory.createFieldRead<Option<Any>>()
        parameterFieldRead.setTarget<CtTargetedExpression<Option<Any>, CtExpression<*>>>(parameterAccess)
        parameterFieldRead.setVariable<CtVariableAccess<Option<Any>>>(parameterField)

        //Static method invocation: "ProjectionUtils.getProjectedValue(dto.field, this.field)"
        val projectionUtilsTypeAccess = spoonApi.factory.createTypeAccess(projectionUtilsCtClass)
        val getProjectedValueInvocation = spoonApi.factory.Code().createInvocation(
            projectionUtilsTypeAccess,
            getProjectedValueMethod,
            mutableListOf<CtExpression<*>>(parameterFieldRead, projectionFieldRead)
        )


        //Parameter field write: "dto.field" in "dto.field = ..."
        val parameterFieldWrite = spoonApi.factory.createFieldWrite<Option<Any>>()
        parameterFieldWrite.setTarget<CtTargetedExpression<Option<Any>, CtExpression<*>>>(parameterAccess)
        parameterFieldWrite.setVariable<CtVariableAccess<Option<Any>>>(parameterField)

        //Assignment: "dto.field = ProjectionUtils.getProjectedValue(dto.field, this.field)"
        val assignmentStatement = spoonApi.factory.Core().createAssignment<Option<Any>, Option<Any>>()
        assignmentStatement.setAssignment<CtRHSReceiver<Option<Any>>>(getProjectedValueInvocation)
        assignmentStatement.setAssigned<CtAssignment<Option<Any>, Option<Any>>>(parameterFieldWrite)

        methodBody.addStatement<CtStatementList>(assignmentStatement)

    }

    result.addMethod<Any, CtType<*>>(method_applyProjectionTo)

    return result

}

fun buildModelClass(dtoClass: CtClass<*>, spoonApi: SpoonAPI): CtClass<*>? {

    val result = spoonApi.factory.createClass(dtoClass, "DtoModel")
    result.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    result.addModifier<CtModifiable>(ModifierKind.STATIC)

    return result

}

fun buildOutputClass(
    spoon: SpoonAPI,
    sourceClass: CtClass<*>,
    dtoClassQualifiedName: String,
    className__ctClass: Map<String, CtClass<Any>>,
    collectedAnnotations: List<CtAnnotation<*>>,
    collectedProperties: List<PropertyData>
): CtClass<*> {

    val result = spoon.factory.createClass(dtoClassQualifiedName)

    //Make it implement the Dto interface
    val dtoInterfaceReference = spoon.factory.Interface()
        .createReference<solutions.sulfura.gend.dtos.Dto<*>>(solutions.sulfura.gend.dtos.Dto::class.java)
    dtoInterfaceReference.addActualTypeArgument<CtActualTypeContainer>(sourceClass.reference)
    result.addSuperInterface<Any, CtType<Any>>(dtoInterfaceReference)

    //Add the getSourceClass method
    val javaClassReference = spoon.factory.Class().get<Any>(Class::class.java)
    val getSourceClassMethod = spoon.factory.createMethod<Any>()
    getSourceClassMethod.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    getSourceClassMethod.setSimpleName<CtMethod<*>>("getSourceClass")
    val returnType = javaClassReference.reference
    returnType.addActualTypeArgument<CtActualTypeContainer>(sourceClass.reference)
    getSourceClassMethod.setType<CtMethod<*>>(returnType)
    val returnExpression = spoon.factory.createCtReturn(spoon.factory.createClassAccess(sourceClass.reference))
    getSourceClassMethod.setBody<CtBodyHolder>(returnExpression)

    result.addMethod<Any, CtType<*>>(getSourceClassMethod)

//    public Class<SourceClassTypes> getSourceClass() {
//        return SourceClassTypes.class;
//    }

    //Add empty constructor
    spoon.factory.createConstructor(result, mutableSetOf(ModifierKind.PUBLIC), null, null)
        .setBody<CtBodyHolder>(spoon.factory.createCtBlock<CtStatement>(null))

    //Add annotations
    collectedAnnotations.forEach { ctAnnotation: CtAnnotation<*> ->
        result.addAnnotation<CtAnnotation<*>>(ctAnnotation)
    }

    //Add fields
    collectedProperties.forEach { propData ->

        var fieldType: CtTypeReference<*>? = null

        if (propData.type.isArray) {
            fieldType = spoon.factory.Class().createReference(List::class.java)
            val listOperationTypeRef = spoon.factory.Class().createReference(ListOperation::class.java)
            val itemType = (propData.type as spoon.reflect.reference.CtArrayTypeReference).arrayType
            listOperationTypeRef.addActualTypeArgument<CtActualTypeContainer>(if (itemType.isPrimitive) itemType.box() else itemType)
            fieldType.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(listOperationTypeRef))
        }

        if (implementsList(propData.type)) {
            fieldType = spoon.factory.Class().createReference(List::class.java)
            val listOperationTypeRef = spoon.factory.Class().createReference(ListOperation::class.java)
            val itemType = propData.type.actualTypeArguments.first()
            listOperationTypeRef.addActualTypeArgument<CtActualTypeContainer>(if (itemType.isPrimitive) itemType.box() else itemType)
            fieldType.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(listOperationTypeRef))
        }

        if (implementsSet(propData.type)) {
            fieldType = spoon.factory.Class().createReference(Set::class.java)
            val listOperationTypeRef = spoon.factory.Class().createReference(ListOperation::class.java)
            val itemType = propData.type.actualTypeArguments.first()
            listOperationTypeRef.addActualTypeArgument<CtActualTypeContainer>(if (itemType.isPrimitive) itemType.box() else itemType)
            fieldType.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(listOperationTypeRef))
        }

        if (fieldType == null) {
            fieldType = propData.type
        }

        val optionFieldType = spoon.factory.Class().createReference(Option::class.java)
        optionFieldType.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(if (fieldType.isPrimitive) fieldType.box() else fieldType))
        fieldType = optionFieldType

        spoon.factory.createField(
            result,
            setOf(ModifierKind.PUBLIC),
            fieldType,
            propData.name
        )

    }

    buildBuilderClass(result, spoon)
    buildProjectionClass(result, sourceClass, spoon)
    buildModelClass(result, spoon)

    return result

}

class PropertyData(val name: String, val type: CtTypeReference<*>)