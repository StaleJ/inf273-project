package operators.kOperator

import classes.World
import operators.Operator
import operators.canTakeCall
import kotlin.random.Random


class InsertK : Operator {
    override fun run(solution: MutableList<Int>, world: World): MutableList<Int> {
        val vehicles: HashMap<Int, MutableList<Int>> = createVehicleMap(solution, world)
        val callsToTake: MutableList<Int> = mutableListOf()
        val listCalls: MutableSet<Int> = mutableSetOf()
        val newSolution: MutableList<Int> = mutableListOf()
        val k: Int = Random.nextInt(1, 7)

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
}
