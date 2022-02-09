import classes.World
import utils.calculateCost
import utils.feasibilityCheck


fun random(world: World): MutableList<Int> {
    val initialSolution = createWorstCase(world)
    var bestSolution = initialSolution

    for (i in 0 until 10000) {
        val current = initialSolution.shuffled()
        if (feasibilityCheck(current, world).isOk()) {
            if (calculateCost(current, world) < calculateCost(bestSolution, world)) {
                bestSolution = current as MutableList<Int>
            }
        }
    }

    return bestSolution

}

fun createWorstCase(world: World): MutableList<Int> {
    val numberOfCalls: Int = world.calls.size
    val numberOfVehicles: Int = world.vehicles.size
    val worstCase: MutableList<Int> = emptyList<Int>().toMutableList()

    for (i in 1..numberOfVehicles) {
        worstCase.add(0)
    }

    for (i in 1..numberOfCalls) {
        worstCase.add(i)
        worstCase.add(i)
    }

    return worstCase

}

