package operators

import classes.Vehicle
import classes.World
import mu.KotlinLogging
import utils.parseInput
import kotlin.random.Random


private val logger = KotlinLogging.logger {}

fun oneReinsert(currentSolution: MutableList<Int>): List<Int> {


    val from = Random.nextInt(0, currentSolution.size)
    val a = currentSolution[from]
    var sublistOne = currentSolution.subList(0, from)
    var sublistTwo = currentSolution.subList(from + 1, currentSolution.size)
    var tempList = sublistOne + sublistTwo
    println(tempList)
    println(a)
    val to = Random.nextInt(0, tempList.size)
    sublistOne = tempList.subList(0, to) as MutableList<Int>
    sublistTwo = tempList.subList(to, tempList.size) as MutableList<Int>
    sublistOne.add(a)
    tempList = sublistOne.plus(sublistTwo)

    println(tempList)
    return tempList
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



    sol = oneReinsert(sol as MutableList<Int>)


}