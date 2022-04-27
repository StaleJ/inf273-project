package algorithms

import classes.World
import createWorstCase
import operators.Escape.MoveDummy
import operators.OneInsert
import operators.Operator
import operators.greedy.InsertBest
import operators.greedy.OptimizeVehicle
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
        var bestCost: Long = calculateCost(bestSolution, world)
        var randomSelection: RandomCollection<Operator> = RandomCollection()
        var currentSolution: MutableList<Int> = initialSolution
        var currentCost: Long = calculateCost(initialSolution, world)
        var incumbentSolution: MutableList<Int>
        val operatorScoreMap: HashMap<Operator, Double> = hashMapOf()
        val historyLength: Int = world.calls.size
        val historyMap: HashMap<Int, Long> = hashMapOf()
        var sinceLastBestFoundSolution = 0
        var I = 0
        var idleI = 0
        (0 until historyLength).forEach { historyMap[it] = currentCost }
        // Add equal weights to the operators
        for (operator in operators) {
            randomSelection.add(1.0, operator)
            operatorScoreMap[operator] = 1.0
        }


        for (i in 1..10000) {


            // Escape algorithm
            if (sinceLastBestFoundSolution > 74 ) {
                for (e in 0 until 20) {
                    val tempSolution = InsertK().run(currentSolution, world)
                    if (feasibilityCheck(tempSolution, world).isOk())
                        currentSolution = tempSolution
                }
                if (feasibilityCheck(currentSolution, world).isOk() && calculateCost(currentSolution,
                        world) < calculateCost(bestSolution, world)
                ) {
                    bestSolution = currentSolution
                    sinceLastBestFoundSolution = 0
                }

            }
            val nextOperator: Operator = randomSelection.next()
            incumbentSolution = nextOperator.run(currentSolution, world)
            val incumbentCost = calculateCost(incumbentSolution, world)
            if (feasibilityCheck(incumbentSolution, world).isOk()) {

                // Calculate the virtual beginning v:= I mod Lh
                val v: Int = I % historyLength
                if (incumbentCost < historyMap[v]!! || incumbentCost < currentCost) {
                    currentSolution = incumbentSolution
                    currentCost = incumbentCost
                    operatorScoreMap[nextOperator]?.plus(3)

                    if (currentCost < bestCost) {
                        bestCost = currentCost
                        bestSolution = currentSolution
                        operatorScoreMap[nextOperator]?.plus(10)

                    }
                }
                if (currentCost < historyMap[v]!!) {
                    historyMap[v] = currentCost
                }
            }


            // Update random selection score every 100 iteration
            if (i % 100 == 0) {
                randomSelection = RandomCollection()
                operatorScoreMap.forEach { randomSelection.add(it.value, it.key) } // TODO: Optimize this
                for (operator in operators) {
                    operatorScoreMap[operator] = 1.0
                }

            }
            I++
            sinceLastBestFoundSolution++
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

