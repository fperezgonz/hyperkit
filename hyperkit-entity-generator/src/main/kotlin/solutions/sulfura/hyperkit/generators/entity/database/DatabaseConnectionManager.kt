package solutions.sulfura.hyperkit.generators.entity.database

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/**
 * Manages database connections for the entity generator
 */
@Component
class DatabaseConnectionManager {

    private val logger = LoggerFactory.getLogger(DatabaseConnectionManager::class.java)

    /**
     * Establishes a connection to the database using the configured properties
     */
    fun getConnection(config: ConnectionProperties): Connection {

        Class.forName(config.databaseDriver)

        val connectionProps = Properties()

        if (config.username != null) {
            connectionProps.setProperty("user", config.username)
        }
        if (config.password != null) {
            connectionProps.setProperty("password", config.password)
        }

        var databaseUrl = config.url;

        if (config.databaseDriver.contains("hsqldb", ignoreCase = true)
            && databaseUrl.contains("hsqldb", ignoreCase = true)
            && !databaseUrl.contains("shutdown=true", ignoreCase = true)
        ) {
            databaseUrl += ";shutdown=true"
        }

        return DriverManager.getConnection(databaseUrl, connectionProps)

    }

}

data class ConnectionProperties(
    val url: String,
    val username: String?,
    val password: String?,
    val databaseDriver: String
)