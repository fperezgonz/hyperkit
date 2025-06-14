package solutions.sulfura.hyperkit.generators.entity.database

/**
 * Represents metadata for a database column.
 */
data class ColumnMetadata(
    /** The column name in the database */
    val columnName: String,

    /** The field name in the entity class (camelCase) */
    val fieldName: String,

    /** The capitalized field name for getter/setter methods */
    val capitalizedName: String,

    /** The Java type for the field */
    val javaType: String,

    /** The SQL type code */
    val sqlType: Int,

    /** The SQL type name */
    val typeName: String,

    /** The column size */
    val columnSize: Int,

    /** The decimal digits (for numeric types) */
    val decimalDigits: Int,

    /** Whether the column is nullable */
    val nullable: Boolean,

    /** Whether the column is auto-increment */
    val autoIncrement: Boolean,

    /** Whether the column is part of the primary key */
    var primaryKey: Boolean,

    /** The JPA column annotation */
    val columnAnnotation: String
)