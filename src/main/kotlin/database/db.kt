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

fun main() {
    val results = mutableListOf<Result>()


    val connection = getConnection()


    val sql = "SELECT * FROM call_7_vehicle_3"
    val query = connection.prepareStatement(sql)

    val result = query.executeQuery()

    while (result.next()) {
        val name = result.getString("name")
        val average = result.getInt("average")
        val best = result.getInt("best")
        val improvement = result.getInt("improvement")
        val time = result.getDouble("time")
        val solution = result.getArray("solution")
        results.add(Result(name, average, best, improvement, time, solution as PgArray))
    }
    insertResultToDB(7, 3, results[0])
}

fun insertResultToDB(call: Int, vehicle: Int, result: Result) {
    val connection = getConnection()


    val sql =
        "UPDATE call_${call}_vehicle_${vehicle} SET average=${result.average}, best=${result.best}, improvement=${result.improvement}, time=${result.time}, solution='${result.solution}' WHERE name=${result.name}"
    val query = connection.prepareStatement(sql)
    query.execute()


}