package operators.greedy

import classes.World
import utils.calculateCost
import utils.feasibilityCheck
import utils.parseInput


fun optimizeVehicle(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
    val vehicles: HashMap<Int, MutableList<Int>> = solutionToVehicles(currentSolution, world.vehicles.size)
    val eligibleVehicles: MutableList<Int> = mutableListOf()
    var bestSolution = currentSolution

    // eligible vehicles are with more than 2 calls in them.
    for ((v, l) in vehicles) if (v != world.vehicles.size && l.size >= 4 && l.size <= 8) eligibleVehicles.add(v)
    if (eligibleVehicles.isNotEmpty()) {
        val randomVehicle = eligibleVehicles.random()
        val permuteRandomVehicle = vehicles[randomVehicle]?.permutations(vehicles[randomVehicle]?.size)
        for (p in permuteRandomVehicle!!.toSet()) {
            vehicles[randomVehicle] = p as MutableList<Int>
            val tempSolution = createSolution(vehicles)
            if (feasibilityCheck(tempSolution, world).isOk() && calculateCost(
                    tempSolution,
                    world
                ) < calculateCost(bestSolution, world)
            ) {
                bestSolution = tempSolution
            }
        }
    }


    return bestSolution
}


fun <T> Iterable<T>.permutations(length: Int? = null): Sequence<List<T>> =
    sequence {
        val pool = this@permutations as? List<T> ?: toList()
        val n = pool.size
        val r = length ?: n
        if (r > n) return@sequence
        val indices = IntArray(n) { it }
        val cycles = IntArray(r) { n - it }
        yield(List(r) { pool[indices[it]] })
        if (n == 0) return@sequence
        cyc@ while (true) {
            for (i in r - 1 downTo 0) {
                cycles[i]--
                if (cycles[i] == 0) {
                    val temp = indices[i]
                    for (j in i until n - 1) indices[j] = indices[j + 1]
                    indices[n - 1] = temp
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
    val solution = mutableListOf(7, 4, 7, 4, 0, 2, 2, 0, 1, 5, 5, 3, 3, 1, 0, 6, 6)
    println(optimizeVehicle(solution, world))


}