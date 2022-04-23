package algorithms

import classes.World
import operators.Operator
import utils.calculateCost
import utils.feasibilityCheck

class LocalSearch: Algorithm {
    override fun runWithOneOperator(
        initialSolution: MutableList<Int>,
        operator: Operator,
        world: World
    ): MutableList<Int> {
        var bestSolution = initialSolution
        for (i in 0 until 10000) {
            val newSolution = operator.run(bestSolution, world)
            if (feasibilityCheck(newSolution, world).isOk() && calculateCost(newSolution, world) < calculateCost(
                    bestSolution, world
                )
            ) {
                bestSolution = newSolution
            }
        }
        return bestSolution
    }
}
