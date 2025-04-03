package solutions.sulfura.processor.utils

import solutions.sulfura.hyperkit.dtos.annotations.Dto
import solutions.sulfura.hyperkit.dtos.annotations.DtoFor
import solutions.sulfura.hyperkit.dtos.annotations.DtoProperty
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

/**
 * Builds a PropertyData object from a DtoProperty annotation
 *
 * @param annotation The DtoProperty annotation to extract data from
 * @param name The name of the property
 * @param type The type of the property
 * @return A PropertyData object with the properties from the annotation
 */
fun buildPropertyDataFromAnnotation(annotation: DtoProperty?, name: String, type: CtTypeReference<*>): PropertyData {
    val createGetter = annotation?.createGetter ?: false
    val createSetter = annotation?.createSetter ?: false

    return PropertyData(
        name,
        type,
        createGetter,
        createSetter
    )
}

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

    val dtoAnnotation = typeReference.typeDeclaration.getAnnotation<Dto>(Dto::class.java)

    val includedAnnotations =
        if (dtoAnnotation == null) {
            mutableSetOf<Any>()
        } else {
            dtoAnnotation.include.map { it.java.canonicalName }.toSet()
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

    typeReference.declaredFields.forEach filter@{ ctFieldRef: CtFieldReference<*> ->

        //If it does not contain any of the annotations used to mark inclusion
        if (!includedAnnotations.isEmpty()
            && !ctFieldRef.fieldDeclaration.annotations.any { annotation -> annotation.type.qualifiedName in includedAnnotations }
        ) {
            return@filter
        }

        //Is not a public field
        if (!ctFieldRef.modifiers.contains(ModifierKind.PUBLIC)) {
            return@filter
        }

        val typeWithInferredParameters =
            buildTypeReferenceWithInferredParameters(ctFieldRef.type, typeParamMap, factory)

        // Extract DtoProperty annotation data and build PropertyData
        val dtoPropertyAnnotation = ctFieldRef.fieldDeclaration.getAnnotation<DtoProperty>(DtoProperty::class.java)
        val propertyData = buildPropertyDataFromAnnotation(dtoPropertyAnnotation, ctFieldRef.simpleName, typeWithInferredParameters)

        result.put(ctFieldRef.simpleName, propertyData)

    }

    typeReference.typeDeclaration.methods.forEach filter@{ ctMethod: CtMethod<*> ->

        //If it does not contain any of the annotations used to mark inclusion
        if (!includedAnnotations.isEmpty()
            && !ctMethod.annotations.any { annotation -> annotation.type.qualifiedName in includedAnnotations }
        ) {
            return@filter
        }

        //Is not a public method
        if (!ctMethod.modifiers.contains(ModifierKind.PUBLIC)) {
            return@filter
        }

        val methodName = ctMethod.simpleName

        //Is a getter
        if (methodName.startsWith("get") && ctMethod.type != factory.Type().voidPrimitiveType()) {

            val typeWithInferredParameters =
                buildTypeReferenceWithInferredParameters(ctMethod.type, typeParamMap, factory)
            var propertyName = methodName.substring(3)
            val dtoPropertyAnnotation = ctMethod.getAnnotation<DtoProperty>(DtoProperty::class.java)

            // Extract annotation data
            val customPropertyName = dtoPropertyAnnotation?.propertyName?.takeIf { it.isNotEmpty() }
            val preserveCase = dtoPropertyAnnotation?.preserveCase ?: false

            if (!preserveCase) {
                propertyName = uncapitalize(propertyName)
            }

            // Use custom property name if provided
            val finalPropertyName = customPropertyName ?: propertyName

            // Build PropertyData directly
            val propertyData = buildPropertyDataFromAnnotation(dtoPropertyAnnotation, finalPropertyName, typeWithInferredParameters)

            result.put(finalPropertyName, propertyData)

        }

        //Is a getter
        if (methodName.startsWith("is") && ctMethod.type != factory.Type().voidPrimitiveType()) {

            val typeWithInferredParameters =
                buildTypeReferenceWithInferredParameters(ctMethod.type, typeParamMap, factory)
            var propertyName = methodName.substring(2)
            val dtoPropertyAnnotation = ctMethod.getAnnotation<DtoProperty>(DtoProperty::class.java)

            // Extract annotation data
            val customPropertyName = dtoPropertyAnnotation?.propertyName?.takeIf { it.isNotEmpty() }
            val preserveCase = dtoPropertyAnnotation?.preserveCase ?: false

            if (!preserveCase) {
                propertyName = uncapitalize(propertyName)
            }

            // Use custom property name if provided
            val finalPropertyName = customPropertyName ?: propertyName

            // Build PropertyData directly
            val propertyData = buildPropertyDataFromAnnotation(dtoPropertyAnnotation, finalPropertyName, typeWithInferredParameters)

            result.put(finalPropertyName, propertyData)

        }

        //Is a setter
        if (methodName.startsWith("set")
            && ctMethod.type == factory.Type().voidPrimitiveType()
            && ctMethod.parameters.size == 1
        ) {

            val setterParamType = ctMethod.parameters.first().type
            val typeWithInferredParameters =
                buildTypeReferenceWithInferredParameters(setterParamType, typeParamMap, factory)
            var propertyName = methodName.substring(3)
            val dtoPropertyAnnotation = ctMethod.getAnnotation<DtoProperty>(DtoProperty::class.java)

            // Extract annotation data
            val customPropertyName = dtoPropertyAnnotation?.propertyName?.takeIf { it.isNotEmpty() }
            val preserveCase = dtoPropertyAnnotation?.preserveCase ?: false

            if (!preserveCase) {
                propertyName = uncapitalize(propertyName)
            }

            // Use custom property name if provided
            val finalPropertyName = customPropertyName ?: propertyName

            // Build PropertyData directly
            val propertyData = buildPropertyDataFromAnnotation(dtoPropertyAnnotation, finalPropertyName, typeWithInferredParameters)

            result.put(finalPropertyName, propertyData)

        }

        return@filter

    }

    if (typeReference.superclass != null) {
        result.putAll(collectProperties(typeReference.superclass, factory).associateBy { it.name })
    }

    return return result.values.toList()

}