package operators.greedy

import classes.World
import utils.parseInput


fun optimizeVehicle(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
    val vehicles: HashMap<Int, MutableList<Int>> = solutionToVehicles(currentSolution, world.vehicles.size)
    val eligibleVehicles: MutableList<Int> = mutableListOf()

    // eligiable vehicles are with more than 2 calls in them.
    for ((v, l) in vehicles) if (v != world.vehicles.size && l.size > 3) eligibleVehicles.add(v)


    return currentSolution
}

fun getUniquePermutations(array: Array<Int>): List<Pair<Int, Int>> {
    val permutationList = mutableListOf<Pair<Int, Int>>()
    for (i in array.toSet()) {
        for (j in array.toSet()) {
            permutationList.add((i to j))
        }
    }
    println(permutationList)
    return permutationList
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
    getUniquePermutations(arrayOf(1, 5, 5, 3, 3, 1))

}