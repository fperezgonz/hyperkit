package solutions.sulfura.hyperkit.generators.entity.database

import org.springframework.stereotype.Component
import solutions.sulfura.hyperkit.generators.entity.generator.EntityGeneratorConfig
import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.Types

/**
 * Collects metadata from the database schema.
 * This component is responsible for extracting information about tables, columns,
 * primary keys, and foreign keys from the database.
 */
@Component
class DatabaseMetadataCollector(private val connectionManager: DatabaseConnectionManager) {

    /**
     * Collects metadata for all tables matching the configured pattern.
     */
    fun collectTableMetadata(
        config: EntityGeneratorConfig,
    ): List<TableMetadata> {

        val connectionProperties = ConnectionProperties(
            config.databaseUrl,
            config.databaseUsername,
            config.databasePassword,
            config.databaseDriver
        )

        connectionManager.getConnection(connectionProperties).use { connection ->
            val tables = mutableListOf<TableMetadata>()
            val databaseMetaData = connection.metaData

            // Get tables
            val tablesResultSet = databaseMetaData.getTables(
                null,
                config.schemaPattern.ifEmpty { null },
                config.tableNamePattern,
                arrayOf("TABLE")
            )

            while (tablesResultSet.next()) {
                val catalog = tablesResultSet.getString("TABLE_CAT")
                val schema = tablesResultSet.getString("TABLE_SCHEM")
                val tableName = tablesResultSet.getString("TABLE_NAME")

                val className = tableName.toPascalCase()
                val packageName = config.basePackage
                val columns = collectColumnMetadata(connection, catalog, schema, tableName)
                val primaryKeys = collectPrimaryKeys(connection, catalog, schema, tableName)

                // Mark primary key columns
                columns.forEach { column ->
                    column.primaryKey = primaryKeys.contains(column.columnName)
                }

                val foreignKeys = collectForeignKeys(connection, catalog, schema, tableName)

                tables.add(
                    TableMetadata(
                        catalog = catalog,
                        schema = schema,
                        tableName = tableName,
                        className = className,
                        packageName = packageName,
                        columns = columns,
                        foreignKeys = foreignKeys
                    )
                )
            }

            return tables
        }
    }

    /**
     * Collects metadata for all columns in a table.
     *
     * @param connection the database connection
     * @param catalog the catalog name
     * @param schema the schema name
     * @param tableName the table name
     * @return a list of column metadata objects
     */
    private fun collectColumnMetadata(
        connection: Connection,
        catalog: String?,
        schema: String?,
        tableName: String
    ): List<ColumnMetadata> {
        val columns = mutableListOf<ColumnMetadata>()
        val databaseMetaData = connection.metaData

        // Get columns
        val columnsResultSet = databaseMetaData.getColumns(catalog, schema, tableName, null)

        while (columnsResultSet.next()) {
            val columnName = columnsResultSet.getString("COLUMN_NAME")
            val dataType = columnsResultSet.getInt("DATA_TYPE")
            val typeName = columnsResultSet.getString("TYPE_NAME")
            val columnSize = columnsResultSet.getInt("COLUMN_SIZE")
            val decimalDigits = columnsResultSet.getInt("DECIMAL_DIGITS")
            val nullable = columnsResultSet.getInt("NULLABLE") == DatabaseMetaData.columnNullable
            val isAutoIncrement = columnsResultSet.getString("IS_AUTOINCREMENT").equals("YES", ignoreCase = true)

            // Map SQL type to Java type
            val javaType = mapSqlTypeToJavaType(dataType, decimalDigits)

            // Generate field name (camelCase)
            val fieldName = columnName.toCamelCase()

            // Generate column annotation
            val columnAnnotation = generateColumnAnnotation(columnName, nullable)

            columns.add(
                ColumnMetadata(
                    columnName = columnName,
                    fieldName = fieldName,
                    capitalizedName = fieldName.capitalize(),
                    javaType = javaType,
                    sqlType = dataType,
                    typeName = typeName,
                    columnSize = columnSize,
                    decimalDigits = decimalDigits,
                    nullable = nullable,
                    autoIncrement = isAutoIncrement,
                    primaryKey = false, // Will be set later
                    columnAnnotation = columnAnnotation
                )
            )
        }

        return columns
    }

    /**
     * Collects primary key column names for a table
     */
    private fun collectPrimaryKeys(
        connection: Connection,
        catalog: String?,
        schema: String?,
        tableName: String
    ): Set<String> {
        val primaryKeys = mutableSetOf<String>()
        val databaseMetaData = connection.metaData

        // Get primary keys
        val primaryKeysResultSet = databaseMetaData.getPrimaryKeys(catalog, schema, tableName)

        while (primaryKeysResultSet.next()) {
            val columnName = primaryKeysResultSet.getString("COLUMN_NAME")
            primaryKeys.add(columnName)
        }

        return primaryKeys
    }

