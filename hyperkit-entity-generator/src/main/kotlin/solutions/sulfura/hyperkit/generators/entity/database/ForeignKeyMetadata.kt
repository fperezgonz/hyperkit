package solutions.sulfura.hyperkit.generators.entity.database

/**
 * Represents metadata for a foreign key relationship.
 */
data class ForeignKeyMetadata(
    /** The foreign key name */
    val name: String,
    /** The column name in the source table */
    val columnName: String,
    /** The field name in the entity class (camelCase) */
    val fieldName: String,
    /** The capitalized field name for getter/setter methods */
    val capitalizedName: String,
    val targetTableName: String,
    val targetColumnName: String,
    val targetClassName: String,
    val relationshipType: String,
    val relationshipOwner: Boolean,
)