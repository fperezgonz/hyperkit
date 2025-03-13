package solutions.sulfura

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import solutions.sulfura.gend.dtos.projection.ProjectionUtils
import spoon.reflect.declaration.CtClass
import java.io.StringWriter

class SourceBuilder {
    /**
     * dtoCtClass: the specifications of the dto class
     * sourceCtClass: the specifications of the class from which the dtoCtClass is derived
     */
    fun buildClassSource(dtoCtClass: CtClass<*>, sourceCtClass: CtClass<*>, valueWrapperDefaultvalue:String): String {

//        This does not include ProjectionUtils in the imports
//        val cu:CtCompilationUnit = dtoCtClass.getFactory().createCompilationUnit();
//        cu.addDeclaredType(dtoCtClass);
//        ForceFullyQualifiedProcessor().process(cu)
//        ForceImportProcessor().process(cu)
//        ImportCleaner().process(cu)
//        ImportConflictDetector().process(cu)
//        val imports = cu.imports
//        println(imports)


        val imports = dtoCtClass.referencedTypes.distinctBy { it.qualifiedName }.toMutableSet()
        for (type in dtoCtClass.nestedTypes) {
            if (type.simpleName == "Projection") {
                imports.add(dtoCtClass.factory.Class().createReference(ProjectionUtils::class.java))
            }
        }
        val velocityContext = VelocityContext()
        velocityContext.put("sourceCtClass", sourceCtClass)
        velocityContext.put("valueWrapperDefaultValue", valueWrapperDefaultvalue)
        velocityContext.put("ctClass", dtoCtClass)
        velocityContext.put("imports", imports)
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

        return stringWriter.toString()

    }
}