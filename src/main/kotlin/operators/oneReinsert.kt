package operators

import classes.Vehicle
import classes.World
import mu.KotlinLogging
import utils.parseInput
import kotlin.random.Random


private val logger = KotlinLogging.logger {}

fun oneReinsert(list: MutableList<Int>, world: World, map: MutableMap<Int, MutableList<Int>>) {

    val numberOfCalls = world.calls.size
    val randomElement: Int = Random.nextInt(1, numberOfCalls)


    val validVehicles = canTakeCall(randomElement, world)
    val randomVehicle = validVehicles.random().id + 1
    logger.info("Random element $randomElement")
    logger.info("Valid vehicles ${validVehicles.size}")
    logger.info("Random vehicle $randomVehicle")

    if (map[randomVehicle]?.isEmpty()!!) {  // If array is empty just insert into vehicle
        map[randomVehicle] = mutableListOf(randomElement, randomElement)
    } else {
        val numberOfCallsInCar = map[randomVehicle]?.size
        val randomIndexPickUp = Random.nextInt(0, numberOfCallsInCar!!)
        val randomIndexDeliver = Random.nextInt(0, numberOfCallsInCar + 1)
        logger.info("$randomIndexPickUp and $randomIndexDeliver")
        map[randomVehicle]?.add(randomIndexPickUp, randomElement)
        map[randomVehicle]?.add(randomIndexDeliver, randomElement)
    }
    logger.info { map }


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
    val sol = listOf(0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7)
    val solutionMap: MutableMap<Int, MutableList<Int>> = HashMap<Int, MutableList<Int>>().toMutableMap()
    for (v in 1..world.vehicles.size) {
        solutionMap[v] = emptyList<Int>().toMutableList()
    }

    logger.info { solutionMap }
    for (i in 0 until 2) {
        oneReinsert(sol as MutableList<Int>, world, solutionMap)
    }

}