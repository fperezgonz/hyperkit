package solutions.sulfura.hyperkit.generators.entity.database

/**
 * Represents metadata for a database table.
 */
data class TableMetadata(
    /** The catalog name */
    val catalog: String?,

    /** The schema name */
    val schema: String?,

    /** The table name */
    val tableName: String,

    /** The class name for the entity */
    val className: String,

    /** The package name for the entity */
    val packageName: String,

    /** The columns in the table */
    val columns: List<ColumnMetadata>,

    /** The foreign keys in the table */
    val foreignKeys: List<ForeignKeyMetadata>
) {
    /**
     * Gets the primary key columns.
     */
    val primaryKeyColumns: List<ColumnMetadata>
        get() = columns.filter { it.primaryKey }
}