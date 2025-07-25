package solutions.sulfura

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf
import spoon.reflect.declaration.CtClass
import java.io.StringWriter
import java.util.Properties

class SourceBuilder {

    private val velocityEngine: VelocityEngine = initVelocityEngine()

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

    fun buildVelocityContext(dtoCtClass: CtClass<*>, sourceCtClass: CtClass<*>): VelocityContext {

        val imports = dtoCtClass.referencedTypes
            .filter { referencedType -> !referencedType.qualifiedName.startsWith(dtoCtClass.qualifiedName) }
            .distinctBy { it.qualifiedName }
            .toMutableSet()

        for (type in dtoCtClass.nestedTypes) {
            if (type.simpleName == "Projection") {
                imports.add(dtoCtClass.factory.Class().createReference(ProjectionUtils::class.java))
                imports.add(dtoCtClass.factory.Class().createReference(FieldConf.Presence::class.java))
            }
        }

        val velocityContext = VelocityContext()
        velocityContext.put("sourceCtClass", sourceCtClass)
        velocityContext.put("ctClass", dtoCtClass)
        velocityContext.put("imports", imports)

        return velocityContext

    }

    /**
     * dtoCtClass: the specifications of the dto class
     * sourceCtClass: the specifications of the class from which the dtoCtClass is derived
     */
    fun buildClassSource(dtoCtClass: CtClass<*>, sourceCtClass: CtClass<*>, templatePath:String): String {

        val velocityContext = buildVelocityContext(dtoCtClass, sourceCtClass)
        val classTemplate = velocityEngine.getTemplate(templatePath)
        val stringWriter = StringWriter()

        classTemplate.merge(velocityContext, stringWriter)

        return stringWriter.toString()

    }
}