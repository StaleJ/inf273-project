package operators

import classes.World
import kotlin.random.Random


class OneInsert : Operator {
    override fun run(solution: MutableList<Int>, world: World): MutableList<Int> {
        val vehicles: HashMap<Int, MutableList<Int>> = createVehicleMap(solution, world)
        val randomCall: Int = world.calls.random().index + 1
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

}








