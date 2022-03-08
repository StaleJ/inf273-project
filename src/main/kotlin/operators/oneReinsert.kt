package operators

import classes.Vehicle
import classes.World
import createWorstCase
import mu.KotlinLogging
import utils.calculateCost
import utils.feasibilityCheck
import utils.parseInput
import kotlin.random.Random
import kotlin.system.measureTimeMillis


private val logger = KotlinLogging.logger {}

fun oneReinsertAlgorithm(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
    val vehicles: HashMap<Int, MutableList<Int>> = hashMapOf()
    val randomCall = world.calls.random().index + 1
    val randomVehicle = world.vehicles.random().id

    for (i in 0 until world.vehicles.size + 1) {
        vehicles[i] = mutableListOf()
    }

    var i = 0
    for (c in currentSolution) {
        if (c == 0) {
            i++
        } else {
            vehicles[i]?.add(c)
        }
    }


    for (v in vehicles.values) {
        if (v.contains(randomCall)) {
            v.removeIf { x -> x == randomCall }
            break
        }
    }
    if (vehicles[randomVehicle] != null && vehicles[randomVehicle]!!.isEmpty()) {
        vehicles[randomVehicle]?.add(randomCall)
        vehicles[randomVehicle]?.add(randomCall)
    } else {
        var n = vehicles[randomVehicle]?.size?.let { Random.nextInt(0, it) }
        vehicles[randomVehicle]?.add(n!!, randomCall)
        n = vehicles[randomVehicle]?.size?.let { Random.nextInt(0, it) }
        vehicles[randomVehicle]?.add(n!!, randomCall)
    }


    val newSolution = mutableListOf<Int>()

    for (v in vehicles.values) {
        newSolution += v
        newSolution.add(0)
    }
    newSolution.removeLast()


    return newSolution


}

internal fun canTakeCall(call: Int, world: World): List<Vehicle> {
    val validVehicles: MutableList<Vehicle> = emptyList<Vehicle>().toMutableList()
    for (vehicle in world.vehicles) {
        if (call - 1 in vehicle.validCalls)
            validVehicles.add(vehicle)
    }
    return validVehicles
}

fun oneReinsert(solution: MutableList<Int>, world: World): MutableList<Int> {
    var current = solution
    var bestSolution = current

    for (i in 0 until 100000) {
        current = oneReinsertAlgorithm(bestSolution, world)
        if (feasibilityCheck(current, world).isOk()) {
            if (calculateCost(current, world) < calculateCost(bestSolution, world)) {
                bestSolution = current
            }
        }
    }
    return bestSolution
}


fun main() {
    val calls = listOf(7, 18, 35, 80, 130, 300)
    val vehicles = listOf(3, 5, 7, 20, 40, 90)


    for ((i, v) in calls.withIndex()) {
        println("Instance name (e.g. CALL $v AND VEHICLE ${vehicles[i]})")
        val world = parseInput("src/main/resources/Call_${v}_Vehicle_${vehicles[i]}.txt")
        val initialCase = createWorstCase(world)
        val initialCost = calculateCost(initialCase, world)
        var best = initialCase
        var newReinsert = initialCase
        var average: Long = 0
        val timeInMillis = measureTimeMillis {
            for (j in 0 until 10) {
                newReinsert = oneReinsert(newReinsert, world)
                val randomCost = calculateCost(newReinsert, world)
                average += randomCost
                if (randomCost < calculateCost(best, world)) {
                    best = newReinsert
                }
            }
        }
        val bestCost = calculateCost(best, world)
        val improvement = 100 * (initialCost - bestCost) / initialCost
        println("              | Average objective | Best objective | Improvement (%) | Running time |")
        println("OneReInsert |    ${average / 10}   |    $bestCost    |    ${improvement}%    |    ${timeInMillis / 1000}s\n")
        println(best)
        println()
    }
}