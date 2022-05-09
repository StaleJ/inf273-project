package operators

import classes.Vehicle
import classes.World
import utils.calculateCost
import utils.feasibilityCheck
import utils.parseInput
import java.util.Collections.max

class RegretK : Operator {
    override fun run(solution: MutableList<Int>, world: World): MutableList<Int> {
        return runRegretK(solution, runRandomRemove(solution, world, 5), world, 5)
    }

    private fun runRandomRemove(solution: MutableList<Int>, world: World, k: Int): MutableList<Int> {
        val vehicles: HashMap<Int, MutableList<Int>> = createVehicleMap(solution, world)
        val callsToTake: MutableList<Int> = mutableListOf()
        val listCalls: MutableSet<Int> = mutableSetOf()


        for (i in 1..world.calls.size) {
            callsToTake.add(i)
        }

        for (i in 1 until k) {
            val randomCall: Int = callsToTake.random()
            callsToTake.removeIf { randomCall == it }
            listCalls.add(randomCall)
            removeCallFromVehicle(randomCall, vehicles)
        }
        return listCalls.toMutableList()
    }


    /**
     * Run greedy remove k element.
     *
     * @param solution solution we want to run greedy remove on.
     * @param world world that the solution lives in.
     * @param k number of calls to be greedy removed.
     * @return calls that is removed.
     */
    private fun runGreedyRemove(solution: MutableList<Int>, world: World, k: Int): MutableList<Int> {
        solution.map { it }
        val callsToInsert: MutableList<Int> = mutableListOf()
        val before: Long = calculateCost(solution, world)
        for (i in 0 until k) {
            val tempMap: HashMap<Long, Int> = hashMapOf()
            val copySolution = solution.map { it }
            for (call in copySolution) {
                if (call != 0) {
                    // Remove and calculate cost
                    val pickupCall = solution.indexOf(call)
                    solution.removeAt(pickupCall)
                    val deliverCall = solution.indexOf(call)
                    solution.removeAt(deliverCall)
                    val after: Long = calculateCost(solution, world)
                    val deltaCost = before - after
                    tempMap[deltaCost] = call
                    // Insert into solution again
                    solution.add(pickupCall, call)
                    solution.add(deliverCall, call)
                }
            }
            val maxDeltaCost: Long = max(tempMap.keys)!!
            val maxCall: Int = tempMap[maxDeltaCost]!!
            solution.remove(maxCall)
            solution.remove(maxCall)
            callsToInsert.add(tempMap[max(tempMap.keys)]!!)
        }
        for (call in callsToInsert) {
            solution.add(call)
            solution.add(call)
        }


        return callsToInsert
    }

    private fun insertBest(solution: MutableList<Int>, world: World, call: Int): MutableList<Int> {
        val vehicles: HashMap<Int, MutableList<Int>> = createVehicleMap(solution, world)
        val randomCall: Int = call
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
        return bestSolution
    }


    fun runRegretK(solution: MutableList<Int>, insertCalls: MutableList<Int>, world: World, k: Int): MutableList<Int> {

        val regretValueList: HashMap<Int, Long> = hashMapOf()
        // Loop over calls we want to insert
        for (call in insertCalls) {
            regretValueList[call] = 0
            val vehicles: HashMap<Int, MutableList<Int>> = createVehicleMap(solution, world)
            for (v in vehicles.values) {
                if (v.contains(call)) {
                    v.removeIf { it == call }
                    break
                }
            }
            val regretQueue: ArrayDeque<Long> = ArrayDeque()
            // Test alle possible position in the solution
            for ((id, car) in vehicles) {
                if (car.isEmpty()) { // there is only one place in the vehicle to put call
                    car.add(call)
                    car.add(call)
                    val tempSolution = createSolution(vehicles)
                    if (feasibilityCheck(tempSolution, world).isOk()) {
                        val cost: Long = calculateCost(tempSolution, world)
                        if (!regretQueue.contains(cost))
                            regretQueue.add(calculateCost(tempSolution, world))
                    }


                    car.remove(call)
                    car.remove(call)
                } else {
                    for (i in 0 until car.size - 1) {
                        for (j in i + 1 until car.size) {
                            car.add(i, call)
                            car.add(j, call)
                            val tempSolution = createSolution(vehicles)

                            if (feasibilityCheck(tempSolution, world).isOk()) {
                                val cost: Long = calculateCost(tempSolution, world)
                                if (!regretQueue.contains(cost))
                                    regretQueue.add(calculateCost(tempSolution, world))
                            }


                            car.remove(call)
                            car.remove(call)
                        }
                    }
                }
            }
            if (regretQueue.isEmpty())
                return solution
            regretQueue.sort()
            val best = regretQueue.removeFirst()
            while (regretQueue.isNotEmpty()) {
                regretValueList[call] = regretValueList[call]!! + regretQueue.removeFirst() - best
            }


        }
        var sol: MutableList<Int> = solution
        for (c in regretValueList.toSortedMap().keys.reversed()) {
            sol = insertBest(sol, world, c)
        }


        return sol

    }


}


fun main() {
    val world = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    val list = mutableListOf(0, 0, 1, 1, 0, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7)
    val regret = RegretK()
    println(regret.run(list, world))
}
