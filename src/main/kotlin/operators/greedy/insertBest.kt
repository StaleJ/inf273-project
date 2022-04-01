package operators.greedy

import classes.Vehicle
import classes.World
import operators.canTakeCall
import utils.calculateCost
import utils.feasibilityCheck

fun insertBest(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
    val vehicles: HashMap<Int, MutableList<Int>> = hashMapOf()
    val randomCall: Int = world.calls.random().index + 1
    val vehiclesCanTakeCall: List<Vehicle> = canTakeCall(randomCall, world)
    val idOfVehiclesCanTakeCall: MutableList<Int> = mutableListOf()
    vehiclesCanTakeCall.forEach { v -> idOfVehiclesCanTakeCall.add(v.id) }

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

    // Start trying to find best place to put call
    var bestSolution = currentSolution
    for ((id, car) in vehicles) {  // Loop through all cars
        if (id in idOfVehiclesCanTakeCall) {
            if (car.isEmpty()) { // there is only one place in the vehicle to put call
                car.add(randomCall)
                car.add(randomCall)
                val tempSolution = createSolution(vehicles)
                if (feasibilityCheck(tempSolution, world).isOk() && calculateCost(tempSolution, world) < calculateCost(
                        bestSolution,
                        world
                    )
                ) {
                    bestSolution = tempSolution
                }
                car.remove(randomCall)
                car.remove(randomCall)
            } else {
                if (id != vehicles.size) {
                    for (k in 0 until car.size - 1) {
                        for (l in 1 until car.size) {
                            car.add(k, randomCall)
                            car.add(l, randomCall)
                            val tempSolution = createSolution(vehicles)
                            if (feasibilityCheck(tempSolution, world).isOk() && calculateCost(
                                    tempSolution,
                                    world
                                ) < calculateCost(bestSolution, world)
                            ) {
                                bestSolution = tempSolution
                            }
                            car.remove(randomCall)
                            car.remove(randomCall)
                        }
                    }
                }
            }

        }
    }
    return bestSolution

}

fun createSolution(vehiclesMap: HashMap<Int, MutableList<Int>>): MutableList<Int> {
    val newSolution = mutableListOf<Int>()

    for (v in vehiclesMap.values) {
        newSolution += v
        newSolution.add(0)
    }
    newSolution.removeLast()


    return newSolution
}