package operators.kOperator

import classes.World
import operators.canTakeCall
import utils.parseInput
import kotlin.random.Random

fun kInsert(currentSolution: MutableList<Int>, world: World, k: Int): MutableList<Int> {

    val vehicles: HashMap<Int, MutableList<Int>> = createVehicleMap(currentSolution, world)
    val callsToTake: MutableList<Int> = mutableListOf()
    val listCalls: MutableSet<Int> = mutableSetOf()
    val newSolution: MutableList<Int> = mutableListOf()

    for (i in 1..world.calls.size) {
        callsToTake.add(i)
    }

    for (i in 1 until k) {
        val randomCall: Int = callsToTake.random()
        callsToTake.removeIf { randomCall == it }
        listCalls.add(randomCall)
        removeCallFromVehicle(randomCall, vehicles)
    }

    for (call in listCalls) {
        val vehicle = canTakeCall(call, world).random().id
        insertCallInVehicle(call, vehicle, vehicles)
    }

    for (v in vehicles.values) {
        newSolution += v
        newSolution.add(0)
    }
    newSolution.removeLast()

    return newSolution
}

fun removeCallFromVehicle(call: Int, vehicles: HashMap<Int, MutableList<Int>>) {
    for (v in vehicles.values) {
        if (v.contains(call)) {
            v.removeIf { x -> x == call }
            break
        }
    }

}

fun insertCallInVehicle(
    call: Int,
    vehicle: Int,
    vehicles: HashMap<Int, MutableList<Int>>,
) {

    if (vehicles[vehicle] != null && vehicles[vehicle]!!.isEmpty()) {
        vehicles[vehicle]?.add(call)
        vehicles[vehicle]?.add(call)
    } else {
        var n = vehicles[vehicle]?.size?.let { Random.nextInt(0, it) }
        vehicles[vehicle]?.add(n!!, call)
        n = vehicles[vehicle]?.size?.let { Random.nextInt(0, it) }
        vehicles[vehicle]?.add(n!!, call)
    }


}


fun createVehicleMap(currentSolution: MutableList<Int>, world: World): HashMap<Int, MutableList<Int>> {
    val vehicles: HashMap<Int, MutableList<Int>> = hashMapOf()
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
    return vehicles
}

fun main() {
    val world = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    var solution = mutableListOf(4, 4, 7, 7, 0, 2, 2, 0, 1, 5, 5, 3, 3, 1, 0, 6, 6)
    println(solution)
    solution = kInsert(solution, world, 7)
    println(solution)
}