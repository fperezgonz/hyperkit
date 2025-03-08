package solutions.sulfura.processor.utils

import solutions.sulfura.gend.dtos.annotations.Dto
import solutions.sulfura.gend.dtos.annotations.DtoFor
import spoon.SpoonAPI
import spoon.reflect.CtModel
import spoon.reflect.declaration.CtAnnotation
import spoon.reflect.declaration.CtClass
import spoon.reflect.declaration.CtElement
import spoon.reflect.declaration.CtMethod
import spoon.reflect.declaration.ModifierKind
import spoon.reflect.factory.Factory
import spoon.reflect.reference.CtFieldReference
import spoon.reflect.reference.CtTypeParameterReference
import spoon.reflect.reference.CtTypeReference
import spoon.reflect.visitor.chain.CtQuery
import java.util.Locale

fun capitalize(s: String): String {
    return s.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun uncapitalize(s: String): String {
    return s.replaceFirstChar { it.lowercase(Locale.getDefault()) }
}

private fun createAnnotation_DtoFor(ctClass: CtClass<*>, spoonApi: SpoonAPI): CtAnnotation<DtoFor> {

    val dtoAnnotationCtType = spoonApi.factory.Annotation().get<DtoFor>(DtoFor::class.java)
    val dtoAnnotation = spoonApi.factory.createAnnotation(dtoAnnotationCtType.reference)
    val ctClassAccess = spoonApi.factory.createClassAccess(ctClass.reference)
    dtoAnnotation.addValue<CtAnnotation<DtoFor>>("value", ctClassAccess)
    return dtoAnnotation
}

fun collectAnnotations(ctClass: CtClass<*>, spoonApi: SpoonAPI): List<CtAnnotation<*>> {

    val result = mutableListOf<CtAnnotation<*>>()
    result.add(createAnnotation_DtoFor(ctClass, spoonApi))
    return result

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

    val result = mutableMapOf<String, PropertyData>()

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
        result.put(el.simpleName, PropertyData(el.simpleName, typeWithInferredParameters))

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
            val propertyName = uncapitalize(methodName.substring(3))
            result.put(propertyName, PropertyData(propertyName, typeWithInferredParameters))

        }

        //Is a getter
        if (methodName.startsWith("is") && el.type != factory.Type().voidPrimitiveType()) {

            val typeWithInferredParameters = buildTypeReferenceWithInferredParameters(el.type, typeParamMap, factory)
            val propertyName = uncapitalize(methodName.substring(2))
            result.put(propertyName, PropertyData(propertyName, typeWithInferredParameters))

        }

        //Is a setter
        if (methodName.startsWith("set") && el.type == factory.Type().voidPrimitiveType() && el.parameters.size == 1) {

            val setterParamType = el.parameters.first().type
            val typeWithInferredParameters =
                buildTypeReferenceWithInferredParameters(setterParamType, typeParamMap, factory)
            val propertyName = uncapitalize(methodName.substring(3))
            result.put(propertyName, PropertyData(propertyName, typeWithInferredParameters))

        }

        return@filter

    }

    if (typeReference.superclass != null) {
        result.putAll(collectProperties(typeReference.superclass, factory).associateBy { it.name })
    }

    return return result.values.toList()

}