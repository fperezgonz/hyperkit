package solutions.sulfura

import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.slf4j.LoggerFactory
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf
import solutions.sulfura.processor.utils.implements
import spoon.reflect.declaration.CtClass
import spoon.reflect.reference.CtTypeReference
import java.io.StringWriter
import java.util.*

val velocityEngine: VelocityEngine = initVelocityEngine()

private val logger = LoggerFactory.getLogger("SourceBuilder")

/**
 * Initializes the Velocity engine.
 *
 * @return the initialized Velocity engine
 */
private fun initVelocityEngine(): VelocityEngine {
    val velocityEngine = VelocityEngine()
    val props = Properties()

    props.setProperty("resource.loaders", "file, class")
    velocityEngine.setProperty(
        "resource.loader.file.class",
        "org.apache.velocity.runtime.resource.loader.FileResourceLoader"
    )
    props.setProperty(
        "resource.loader.class.class",
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
    )

    // Empty path allows absolute paths
    props.setProperty("resource.loader.file.path", "")

    // Configure Velocity
    props.setProperty("resource.default_encoding", "UTF-8")
    props.setProperty("output.encoding", "UTF-8")

    velocityEngine.init(props)
    return velocityEngine
}

fun isDto(ctTypeReference: CtTypeReference<*>): Boolean {
    return implements(
        ctTypeReference, typeToImplement = "solutions.sulfura.hyperkit.dtos.Dto"
    )
}

/** @return true if it is not a projection nested within a Dto*/
fun isDefaultDtoProjection(referencedType: CtTypeReference<*>): Boolean {

    if (referencedType.declaringType == null) {
        return false
    }

    if (!isDto(referencedType.declaringType)) {
        return false
    }

    return referencedType.qualifiedName.endsWith("\$Projection")

}

fun buildVelocityContext(dtoCtClass: CtClass<*>, sourceCtClass: CtClass<*>): VelocityContext {

    val importsMap = dtoCtClass.referencedTypes
        .filter { referencedType -> !referencedType.qualifiedName.startsWith(dtoCtClass.qualifiedName) }
        .filter { referencedType -> !referencedType.isPrimitive }
        .filter { referencedType -> !isDefaultDtoProjection(referencedType) }
        .distinctBy { it.simpleName }
        .associateBy { it.qualifiedName }
        .toMutableMap()

    logger.debug("importsMap: {}", importsMap)

    for (type in dtoCtClass.nestedTypes) {
        if (type.simpleName == "Projection") {
            importsMap.put("ProjectionUtils", dtoCtClass.factory.Class().createReference(ProjectionUtils::class.java))
            importsMap.put("FieldConf.Presence",dtoCtClass.factory.Class().createReference(FieldConf.Presence::class.java))
        }
    }

    val imports = importsMap.values

    val velocityContext = VelocityContext()
    velocityContext.put("sourceCtClass", sourceCtClass)
    velocityContext.put("ctClass", dtoCtClass)
    velocityContext.put("imports", imports)
    velocityContext.put("importsMap", importsMap)

    return velocityContext

}


@Synchronized
fun mergeTemplate(template: Template, velocityContext: VelocityContext, stringWriter: StringWriter) {
    template.merge(velocityContext, stringWriter)
}

/**
 * dtoCtClass: the specifications of the dto class
 * sourceCtClass: the specifications of the class from which the dtoCtClass is derived
 */
fun buildClassSource(dtoCtClass: CtClass<*>, sourceCtClass: CtClass<*>, classTemplate: Template): String {

    val velocityContext = buildVelocityContext(dtoCtClass, sourceCtClass)
    val stringWriter = StringWriter()

    mergeTemplate(template = classTemplate, velocityContext = velocityContext, stringWriter);

    return stringWriter.toString()

}