package solutions.sulfura.processor.utils

import io.vavr.control.Option
import solutions.sulfura.gend.dtos.Dto
import solutions.sulfura.gend.dtos.ListOperation
import solutions.sulfura.gend.dtos.projection.DtoProjection
import solutions.sulfura.gend.dtos.projection.DtoProjectionException
import solutions.sulfura.gend.dtos.projection.ProjectionFor
import solutions.sulfura.gend.dtos.projection.ProjectionUtils
import solutions.sulfura.gend.dtos.projection.fields.DtoFieldConf
import solutions.sulfura.gend.dtos.projection.fields.DtoListFieldConf
import solutions.sulfura.gend.dtos.projection.fields.FieldConf
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf
import spoon.reflect.code.*
import spoon.reflect.declaration.*
import spoon.reflect.factory.Factory
import spoon.reflect.reference.CtActualTypeContainer
import spoon.reflect.reference.CtArrayTypeReference
import spoon.reflect.reference.CtTypeReference
import spoon.reflect.reference.CtVariableReference

private fun createAnnotation_ProjectionFor(
    ctClass: CtClass<*>,
    factory: Factory
): CtAnnotation<ProjectionFor> {

    val dtoAnnotationCtType = factory.Annotation().get<ProjectionFor>(ProjectionFor::class.java)
    val dtoAnnotation = factory.createAnnotation(dtoAnnotationCtType.reference)
    val ctSourceClassAccess = factory.createClassAccess(ctClass.reference)
    dtoAnnotation.addValue<CtAnnotation<ProjectionFor>>("value", ctSourceClassAccess)
    return dtoAnnotation

}

fun buildBuilderClass(dtoClass: CtClass<*>, factory: Factory): CtClass<*>? {

    val result = factory.createClass(dtoClass, "Builder")
    result.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    result.addModifier<CtModifiable>(ModifierKind.STATIC)
    return result

}

