package solutions.sulfura

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import spoon.reflect.declaration.CtClass
import java.io.StringWriter

class SourceBuilder {
    /**
     * dtoCtClass: the specifications of the dto class
     * sourceCtClass: the specifications of the class from which the dtoCtClass is derived
     */
    fun buildClassSource(dtoCtClass: CtClass<*>, sourceCtClass: CtClass<*>): String {

        val velocityContext = VelocityContext()
        velocityContext.put("sourceCtClass", sourceCtClass)
        velocityContext.put("ctClass", dtoCtClass)
        val velocityEngine = VelocityEngine()
        velocityEngine.setProperty("resource.loaders", "file, class")
//        velocityEngine.setProperty("resource.loader.file.path", "./src/main/resources/velocity_templates/")
//        velocityEngine.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader")
        velocityEngine.setProperty("resource.loader.class.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
        velocityEngine.init()

//        val classTemplateFile = javaClass.getResource("/velocity_templates/class.vm")!!.toURI()
//        val classTemplate = velocityEngine.getTemplate(classTemplateFile.toPath().toString())
        val classTemplate = velocityEngine.getTemplate("velocity_templates/class.vm")
        val stringWriter = StringWriter()
        classTemplate.merge(velocityContext, stringWriter)
        println(stringWriter.toString())

        return stringWriter.toString()

    }
}