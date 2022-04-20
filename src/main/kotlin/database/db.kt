package database

import PropertiesDB
import classes.Result
import mu.KotlinLogging
import org.postgresql.jdbc.PgArray
import java.sql.Connection
import java.sql.DriverManager

private val logger = KotlinLogging.logger {}


fun getConnection(): Connection {
    val propertiesDB = PropertiesDB()
    val connection = DriverManager.getConnection(propertiesDB.uri, propertiesDB.username, propertiesDB.password)
    logger.info { "Connection is: ${connection.isValid(0)}" }
    return connection

}



fun insertResultToDB(call: Int, vehicle: Int, result: Result) {
    val connection = getConnection()


    val sql =
        "UPDATE call_${call}_vehicle_${vehicle} SET average=${result.average}, best=${result.best}, improvement=${result.improvement}, time=${result.time}, solution='${result.solution}' WHERE name=${result.name}"
    val query = connection.prepareStatement(sql)
    query.execute()


}