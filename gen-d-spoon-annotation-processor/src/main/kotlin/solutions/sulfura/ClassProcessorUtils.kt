package solutions.sulfura

import solutions.sulfura.gend.dtos.annotations.Dto
import solutions.sulfura.gend.dtos.annotations.DtoFor
import spoon.SpoonAPI
import spoon.reflect.CtModel
import spoon.reflect.declaration.CtAnnotation
import spoon.reflect.declaration.CtClass
import spoon.reflect.declaration.CtElement
import spoon.reflect.declaration.CtField
import spoon.reflect.declaration.CtMethod
import spoon.reflect.declaration.ModifierKind
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

fun collectAnnotations(ctClass: CtClass<*>, spoonApi: SpoonAPI): List<CtAnnotation<*>> {


//        dtoAnnotation.values.forEach {
//            it.value as CtFieldRead<*>
//            println(((it.value as CtFieldRead<*>).target as CtTypeAccess<*>).accessedType.qualifiedName )
//        }
    val result = mutableListOf<CtAnnotation<*>>()
    result.add(createAnnotation_DtoFor(ctClass, spoonApi))
    return result

}

fun buildOutputClass(
    spoon: SpoonAPI,
    dtoClassQualifiedName: String,
    className__ctClass: Map<String, CtClass<Any>>,
    collectedAnnotations: List<CtAnnotation<*>>,
    collectedProperties: CtQuery
): CtClass<*> {

    val result = spoon.factory.createClass(dtoClassQualifiedName)

    collectedAnnotations.forEach { ctAnnotation: CtAnnotation<*> ->
        result.addAnnotation<CtAnnotation<*>>(ctAnnotation)
    }

    collectedProperties.forEach { ctField: CtField<*> ->

        spoon.factory.createField(
            result,
            setOf(ModifierKind.PUBLIC),
            ctField.type,
            ctField.simpleName
        )

    }

    return result

}

private fun createAnnotation_DtoFor(ctClass: CtClass<*>, spoonApi: SpoonAPI): CtAnnotation<DtoFor> {

    val dtoAnnotationCtType = spoonApi.factory.Annotation().get<DtoFor>(DtoFor::class.java)
    val dtoAnnotation = spoonApi.factory.createAnnotation(dtoAnnotationCtType.reference)
    val ctClassAccess = spoonApi.factory.createClassAccess(ctClass.reference)
    dtoAnnotation.addValue<CtAnnotation<DtoFor>>("value", ctClassAccess)
    return dtoAnnotation
}