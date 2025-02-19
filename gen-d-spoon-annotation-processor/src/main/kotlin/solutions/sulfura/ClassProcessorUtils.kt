package solutions.sulfura

import groovyjarjarasm.asm.TypeReference
import io.vavr.control.Option
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

private fun createAnnotation_ProjectionFor(ctClass: CtClass<*>, ctSourceClass: CtClass<*>, spoonApi: SpoonAPI): CtAnnotation<ProjectionFor> {
    val dtoAnnotationCtType = spoonApi.factory.Annotation().get<ProjectionFor>(ProjectionFor::class.java)
    val dtoAnnotation = spoonApi.factory.createAnnotation(dtoAnnotationCtType.reference)
    val ctSourceClassAccess = spoonApi.factory.createClassAccess(ctSourceClass.reference)
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

fun buildProjectionClass(dtoClass: CtClass<*>, sourceClass: CtClass<*>, spoonApi: SpoonAPI): CtClass<*>? {

    val result = spoonApi.factory.createClass(dtoClass, "Projection")
    result.addModifier<CtModifiable>(ModifierKind.PUBLIC)
    result.addModifier<CtModifiable>(ModifierKind.STATIC)

    result.addAnnotation<CtAnnotation<ProjectionFor>>(createAnnotation_ProjectionFor(dtoClass, sourceClass, spoonApi))
    //Make it extend the Projection superclass
    val projectionSuperclass = spoonApi.factory.Class().createReference<DtoProjection<*>>(DtoProjection::class.java)
    projectionSuperclass.addActualTypeArgument<CtActualTypeContainer>(sourceClass.reference)
    result.setSuperclass<CtType<*>>(projectionSuperclass)

    return result;

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

        var fieldType = spoon.factory.Class().createReference(Option::class.java)
        fieldType.setActualTypeArguments<CtActualTypeContainer>(mutableListOf(if (ctField.type.isPrimitive) ctField.type.box() else ctField.type))

        spoon.factory.createField(
            result,
            setOf(ModifierKind.PUBLIC),
            fieldType,
            ctField.simpleName
        )

    }

    buildBuilderClass(result, spoon)
    buildProjectionClass(result, sourceClass, spoon)

    return result

}