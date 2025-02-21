package solutions.sulfura

import io.vavr.control.Option
import solutions.sulfura.gend.dtos.ListOperation
import solutions.sulfura.gend.dtos.annotations.Dto
import solutions.sulfura.gend.dtos.annotations.DtoFor
import solutions.sulfura.gend.dtos.projection.DtoProjection
import solutions.sulfura.gend.dtos.projection.ProjectionFor
import spoon.SpoonAPI
import spoon.reflect.CtModel
import spoon.reflect.code.CtBodyHolder
import spoon.reflect.code.CtStatement
import spoon.reflect.declaration.*
import spoon.reflect.reference.CtActualTypeContainer
import spoon.reflect.reference.CtTypeReference
import spoon.reflect.visitor.chain.CtQuery
import spoon.reflect.visitor.filter.CompositeFilter
import spoon.reflect.visitor.filter.FilteringOperator


fun collectClasses(model: CtModel, spoonApi: SpoonAPI): CtQuery {

    val dtoAnnotationCtType = spoonApi.factory.Type().get<Annotation>(Dto::class.java).reference

    val dtoAnnotatedClassesQuery = model.filterChildren { el: CtElement ->
        el is CtClass<*> && el.getAnnotation(dtoAnnotationCtType) != null
    }

    return dtoAnnotatedClassesQuery

}

fun collectProperties(ctClass: CtClass<*>, spoonApi: SpoonAPI): CtQuery {

    val includedAnnotations = ctClass.getAnnotation<Dto>(Dto::class.java).include
        .map { it.java.canonicalName }
        .toSet()

    var filter: CompositeFilter<*> = CompositeFilter(
        FilteringOperator.UNION,
        //Collect public fields
        { el: CtElement -> el is CtField<*> && el.modifiers.contains(ModifierKind.PUBLIC) },
        //Collect getters and setters data
        { el: CtElement ->
            el is CtMethod<*> && el.modifiers.contains(ModifierKind.PUBLIC) &&
                    (
                            //is getter
                            ((el.simpleName.startsWith("get") || el.simpleName.startsWith("is"))
                                    && el.type != spoonApi.factory.Type().voidType())
                                    ||
                                    //is setter
                                    (el.simpleName.startsWith("set")
                                            && el.type == spoonApi.factory.Type().voidType()
                                            && el.parameters.size == 1)
                            )
        }
    )

    filter = CompositeFilter(
        FilteringOperator.INTERSECTION,
        { el: CtElement ->
            //Collect elements with the selected annotations
            includedAnnotations.isEmpty() || el.annotations.any({ annotation -> annotation.type.qualifiedName in includedAnnotations })
        },
        filter
    )

    val dtoPropertiesQuery = ctClass.filterChildren(filter)

    return dtoPropertiesQuery

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


//        dtoAnnotation.values.forEach {
//            it.value as CtFieldRead<*>
//            println(((it.value as CtFieldRead<*>).target as CtTypeAccess<*>).accessedType.qualifiedName )
//        }
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
    result.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    result.addModifier<CtModifiable>(ModifierKind.STATIC)

    result.addAnnotation<CtAnnotation<ProjectionFor>>(createAnnotation_ProjectionFor(dtoClass, sourceClass, spoonApi))
    //Make it extend the Projection superclass
    val projectionSuperclass = spoonApi.factory.Class().createReference<DtoProjection<*>>(DtoProjection::class.java)
    projectionSuperclass.addActualTypeArgument<CtActualTypeContainer>(dtoClass.reference)
    result.setSuperclass<CtType<*>>(projectionSuperclass)

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
    collectedProperties: CtQuery
): CtClass<*> {

    val result = spoon.factory.createClass(dtoClassQualifiedName)

    //Make it implement the Dto interface
    val dtoInterfaceReference = spoon.factory.Interface().createReference<Dto>(Dto::class.java)
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
    collectedProperties.forEach { ctField: CtField<*> ->

        var fieldType: CtTypeReference<*>? = null

        if (ctField.type.isArray) {
            fieldType = spoon.factory.Class().createReference(List::class.java)
            val listOperationTypeRef = spoon.factory.Class().createReference(ListOperation::class.java)
            val itemType = (ctField.type as spoon.reflect.reference.CtArrayTypeReference).arrayType
            listOperationTypeRef.addActualTypeArgument<CtActualTypeContainer>(if (itemType.isPrimitive) itemType.box() else itemType)
            fieldType.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(listOperationTypeRef))
        }

        if (implementsList(ctField.type)) {
            fieldType = spoon.factory.Class().createReference(List::class.java)
            val listOperationTypeRef = spoon.factory.Class().createReference(ListOperation::class.java)
            val itemType = ctField.type.actualTypeArguments.first()
            listOperationTypeRef.addActualTypeArgument<CtActualTypeContainer>(if (itemType.isPrimitive) itemType.box() else itemType)
            fieldType.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(listOperationTypeRef))
        }

        if (implementsSet(ctField.type)) {
            fieldType = spoon.factory.Class().createReference(Set::class.java)
            val listOperationTypeRef = spoon.factory.Class().createReference(ListOperation::class.java)
            val itemType = ctField.type.actualTypeArguments.first()
            listOperationTypeRef.addActualTypeArgument<CtActualTypeContainer>(if (itemType.isPrimitive) itemType.box() else itemType)
            fieldType.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(listOperationTypeRef))
        }

        if (fieldType == null) {
            fieldType = ctField.type!!
        }

        val optionFieldType = spoon.factory.Class().createReference(Option::class.java)
        optionFieldType.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(if (fieldType.isPrimitive) fieldType.box() else fieldType))
        fieldType = optionFieldType

        spoon.factory.createField(
            result,
            setOf(ModifierKind.PUBLIC),
            fieldType,
            ctField.simpleName
        )

    }

    buildBuilderClass(result, spoon)
    buildProjectionClass(result, sourceClass, spoon)
    buildModelClass(result, spoon)

    return result

}