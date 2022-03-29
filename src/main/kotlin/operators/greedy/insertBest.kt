package operators.greedy

import classes.Vehicle
import classes.World
import operators.canTakeCall

fun insertBest(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
    val vehicles: HashMap<Int, MutableList<Int>> = hashMapOf()
    val randomCall: Int = world.calls.random().index + 1
    val vehiclesCanTakeCall: List<Vehicle> = canTakeCall(randomCall, world)

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

}