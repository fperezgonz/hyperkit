package solutions.sulfura

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor
import javax.inject.Inject

@CacheableTask
abstract class GenerateDtosTask : DefaultTask() {

    @get:Inject
    abstract val workerExecutor: WorkerExecutor

    /** Absolute paths of the input sources to be processed (files or folders) */
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val inputFiles: ConfigurableFileCollection

    /** Root path for the generated sources */
    @get:Input
    abstract val rootOutputPath: Property<String>

    /** Default package where the generated DTOs will be placed. Default: solutions.sulfura.hyperkit.dtos */
    @get:Input
    abstract val defaultOutputPackage: Property<String>

    /** Velocity template used for DTO code generation */
    @get:Input
    abstract val templatePath: Property<String>

    @get:Classpath
    abstract val spoonSourcesClasspath: ConfigurableFileCollection

    @get:Classpath
    abstract val workerClasspath: ConfigurableFileCollection

    @TaskAction
    fun generate() {

        logger.info("Worker classpath: ${workerClasspath.files.joinToString(", ")}")

        val filteredClassPath = spoonSourcesClasspath.files.filter {

            val exclude = it.name.startsWith("ecj-") && it.path.contains("org.eclipse.jdt")

            if (exclude) {
                logger.warn("The sources classpath contains an ecj compiler artifact `${it.absolutePath}`. It will be excluded from the classpath to avoid conflicts with spoon's own ecj compiler version")
            }
            return@filter !exclude
        }

        // Configure classloader isolation to avoid errors due to conflicts with other plugins when processing the input sources with Spoon
        val workQueue: WorkQueue = workerExecutor.classLoaderIsolation {
            classpath.from(filteredClassPath)
            classpath.from(workerClasspath)
        }

        workQueue.submit(DtoGeneratorWorkAction::class.java) {
            absoluteInputPaths.set(this@GenerateDtosTask.inputFiles.map { it.absolutePath }.toSet())
            rootOutputPath.set(this@GenerateDtosTask.rootOutputPath)
            defaultOutputPackage.set(this@GenerateDtosTask.defaultOutputPackage)
            templatePath.set(this@GenerateDtosTask.templatePath)
            spoonSourcesClasspath.set(this@GenerateDtosTask.spoonSourcesClasspath.files.map { it.absolutePath }
                .toSet() + workerClasspath.files.map { it.absolutePath }.toSet())
        }

    }
}
