package classes

import java.util.concurrent.TimeUnit


data class Result(
    val name: String,
    val average: Long,
    val best: Long,
    val improvement: Long,
    val time: Long,
    val solution: MutableList<Int>
) {
    override fun toString(): String {
        return "$name | average " + " ".repeat(average.toString().length - 7) + "| best " + " ".repeat(best.toString().length - 4) + "| improvement | time \n " + " ".repeat(
            name.length
        ) + "| $average | $best |" + " ".repeat(5) + "$improvement% " + " ".repeat(3) + " | ${TimeUnit.MILLISECONDS.toSeconds(time)} s\n $solution"
    }
}