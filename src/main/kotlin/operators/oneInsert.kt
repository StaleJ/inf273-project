package operators

import classes.Vehicle
import classes.World
import operators.kOperator.createVehicleMap
import operators.kOperator.insertCallInVehicle
import operators.kOperator.removeCallFromVehicle
import kotlin.random.Random


fun oneInsert(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
    val randomBoolean: Boolean = Random.nextDouble() < 0.05
    val vehicles: HashMap<Int, MutableList<Int>> = createVehicleMap(currentSolution, world)
    val randomCall: Int = if (randomBoolean) {
        world.calls.find { call -> call.size == world.calls.maxOf { maxCall -> maxCall.size } }!!.index + 1

    } else world.calls.random().index + 1
    val randomVehicle = canTakeCall(randomCall, world).random().id



    removeCallFromVehicle(randomCall, vehicles)
    insertCallInVehicle(randomCall, randomVehicle, vehicles)



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




