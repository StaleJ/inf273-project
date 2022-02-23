package database

import PropertiesDB
import mu.KotlinLogging
import java.sql.Array
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private val logger = KotlinLogging.logger {}

data class Result(
    val id: Int,
    val name: String,
    val average: Int,
    val best: Int,
    val improvement: Int,
    val time: Double,
    val solution: Array
)

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
        val id = result.getInt("id")
        val name = result.getString("name")
        val average = result.getInt("average")
        val best = result.getInt("best")
        val improvement = result.getInt("improvement")
        val time = result.getDouble("time")
        val solution = result.getArray("solution")
        results.add(Result(id, name, average, best, improvement, time, solution))
    }
    insertResultToDB(7, 3, results[0])
}

fun insertResultToDB(call: Int, vehicle: Int, result: Result) {
    val connection = getConnection()



    val sql =
        "INSERT INTO call_${call}_vehicle_${vehicle}(name, average, best, improvement, time, solution) values ('${result.name}', ${result.average}, ${result.best}, ${result.improvement}, ${result.time}, '${result.solution}')"
    println(sql)
    val query = connection.prepareStatement(sql)
    try {
        query.executeQuery()
    } catch (e: SQLException) {
        val statement = "SELECT setval('the_primary_key_sequence', (SELECT MAX(id) FROM call_${call}_vehicle_${vehicle})+1)"
        val secondQuery = connection.prepareStatement(statement)
        secondQuery.executeQuery()
        query.executeQuery()
    }


}