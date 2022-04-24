package algorithms

import classes.World
import operators.Operator
import operators.kOperator.InsertK
import utils.RandomCollection
import utils.calculateCost


class AdaptiveLargeNeighborhoodSearch : Algorithm {

    override fun runSetOperator(
        initialSolution: MutableList<Int>,
        operators: List<Operator>,
        world: World,
    ): MutableList<Int> {
        var bestSolution: MutableList<Int> = initialSolution
        val weight: Double = 100.0 / operators.size
        val randomSelection: RandomCollection<Operator> = RandomCollection()
        var currentSolution: MutableList<Int> = initialSolution
        var incumbentSolution: MutableList<Int> = initialSolution

        // Add equal weights to the operators
        for (operator in operators) {
            randomSelection.add(weight, operator)
        }

        for ((i, _) in (1..25000).withIndex()) {

            // Escape algorithm
            if (i > 100) {
                for (e in 0 until 20) {
                    currentSolution = InsertK().run(currentSolution, world)
                }
                if (calculateCost(currentSolution, world) < calculateCost(bestSolution, world)) {
                    bestSolution = currentSolution
                }

            }

            incumbentSolution = randomSelection.next().run(currentSolution, world)
            if (calculateCost(incumbentSolution, world) < calculateCost(bestSolution, world)) {
                bestSolution = incumbentSolution
            }

            if (true) { // TODO: implement accept
                currentSolution = incumbentSolution
            }
            TODO("Implement update")

        }

        return bestSolution

    }

}

