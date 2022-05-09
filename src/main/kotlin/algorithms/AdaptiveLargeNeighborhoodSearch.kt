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
import kotlin.math.ln
import kotlin.math.pow
import kotlin.random.Random


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
        var currentCost: Long = bestCost
        var incumbentSolution: MutableList<Int> = initialSolution
        var incumbentCost: Long = bestCost
        val operatorScoreMap: HashMap<Operator, Double> = hashMapOf()
        val deltaW: MutableList<Long> = mutableListOf()
        val finaTemperature = 0.1
        var sinceLastBestFoundSolution = 0


        // Add equal weights to the operators
        for (operator in operators) {
            randomSelection.add(1.0, operator)
            operatorScoreMap[operator] = 1.0
        }

        // Warm up
        for (w in 0 until 200) {
            val operator: Operator = randomSelection.next()
            val newSolution = operator.run(incumbentSolution, world)
            if (newSolution != incumbentSolution && feasibilityCheck(newSolution, world).isOk()) {
                val newCost = calculateCost(newSolution, world)
                val deltaE = newCost - incumbentCost
                if (deltaE < 0) {
                    incumbentSolution = newSolution
                    incumbentCost = newCost
                    operatorScoreMap[operator] = operatorScoreMap[operator]!! + 10
                    if (incumbentCost < bestCost) {
                        bestSolution = incumbentSolution
                        bestCost = incumbentCost
                        operatorScoreMap[operator] = operatorScoreMap[operator]!! + 15
                        sinceLastBestFoundSolution = 0
                    }
                } else if (Random.nextDouble() < 0.8) {
                    incumbentSolution = newSolution
                }

                deltaW.add(deltaE)
                sinceLastBestFoundSolution++
            }
        }
        randomSelection = RandomCollection()
        operatorScoreMap.forEach { randomSelection.add(it.value, it.key) } // TODO: Optimize this
        for (operator in operators) {
            operatorScoreMap[operator] = 1.0
        }

        val deltaAverage = deltaW.average()
        val t0 = -deltaAverage / ln(0.8)
        val alpha = (finaTemperature / t0).pow(1 / 19800)
        var T = t0

        for (i in 1..19800) {

            // Escape algorithm
            if (sinceLastBestFoundSolution > 300) {
                for (e in 0 until 20) {
                    val tempSolution = InsertK().run(currentSolution, world)
                    if (currentSolution != incumbentSolution && feasibilityCheck(tempSolution,
                            world).isOk()
                    ) currentSolution = tempSolution
                    currentCost = calculateCost(currentSolution, world)
                    if (feasibilityCheck(currentSolution, world).isOk() && currentCost < bestCost) {
                        bestSolution = currentSolution
                        bestCost = currentCost
                    }


                }
                sinceLastBestFoundSolution = 0

            }
            val nextOperator: Operator = randomSelection.next()
            incumbentSolution = nextOperator.run(currentSolution, world)

            if (feasibilityCheck(incumbentSolution, world).isOk()) {
                incumbentCost = calculateCost(incumbentSolution, world)
                val deltaE = incumbentCost - currentCost
                if (deltaE < 0) {
                    currentSolution = incumbentSolution
                    currentCost = incumbentCost
                    operatorScoreMap[nextOperator] = operatorScoreMap[nextOperator]!! + 10
                    if (currentCost < bestCost) {
                        bestSolution = currentSolution
                        bestCost = currentCost
                        operatorScoreMap[nextOperator] = operatorScoreMap[nextOperator]!! + 15
                        sinceLastBestFoundSolution = 0
                    }
                } else if (Random.nextDouble() < Math.E.pow(-deltaE / T)) {
                    currentSolution = incumbentSolution
                }


            }
            T *= alpha


            // Update random selection score every 100 iteration
            if (i % 100 == 0) {
                randomSelection = RandomCollection()
                operatorScoreMap.forEach { randomSelection.add(it.value, it.key) } // TODO: Optimize this
                for (operator in operators) {
                    operatorScoreMap[operator] = 1.0
                }

            }

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
