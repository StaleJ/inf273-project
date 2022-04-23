package operators.greedy

import classes.Vehicle
import classes.World
import operators.Operator
import operators.canTakeCall
import utils.calculateCost
import utils.feasibilityCheck


class InsertBest: Operator {
    override fun run(solution: MutableList<Int>, world: World): MutableList<Int> {
        val vehicles: HashMap<Int, MutableList<Int>> = createVehicleMap(solution, world)
        val randomCall: Int = world.calls.random().index + 1
        val vehiclesCanTakeCall: List<Vehicle> = canTakeCall(randomCall, world)
        val idOfVehiclesCanTakeCall: MutableList<Int> = mutableListOf()
        vehiclesCanTakeCall.forEach { v -> idOfVehiclesCanTakeCall.add(v.id) }



        for (v in vehicles.values) {
            if (v.contains(randomCall)) {
                v.removeIf { x -> x == randomCall }
                break
            }
        }

        // Start trying to find best place to put call
        var bestSolution = solution
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
                    if (id != vehicles.size + 1) {
                        for (k in 0 until car.size - 1) {
                            for (l in k + 1 until car.size) {
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

}


