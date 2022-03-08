package operators

import classes.Vehicle
import classes.World
import mu.KotlinLogging
import utils.parseInput
import kotlin.random.Random


private val logger = KotlinLogging.logger {}

fun oneReinsert(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
    val vehicles: HashMap<Int, MutableList<Int>> = hashMapOf()
    val randomCall = world.calls.random().index + 1
    val randomVehicle = world.vehicles.random().id

    for (i in 0 until 4) {
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

    println("random call: $randomCall")
    println("random vehicle: $randomVehicle")

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
    println(newSolution)

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

fun main() {
    val world = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    var sol = listOf(0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7)


    for (i in 0 until 10) {
        sol = oneReinsert(sol as MutableList<Int>, world)
    }

}