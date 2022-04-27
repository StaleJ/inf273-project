package operators.Escape

import classes.World
import operators.Operator
import utils.parseInput
import kotlin.random.Random

class MoveDummy : Operator {
    override fun run(solution: MutableList<Int>, world: World): MutableList<Int> {
        val vehicles: HashMap<Int, MutableList<Int>> = createVehicleMap(solution, world)
        val newSolution: MutableList<Int> = mutableListOf()
        val k: Int = Random.nextInt(1, 4)

        for (i in 1 until k) {
            val randomCall: Int = world.calls.random().index + 1
            removeCallFromVehicle(randomCall, vehicles)
            insertCallInVehicle(randomCall, world.vehicles.size, vehicles)
        }

        for (v in vehicles.values) {
            newSolution += v
            newSolution.add(0)
        }
        newSolution.removeLast()


        return newSolution
    }

}

fun main() {
    val world = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    val solution = mutableListOf(4, 4, 2, 2, 0, 7, 7, 0, 1, 5, 5, 3, 3, 1, 0, 6, 6)
    print(MoveDummy().run(solution, world))
}