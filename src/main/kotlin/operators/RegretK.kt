package operators

import classes.World
import utils.calculateCost
import utils.parseInput
import java.util.Collections.max

class RegretK : Operator {
    override fun run(solution: MutableList<Int>, world: World): MutableList<Int> {
        return solution
    }

    fun runGreedyRemove(solution: MutableList<Int>, world: World, k: Int): MutableList<Int> {
        val callsToInsert: MutableList<Int> = mutableListOf()
        val before: Long = calculateCost(solution, world)
        for (i in 0 until k) {
            val tempMap: HashMap<Long, Int> = hashMapOf()

            for (j in 0 until solution.size) {
                val call = solution[j]
                if (call != 0) {
                    // Remove and calculate cost
                    val pickupCall = solution.indexOf(call)
                    solution.removeAt(pickupCall)
                    val deliverCall = solution.indexOf(call)
                    solution.removeAt(deliverCall)
                    val after: Long = calculateCost(solution, world)
                    val deltaCost = before - after
                    tempMap[deltaCost] = call
                    // Insert into solution again
                    solution.add(pickupCall, call)
                    solution.add(deliverCall, call)
                }
            }
            val maxDeltaCost: Long = max(tempMap.keys)!!
            val maxCall: Int = tempMap[maxDeltaCost]!!
            solution.removeAt(maxCall)
            solution.removeAt(maxCall)
            println(max(tempMap.keys))
            callsToInsert.add(tempMap[max(tempMap.keys)]!!)

        }
        println(callsToInsert)
        return solution
    }

}

fun main() {
    val world = parseInput("src/main/resources/Call_18_Vehicle_5.txt")
    val list = listOf(4,
        4,
        15,
        15,
        14,
        14,
        16,
        16,
        0,
        6,
        6,
        5,
        5,
        17,
        17,
        13,
        13,
        0,
        8,
        18,
        8,
        18,
        0,
        9,
        9,
        12,
        12,
        0,
        7,
        7,
        3,
        3,
        10,
        1,
        10,
        1,
        0,
        2,
        11,
        2,
        11).toMutableList()
    println(RegretK().runGreedyRemove(list, world, 4))
}
