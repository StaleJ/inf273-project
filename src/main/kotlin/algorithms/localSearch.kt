package algorithms

import classes.World
import utils.calculateCost
import utils.feasibilityCheck


fun localSearch(
    initialSolution: MutableList<Int>, operator: (s: MutableList<Int>, w: World) -> MutableList<Int>, world: World
): MutableList<Int> {
    var bestSolution = initialSolution
    for (i in 0 until 10000) {
        val newSolution = operator(bestSolution, world)
        if (feasibilityCheck(newSolution, world).isOk() && calculateCost(newSolution, world) < calculateCost(
                bestSolution, world
            )
        ) {
            bestSolution = newSolution
        }
    }
    return bestSolution
}