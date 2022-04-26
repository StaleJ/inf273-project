package operators.greedy

import classes.World
import operators.Operator

class GreedyDummy : Operator {
    override fun run(solution: MutableList<Int>, world: World): MutableList<Int> {
        val vehicle: HashMap<Int, MutableList<Int>> = createVehicleMap(solution, world)
        if (vehicle.values.last().isNotEmpty()) {
            var callInDummy: Int = vehicle.values.last()[0] - 1
            var biggestSize = world.calls[callInDummy].costNotTransport
            for (call in vehicle.values.last()) {
                val currentSize = world.calls[call - 1].costNotTransport
                if (currentSize < biggestSize) {
                    callInDummy = call - 1
                    biggestSize = currentSize
                }

            }
            callInDummy++

            removeCallFromVehicle(callInDummy, vehicle)
            insertCallInVehicle(callInDummy, canTakeCall(callInDummy, world).random().id, vehicle)
            val newSolution = mutableListOf<Int>()

            for (v in vehicle.values) {
                newSolution += v
                newSolution.add(0)
            }
            newSolution.removeLast()


            return newSolution
        }
        return solution
    }
}

fun main() {

}