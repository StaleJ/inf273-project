package operators

import classes.Vehicle
import classes.World
import kotlin.random.Random


fun oneInsert(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
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
        if (call - 1 in vehicle.validCalls) validVehicles.add(vehicle)
    }
    return validVehicles
}