fun isInnerType(ctType: CtTypeReference<*>): CtTypeReference<*>? {

    if (ctType.qualifiedName.contains("$")) {
        return ctType
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

        var c = t

        //If the type declaration cannot be found, the type is likely to be an inner type
        if (c.typeDeclaration == null) {
            c = isInnerType(c) ?: c
        }


        if (c.qualifiedName == typeToImplement) {
            return true
        }

        if (c.superclass != null && implements(c.superclass, typeToImplement = typeToImplement)) {
            return true
        }

        if (c.superInterfaces == null || c.superInterfaces.isEmpty()) {
            continue
        }

        for (interf in c.superInterfaces) {
            if (implements(interf, typeToImplement = typeToImplement)) {
                return true
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

fun buildProjectionClass(dtoClass: CtClass<*>, factory: Factory): CtClass<*>? {

    val result = factory.createClass(dtoClass, "Projection")
    val fieldConf = factory.Class().createReference(FieldConf::class.java)
    val dtoFieldConf = factory.Class().createReference(DtoFieldConf::class.java)
    val listFieldConf = factory.Class().createReference(ListFieldConf::class.java)
    val dtoListFieldConf = factory.Class().createReference(DtoListFieldConf::class.java)

    result.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    result.addModifier<CtModifiable>(ModifierKind.STATIC)

    result.addAnnotation<CtAnnotation<ProjectionFor>>(createAnnotation_ProjectionFor(dtoClass, factory))
    //Make it extend the Projection superclass
    val projectionSuperclass = factory.Class().createReference<DtoProjection<*>>(DtoProjection::class.java)
    projectionSuperclass.addActualTypeArgument<CtActualTypeContainer>(dtoClass.reference)
    result.setSuperclass<CtType<*>>(projectionSuperclass)

    //Build Projection fields
    dtoClass.fields.forEach { ctField ->

        val optionalGenericArgType = ctField.type.actualTypeArguments.first()


        val isCollection = optionalGenericArgType.qualifiedName == List::class.java.canonicalName
                || optionalGenericArgType.qualifiedName == Set::class.java.canonicalName
                || optionalGenericArgType.isArray

        val elementType =
            if (isCollection) optionalGenericArgType.actualTypeArguments.first().actualTypeArguments.first() else optionalGenericArgType
        val isDto = implements(elementType, typeToImplement = Dto::class.java.canonicalName)
        val fieldType: CtTypeReference<*>

        if (isCollection) {
            fieldType = if (isDto) dtoListFieldConf else listFieldConf
        } else {
            fieldType = if (isDto) dtoFieldConf else fieldConf
        }

        if (isDto) {
            val elementProjectionTypeReference = factory.Class().createReference<Any>(elementType.qualifiedName + "\$Projection")
            fieldType.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(elementProjectionTypeReference))
        }

        val newField = factory.createField(
            result,
            setOf(ModifierKind.PUBLIC),
            fieldType,
            ctField.simpleName
        )

        result.addField<Any, CtType<*>>(newField)

    }

    //Add empty constructor
    factory.createConstructor(result, mutableSetOf(ModifierKind.PUBLIC), null, null)
        .setBody<CtBodyHolder>(factory.createCtBlock<CtStatement>(null))

    //Method "public void applyProjectionTo(SourceClassTypesDto dto) throws DtoProjectionException"
    val method_applyProjectionTo = factory.createMethod<Any>()
    method_applyProjectionTo.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    method_applyProjectionTo.setSimpleName<CtMethod<*>>("applyProjectionTo")
    method_applyProjectionTo.setType<CtMethod<*>>(factory.Type().voidPrimitiveType())

    val parameter = factory.createParameter<Any>()
    parameter.setSimpleName<CtNamedElement>("dto")
    parameter.setType<CtTypedElement<*>>(dtoClass.reference)
    method_applyProjectionTo.addParameter<CtExecutable<Any>>(parameter)

    val exception = factory.Type().get<DtoProjectionException>(DtoProjectionException::class.java).reference
    method_applyProjectionTo.addThrownType<CtExecutable<Any>>(exception)

    val methodBody: CtBlock<Int?> = factory.Core().createBlock()

    //Add statements "dto.field = ProjectionUtils.getProjectedValue(dto.field, this.field)" to method body
    result.fields.forEach { ctField ->
        @Suppress("UNCHECKED_CAST")
        ctField as CtField<Option<Any>>

        val projectionUtilsCtClass = factory.Class().createReference(ProjectionUtils::class.java)

        @Suppress("UNCHECKED_CAST")
        val optionCtClass = factory.Class().createReference(Option::class.java) as CtTypeReference<Option<Any>>
        val getProjectedValueMethod = factory.Method().createReference(
            projectionUtilsCtClass,
            true,
            optionCtClass,
            "getProjectedValue",
            mutableListOf<CtTypeReference<*>>(optionCtClass, ctField.type)
        )

        //Field read: "this.field" in "ProjectionUtils.getProjectedValue(dto.field, this.field)"
        val projectionFieldRead = factory.createFieldRead<Option<Any>>()
        val thisAccess = factory.createThisAccess<Any>(result.reference)
        projectionFieldRead.setTarget<CtTargetedExpression<Option<Any>, CtExpression<*>>>(thisAccess)
        projectionFieldRead.setVariable<CtVariableAccess<Option<Any>>>(ctField.reference)

        //Parameter field read: "dto.field" in "ProjectionUtils.getProjectedValue(dto.field, this.field)"
        @Suppress("UNCHECKED_CAST")
        val parameterField =
            parameter.type.getDeclaredOrInheritedField(ctField.simpleName) as CtVariableReference<Option<Any>>
        val parameterAccess = factory.Code().createVariableRead(parameter.reference, false)
        val parameterFieldRead = factory.createFieldRead<Option<Any>>()
        parameterFieldRead.setTarget<CtTargetedExpression<Option<Any>, CtExpression<*>>>(parameterAccess)
        parameterFieldRead.setVariable<CtVariableAccess<Option<Any>>>(parameterField)

        //Static method invocation: "ProjectionUtils.getProjectedValue(dto.field, this.field)"
        val projectionUtilsTypeAccess = factory.createTypeAccess(projectionUtilsCtClass)
        val getProjectedValueInvocation = factory.Code().createInvocation(
            projectionUtilsTypeAccess,
            getProjectedValueMethod,
            mutableListOf<CtExpression<*>>(parameterFieldRead, projectionFieldRead)
        )


        //Parameter field write: "dto.field" in "dto.field = ..."
        val parameterFieldWrite = factory.createFieldWrite<Option<Any>>()
        parameterFieldWrite.setTarget<CtTargetedExpression<Option<Any>, CtExpression<*>>>(parameterAccess)
        parameterFieldWrite.setVariable<CtVariableAccess<Option<Any>>>(parameterField)

        //Assignment: "dto.field = ProjectionUtils.getProjectedValue(dto.field, this.field)"
        val assignmentStatement = factory.Core().createAssignment<Option<Any>, Option<Any>>()
        assignmentStatement.setAssignment<CtRHSReceiver<Option<Any>>>(getProjectedValueInvocation)
        assignmentStatement.setAssigned<CtAssignment<Option<Any>, Option<Any>>>(parameterFieldWrite)

        methodBody.addStatement<CtStatementList>(assignmentStatement)

    }

    result.addMethod<Any, CtType<*>>(method_applyProjectionTo)

    return result

}

fun buildModelClass(dtoClass: CtClass<*>, factory: Factory): CtClass<*>? {

    val result = factory.createClass(dtoClass, "DtoModel")
    result.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    result.addModifier<CtModifiable>(ModifierKind.STATIC)

    return result

}

fun buildReplacedType(
    typeRef: CtTypeReference<*>,
    className__ctClass: Map<String, CtClass<*>>,
    factory: Factory
): CtTypeReference<*> {

    var result = typeRef
    val mappedType = className__ctClass[typeRef.qualifiedName]

    // Create type reference of the replacement type (if necessary)
    if (mappedType != null && typeRef.typeDeclaration != mappedType) {
        result = factory.Type().createReference(mappedType)
        result.setActualTypeArguments<CtActualTypeContainer>(typeRef.actualTypeArguments.toMutableList())
    }

    if (typeRef.isPrimitive || !typeRef.isParameterized) {
        return result
    }

    var changesDetected = false
    val typeArgs = mutableListOf<CtTypeReference<*>>()
    //Calculate replacements for type arguments
    for (innerType in typeRef.actualTypeArguments) {
        val replacedInnerType = buildReplacedType(innerType, className__ctClass, factory)
        typeArgs.add(replacedInnerType)

        //If a new type was created, mark changesDetected
        if (replacedInnerType != innerType) {
            changesDetected = true
        }

    }

    //If there were no changes to type arguments, return the result as is
    if (!changesDetected) {
        return result
    }

    //Create a new type reference if necessary
    if (result == typeRef) {
        result = factory.Type().createReference<Any>(typeRef.qualifiedName)
        result.setActualTypeArguments<CtActualTypeContainer>(typeRef.actualTypeArguments.toMutableList())
    }

    //Apply calculated type parameters
    for (idx in typeRef.actualTypeArguments.indices) {
        result.actualTypeArguments[idx] = typeArgs[idx]
    }

    //Return the new type reference, or the original type reference if no type reference was created
    return result

}

fun wrapCollection(
    typeRef: CtTypeReference<*>,
    factory: Factory
): CtTypeReference<*> {

    if (!typeRef.isArray && (typeRef.isPrimitive || !typeRef.isParameterized)) {
        return typeRef
    }

    //Is this type a collection?
    val isList = implementsList(typeRef) || typeRef.isArray
    val isSet = implementsSet(typeRef)
    val isCollection = isList || isSet
    val itemType =
        if (typeRef.isArray) (typeRef as CtArrayTypeReference).arrayType else if (isCollection) typeRef.actualTypeArguments.first() else null

    var changesDetected = false
    val typeArgs = mutableListOf<CtTypeReference<*>>()

    //Calculate replacements for type arguments
    for (innerType in typeRef.actualTypeArguments) {

        var replacedInnerType = wrapCollection(innerType, factory)

        if (isCollection) {
            itemType!!
            replacedInnerType = factory.Class().createReference(ListOperation::class.java)
            replacedInnerType.addActualTypeArgument<CtActualTypeContainer>(if (itemType.isPrimitive) itemType.box() else itemType)
        }

        typeArgs.add(replacedInnerType)

        //If a new type was created, mark changesDetected
        if (replacedInnerType != innerType) {
            changesDetected = true
        }

    }

    val result: CtTypeReference<*>

    if (!isCollection) {

        //If there were no changes to type arguments and it is not a collection, return the original type
        if (!changesDetected) {
            return typeRef
        }

        result = factory.Type().createReference<Any>(typeRef.qualifiedName)
        result.setActualTypeArguments<CtActualTypeContainer>(typeArgs)

        return result

    }

    //If it is a collection the itemType will never be null
    itemType!!

    if (typeRef.isArray) {

        val wrappedElementType = wrapCollection(itemType, factory)
        val replacedInnerType = factory.Class().createReference(ListOperation::class.java)
        replacedInnerType.addActualTypeArgument<CtActualTypeContainer>(if (wrappedElementType.isPrimitive) itemType.box() else wrappedElementType)

        typeArgs.add(0, replacedInnerType)

    }

    result = factory.Class().createReference(if (isList) List::class.java else Set::class.java)
    result.setActualTypeArguments<CtActualTypeContainer>(typeArgs)

    //Return the new type reference
    return result

}

fun removePrefixUntilMismatch(string: String, prefix: String): String {

    var mismatchIndex = 0

    // Find the first mismatching character index
    while (mismatchIndex < string.length
        && mismatchIndex < prefix.length
        && string[mismatchIndex] == prefix[mismatchIndex]
    ) {
        mismatchIndex++
    }

    // Remove all characters up to the mismatch index
    return string.substring(mismatchIndex)


}

fun sourceClassToDtoClassReference(ctClass: CtClass<*>, factory: Factory): CtClass<*> {

    var dtoClassPackage = "solutions.sulfura.gend.dtos." + removePrefixUntilMismatch(
        ctClass.`package`.qualifiedName,
        "solutions.sulfura.gend.dtos"
    )

    val dtoClassSimpleName = ctClass.simpleName + "Dto"
    val dtoClassQualifiedName = "$dtoClassPackage.$dtoClassSimpleName"

    var result = factory.Class().get<Any>(dtoClassQualifiedName)

    if (result != null) {
        return result
    }

    result = factory.Class().create(dtoClassQualifiedName)
    result.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    val superInterfaces = result.superInterfaces.toMutableSet()
    val dtoInterfaceReference = factory.Type().createReference(Dto::class.java)
    superInterfaces.add(dtoInterfaceReference)

    result.setSuperInterfaces<CtType<Any>>(superInterfaces)

    return result

}

fun buildOutputClass(
    sourceClass: CtClass<*>,
    dtoClassQualifiedName: String,
    sourceClassName__dtoCtClass: Map<String, CtClass<*>>,
    collectedAnnotations: List<CtAnnotation<*>>,
    collectedProperties: List<PropertyData>,
    valueWrapperType: CtType<*>?,
    factory: Factory
): CtClass<*> {

    val result = factory.createClass(dtoClassQualifiedName)
    result.addModifier<CtModifiable>(ModifierKind.PUBLIC)

    //Make it implement the Dto interface
    val dtoInterfaceReference = factory.Interface()
        .createReference<Dto<*>>(Dto::class.java)
    dtoInterfaceReference.addActualTypeArgument<CtActualTypeContainer>(sourceClass.reference)
    result.addSuperInterface<Any, CtType<Any>>(dtoInterfaceReference)

    //Add the getSourceClass method
    val javaClassReference = factory.Class().get<Any>(Class::class.java)
    val getSourceClassMethod = factory.createMethod<Any>()
    getSourceClassMethod.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    getSourceClassMethod.setSimpleName<CtMethod<*>>("getSourceClass")
    val returnType = javaClassReference.reference
    returnType.addActualTypeArgument<CtActualTypeContainer>(sourceClass.reference)
    getSourceClassMethod.setType<CtMethod<*>>(returnType)
    val returnExpression = factory.createCtReturn(factory.createClassAccess(sourceClass.reference))
    getSourceClassMethod.setBody<CtBodyHolder>(returnExpression)

    result.addMethod<Any, CtType<*>>(getSourceClassMethod)

    //Add empty constructor
    factory.createConstructor(result, mutableSetOf(ModifierKind.PUBLIC), null, null)
        .setBody<CtBodyHolder>(factory.createCtBlock<CtStatement>(null))

    //Add annotations
    collectedAnnotations.forEach { ctAnnotation: CtAnnotation<*> ->
        result.addAnnotation<CtAnnotation<*>>(ctAnnotation)
    }

    //Add fields
    collectedProperties.forEach { propData ->

        var fieldType: CtTypeReference<*> = propData.type

        //Wrap collection elements in ListOperation
        fieldType = wrapCollection(fieldType, factory)

        //Replace types with dto types
        fieldType = buildReplacedType(fieldType, sourceClassName__dtoCtClass, factory)

        val valueWrapperTypeReference = factory.Type().createReference(valueWrapperType)
        valueWrapperTypeReference.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(if (fieldType.isPrimitive) fieldType.box() else fieldType))
        fieldType = valueWrapperTypeReference

        factory.createField(
            result,
            setOf(ModifierKind.PUBLIC),
            fieldType,
            propData.name
        )

    }

    buildBuilderClass(result, factory)
    buildProjectionClass(result, factory)
    buildModelClass(result, factory)

    return result

}

class PropertyData(val name: String, val type: CtTypeReference<*>)