    /**
     * Collects foreign key metadata for a table
     */
    private fun collectForeignKeys(
        connection: Connection,
        catalog: String?,
        schema: String?,
        tableName: String
    ): List<ForeignKeyMetadata> {
        val foreignKeys = mutableListOf<ForeignKeyMetadata>()
        val databaseMetaData = connection.metaData

        // Get foreign keys
        val foreignKeysResultSet = databaseMetaData.getImportedKeys(catalog, schema, tableName)

        // Collect unique columns to detect OneToOne relationships
        val uniqueColumns = collectUniqueColumns(connection, catalog, schema, tableName)

        while (foreignKeysResultSet.next()) {
            val fkName = foreignKeysResultSet.getString("FK_NAME")
            val fkColumnName = foreignKeysResultSet.getString("FKCOLUMN_NAME")
            val pkTableName = foreignKeysResultSet.getString("PKTABLE_NAME")
            val pkColumnName = foreignKeysResultSet.getString("PKCOLUMN_NAME")

            // Determine the target class name
            val targetClassName = pkTableName.toPascalCase()

            // Generate field name (camelCase)
            val fieldName = pkTableName.toCamelCase()

            // Determine if this is a OneToOne relationship based on a unique constraint
            val isOneToOne = uniqueColumns.contains(fkColumnName)

            val relationshipType = if (isOneToOne) "ONE_TO_ONE" else "MANY_TO_ONE"

            foreignKeys.add(
                ForeignKeyMetadata(
                    name = fkName,
                    columnName = fkColumnName,
                    fieldName = fieldName,
                    capitalizedName = fieldName.capitalize(),
                    targetTableName = pkTableName,
                    targetColumnName = pkColumnName,
                    targetClassName = targetClassName,
                    relationshipType = relationshipType,
                    relationshipOwner = true
                )
            )
        }

        return foreignKeys
    }

    /**
     * Collects columns that are the only column in a unique constraint
     */
    private fun collectUniqueColumns(
        connection: Connection,
        catalog: String?,
        schema: String?,
        tableName: String
    ): Set<String> {
        val uniqueColumns = mutableSetOf<String>()
        val databaseMetaData = connection.metaData
        val indexName__columns = mutableMapOf<String, MutableList<String>>()

        // Get indexes
        val indexesResultSet = databaseMetaData.getIndexInfo(catalog, schema, tableName, true, false)

        while (indexesResultSet.next()) {
            val indexName = indexesResultSet.getString("INDEX_NAME")
            val columnName = indexesResultSet.getString("COLUMN_NAME")
            if (columnName != null) {
                indexName__columns.getOrPut(indexName) { mutableListOf() }.add(columnName)
            }
        }

        for ((_, columns) in indexName__columns) {

            if (columns.size > 1) {
                continue
            }

            uniqueColumns.add(columns.first())

        }

        return uniqueColumns
    }

    /**
     * Maps a SQL type to a Java type.
     *
     * @param sqlType the SQL type code
     * @param decimalDigits the decimal digits
     * @return the Java type name
     */
    private fun mapSqlTypeToJavaType(sqlType: Int, decimalDigits: Int): String {
        return when (sqlType) {
            Types.CHAR, Types.VARCHAR, Types.LONGVARCHAR, Types.NCHAR, Types.NVARCHAR, Types.LONGNVARCHAR -> "String"
            Types.NUMERIC, Types.DECIMAL -> {
                if (decimalDigits > 0) "BigDecimal" else "Long"
            }

            Types.BIT -> "Boolean"
            Types.BOOLEAN -> "Boolean"
            Types.TINYINT -> "Byte"
            Types.SMALLINT -> "Short"
            Types.INTEGER -> "Integer"
            Types.BIGINT -> "Long"
            Types.REAL -> "Float"
            Types.FLOAT, Types.DOUBLE -> "Double"
            Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY -> "byte[]"
            Types.DATE -> "LocalDate"
            Types.TIME -> "LocalTime"
            Types.TIMESTAMP -> "LocalDateTime"
            Types.TIMESTAMP_WITH_TIMEZONE -> "OffsetDateTime"
            Types.BLOB -> "Blob"
            Types.CLOB, Types.NCLOB -> "Clob"
            Types.ARRAY -> "Array"
            Types.STRUCT -> "Object"
            Types.REF -> "Object"
            Types.JAVA_OBJECT -> "Object"
            else -> "Object"
        }
    }

    private fun generateColumnAnnotation(columnName: String, nullable: Boolean): String {
        return "@Column(name = \"$columnName\"${if (!nullable) ", nullable = false" else ""})"
    }

    private fun String.toCamelCase(): String {

        val parts = this.split("_")

        var firstPart = parts[0]

        if (firstPart.uppercase() == firstPart) {
            firstPart = firstPart.lowercase()
        }

        firstPart = firstPart.deCapitalize()

        return firstPart + parts.drop(1)
            .joinToString("_")
            .toPascalCase()

    }

    private fun String.toPascalCase(): String {

        val parts = this.split("_")

        return parts.joinToString("") {

            var result: String = it

            if (result.uppercase() == result) {
                result = result.lowercase()
            }

            result.capitalize()

        }

    }

    private fun String.capitalize(): String {
        return if (this.isEmpty()) this else this[0].uppercase() + this.substring(1)
    }

    private fun String.deCapitalize(): String {
        return if (this.isEmpty()) this else this[0].lowercase() + this.substring(1)
    }

}
