import mu.KotlinLogging
import java.sql.Array
import java.sql.Connection
import java.sql.DriverManager

private val logger = KotlinLogging.logger {}

data class Result(
    val id: Int,
    val name: String,
    val average: Int,
    val best: Int,
    val improvement: Int,
    val time: Float,
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
        val time = result.getFloat("time")
        val solution = result.getArray("solution")
        results.add(Result(id, name, average, best, improvement, time, solution))
    }
    println(results)
}

fun insertResultToDB(call: Int, vehicle: Int, result: Result) {

}