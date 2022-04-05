package operators.greedy

import classes.World
import com.google.common.collect.Sets.cartesianProduct
import com.google.common.collect.Sets.combinations
import utils.parseInput


fun optimizeVehicle(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
    val vehicles: HashMap<Int, MutableList<Int>> = solutionToVehicles(currentSolution, world.vehicles.size)
    val eligibleVehicles: MutableList<Int> = mutableListOf()

    // eligiable vehicles are with more than 2 calls in them.
    for ((v, l) in vehicles) if (v != world.vehicles.size && l.size > 3) eligibleVehicles.add(v)


    return currentSolution
}


fun <T> Iterable<T>.permutations(length: Int? = null): Sequence<List<T>> =
    sequence {
        val pool = this@permutations as? List<T> ?: toList()
        val n = pool.size
        val r = length ?: n
        if(r > n) return@sequence
        val indices = IntArray(n) { it }
        val cycles = IntArray(r) { n - it }
        yield(List(r) { pool[indices[it]] })
        if(n == 0) return@sequence
        cyc@ while(true) {
            for(i in r-1 downTo 0) {
                cycles[i]--
                if(cycles[i] == 0) {
                    val temp = indices[i]
                    for(j in i until n-1) indices[j] = indices[j+1]
                    indices[n-1] = temp
                    cycles[i] = n - i
                } else {
                    val j = n - cycles[i]
                    indices[i] = indices[j].also { indices[j] = indices[i] }
                    yield(List(r) { pool[indices[it]] })
                    continue@cyc
                }
            }
            return@sequence
        }
    }


fun solutionToVehicles(solution: MutableList<Int>, numberOfVehicles: Int): HashMap<Int, MutableList<Int>> {
    val vehiclesMap: HashMap<Int, MutableList<Int>> = hashMapOf()
    var i = 0

    (0..numberOfVehicles).forEach { vehiclesMap[it] = mutableListOf() }
    solution.forEach { if (it == 0) i++ else vehiclesMap[i]?.add(it) }

    return vehiclesMap
}

fun main() {
    val world: World = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    val solution = mutableListOf(4, 4, 7, 7, 0, 2, 2, 0, 1, 5, 5, 3, 3, 1, 0, 6, 6)
    println(optimizeVehicle(solution, world))
    val sets = listOf(4,4,7,7)
    for (comb in sets.permutations(4)) println(comb)

}