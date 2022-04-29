package operators.greedy

import classes.Vehicle
import classes.World
import operators.Operator
import utils.calculateCost
import utils.feasibilityCheck

class GreedyDummy : Operator {
    override fun run(solution: MutableList<Int>, world: World): MutableList<Int> {
        val vehicle: HashMap<Int, MutableList<Int>> = createVehicleMap(solution, world)
        if (vehicle.values.last().isNotEmpty()) {
            var callInDummy: Int = vehicle.values.last()[0] - 1
            var biggestSize = world.calls[callInDummy].costNotTransport
            for (call in vehicle.values.last()) {
                val currentSize = world.calls[call - 1].costNotTransport
                if (currentSize > biggestSize) {
                    callInDummy = call - 1
                    biggestSize = currentSize
                }

            }
            callInDummy++
            val randomCall = callInDummy
            val vehiclesCanTakeCall: List<Vehicle> = canTakeCall(randomCall, world)
            val idOfVehiclesCanTakeCall: MutableList<Int> = mutableListOf()
            vehiclesCanTakeCall.forEach { v -> idOfVehiclesCanTakeCall.add(v.id) }
            removeCallFromVehicle(callInDummy, vehicle)
            var bestSolution = solution
            for ((id, car) in vehicle) {  // Loop through all cars
                if (id in idOfVehiclesCanTakeCall) {
                    if (car.isEmpty()) { // there is only one place in the vehicle to put call
                        car.add(randomCall)
                        car.add(randomCall)
                        val tempSolution = createSolution(vehicle)
                        if (feasibilityCheck(tempSolution, world).isOk() && calculateCost(tempSolution,
                                world) < calculateCost(
                                bestSolution,
                                world
                            )
                        ) {
                            bestSolution = tempSolution
                        }
                        car.remove(randomCall)
                        car.remove(randomCall)
                    } else {

                        for (k in 0 until car.size - 1) {
                            for (l in k + 1 until car.size) {
                                car.add(k, randomCall)
                                car.add(l, randomCall)
                                val tempSolution = createSolution(vehicle)
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
            return bestSolution
        }
        return solution
    }
}

fun main() {

}