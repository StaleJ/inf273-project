package classes

import org.postgresql.jdbc.PgArray


data class Result(
    val name: String,
    val average: Int,
    val best: Int,
    val improvement: Int,
    val time: Double,
    val solution: PgArray
)