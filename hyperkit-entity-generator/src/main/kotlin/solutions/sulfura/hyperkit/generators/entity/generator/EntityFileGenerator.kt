package solutions.sulfura.hyperkit.generators.entity.generator

import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Generates entity class files from table metadata.
 * This component is responsible for creating or updating entity class files
 * based on the table metadata and the configured overwrite settings.
 */
@Component
class EntityFileGenerator() {

    fun generateEntityFile(generatedEntity: GeneratedEntityData, outputPath: String): Boolean {

        // Create the output directory if it doesn't exist
        val packagePath = generatedEntity.packageName.replace('.', File.separatorChar)
        val outputDir = Paths.get(outputPath, packagePath)
        Files.createDirectories(outputDir)

        // Determine the output file path
        val outputFile = outputDir.resolve("${generatedEntity.entityName}.java")

        // Check if the file already exists
        if (Files.exists(outputFile)) {
            return updateExistingFile(outputFile, generatedEntity)
        } else {
            // Create a new file
            Files.write(outputFile, generatedEntity.entityCode.toByteArray())
            return true
        }
    }

    private fun updateExistingFile(
        filePath: Path,
        generatedEntityData: GeneratedEntityData
    ): Boolean {
        // Read the existing file
        val existingCode = Files.readString(filePath)

        // Merge the files
        val mergedCode = mergeEntityFiles(existingCode, generatedEntityData)
        Files.write(filePath, mergedCode.toByteArray())
        return true
    }

    /**
     * Merges an existing entity class file with new code
     *
     * @return the merged entity class code
     */
    private fun mergeEntityFiles(existingCode: String, generatedEntityData: GeneratedEntityData): String {
        var result = existingCode

        // Extract sections from new code
        val fieldsSection = extractSection(generatedEntityData.entityCode, "// <fields>", "// </fields>")
        val gettersSettersSection =
            extractSection(generatedEntityData.entityCode, "// <getters-setters>", "// </getters-setters>")
        val builderSection = extractSection(generatedEntityData.entityCode, "// <builder>", "// </builder>")

        // Replace sections based on overwrite settings
        if (fieldsSection != null) {
            result = replaceSection(result, "// <fields>", "// </fields>", fieldsSection)
        }

        if (gettersSettersSection != null) {
            result = replaceSection(result, "// <getters-setters>", "// </getters-setters>", gettersSettersSection)
        }

        if (builderSection != null) {
            result = replaceSection(result, "// <builder>", "// </builder>", builderSection)
        }

        return result
    }

    /**
     * Extracts a section from the code between the given start and end markers.
     *
     * @param code the code to extract from
     * @param startMarker the start marker
     * @param endMarker the end marker
     * @return the extracted section, or null if the section was not found
     */
    private fun extractSection(code: String, startMarker: String, endMarker: String): String? {
        val startIndex = code.indexOf(startMarker)
        if (startIndex == -1) return null

        val endIndex = code.indexOf(endMarker, startIndex)
        if (endIndex == -1) return null

        return code.substring(startIndex, endIndex + endMarker.length)
    }

    /**
     * Replaces a section in the code between the given start and end markers.
     *
     * @param code the code to replace in
     * @param startMarker the start marker
     * @param endMarker the end marker
     * @param replacement the replacement section
     * @return the code with the section replaced
     */
    private fun replaceSection(code: String, startMarker: String, endMarker: String, replacement: String): String {
        val startIndex = code.indexOf(startMarker)
        if (startIndex == -1) return code

        val endIndex = code.indexOf(endMarker, startIndex)
        if (endIndex == -1) return code

        return code.substring(0, startIndex) + replacement + code.substring(endIndex + endMarker.length)
    }
}