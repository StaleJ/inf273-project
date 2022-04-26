package algorithms

import classes.World
import createWorstCase
import operators.OneInsert
import operators.Operator
import operators.kOperator.InsertK
import utils.RandomCollection
import utils.calculateCost
import utils.feasibilityCheck
import utils.parseInput


class AdaptiveLargeNeighborhoodSearch : Algorithm {

    override fun runSetOperator(
        initialSolution: MutableList<Int>,
        operators: List<Operator>,
        world: World,
    ): MutableList<Int> {
        var bestSolution: MutableList<Int> = initialSolution
        var randomSelection: RandomCollection<Operator> = RandomCollection()
        var currentSolution: MutableList<Int> = initialSolution
        var incumbentSolution: MutableList<Int> = initialSolution
        val operatorScoreMap: HashMap<Operator, Double> = hashMapOf()

        // Add equal weights to the operators
        for (operator in operators) {
            randomSelection.add(1.0, operator)
            operatorScoreMap[operator] = 1.0
        }

        for (i in 1..10000) {

            // Escape algorithm
            if (i > 100) {
                for (e in 0 until 20) {
                    val tempSolution = InsertK().run(currentSolution, world)
                    if (feasibilityCheck(tempSolution, world).isOk())
                    currentSolution = tempSolution
                }
                if (feasibilityCheck(currentSolution, world).isOk() && calculateCost(currentSolution,
                        world) < calculateCost(bestSolution, world)
                ) {
                    bestSolution = currentSolution
                }

            }
            val nextOperator: Operator = randomSelection.next()
            incumbentSolution = nextOperator.run(currentSolution, world)


            if (feasibilityCheck(incumbentSolution, world).isOk() && calculateCost(incumbentSolution,
                    world) < calculateCost(
                    bestSolution,
                    world)
            ) {
                bestSolution = incumbentSolution
                operatorScoreMap[nextOperator] = operatorScoreMap[nextOperator]?.plus(2.0)!!
            }

            if (feasibilityCheck(incumbentSolution, world).isOk() && calculateCost(incumbentSolution,
                    world) < calculateCost(currentSolution, world)
            ) {
                currentSolution = incumbentSolution
                operatorScoreMap[nextOperator] = operatorScoreMap[nextOperator]?.plus(1.0)!!
            }

            // Update random selection score every 100 iteration
            if (i % 100 == 0) {
                randomSelection = RandomCollection()
                operatorScoreMap.forEach { randomSelection.add(it.value, it.key) }

            }


        }

        return bestSolution

    }

}

fun main() {
    val algorithm: AdaptiveLargeNeighborhoodSearch = AdaptiveLargeNeighborhoodSearch()
    val world = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    val solution = createWorstCase(world)
    val operatorList = listOf(OneInsert(), InsertK())
    algorithm.runSetOperator(solution, operatorList, world)
}

