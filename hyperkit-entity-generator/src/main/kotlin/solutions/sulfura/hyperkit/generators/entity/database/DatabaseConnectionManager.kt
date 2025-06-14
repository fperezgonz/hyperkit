package solutions.sulfura.hyperkit.generators.entity.database

import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/**
 * Manages database connections for the entity generator
 */
@Component
class DatabaseConnectionManager {

    /**
     * Establishes a connection to the database using the configured properties
     */
    fun getConnection(config: ConnectionProperties): Connection {

        Class.forName(config.databaseDriver)

        val connectionProps = Properties()
        connectionProps.setProperty("user", config.username)
        connectionProps.setProperty("password", config.password)

        return DriverManager.getConnection(config.url, connectionProps)

    }

}

data class ConnectionProperties(val url: String, val username: String, val password: String, val databaseDriver: String)