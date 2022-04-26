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
        var randomSelection: RandomCollection<Operator> = RandomCollection()
        var currentSolution: MutableList<Int> = initialSolution
        var incumbentSolution: MutableList<Int> = initialSolution
        val operatorScoreMap: HashMap<Operator, Double> = hashMapOf()
        val deltaW: MutableList<Long> = mutableListOf()
        val finaTemperature = 0.1
        var sinceLastBestFoundSolution: Int = 0

        // Add equal weights to the operators
        for (operator in operators) {
            randomSelection.add(1.0, operator)
            operatorScoreMap[operator] = 1.0
        }

        // Warm up
        for (w in 0 until 100) {
            val operator: Operator = randomSelection.next()
            val newSolution = operator.run(incumbentSolution, world)
            val deltaE = calculateCost(newSolution, world) - calculateCost(incumbentSolution, world)
            if (feasibilityCheck(newSolution, world).isOk() && deltaE < 0) {
                incumbentSolution = newSolution
                operatorScoreMap[operator] = operatorScoreMap[operator]?.plus(1.0)!!
                if (calculateCost(incumbentSolution, world) < calculateCost(bestSolution, world)) {
                    bestSolution = incumbentSolution
                    operatorScoreMap[operator] = operatorScoreMap[operator]?.plus(2.0)!!
                    sinceLastBestFoundSolution = 0
                }
            } else if (feasibilityCheck(newSolution, world).isOk() && Random.nextDouble() < 0.8) {
                incumbentSolution = newSolution
            }
            deltaW.add(deltaE)
            sinceLastBestFoundSolution++
        }
        randomSelection = RandomCollection()
        operatorScoreMap.forEach { randomSelection.add(it.value, it.key) } // TODO: Optimize this
        for (operator in operators) {
            operatorScoreMap[operator] = 1.0
        }

        val deltaAverage = deltaW.average()
        val t0 = -deltaAverage / ln(0.8)
        val alpha = (finaTemperature / t0).pow(1 / 9900)
        var T = t0

        for (i in 1..9900) {

            // Escape algorithm
            if (sinceLastBestFoundSolution > 100) {
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
            val deltaE = calculateCost(incumbentSolution, world) - calculateCost(currentSolution, world)


            if (feasibilityCheck(incumbentSolution, world).isOk() && deltaE < 0) {
                currentSolution = incumbentSolution
                operatorScoreMap[nextOperator] = operatorScoreMap[nextOperator]?.plus(1.0)!!
                if (calculateCost(currentSolution, world) < calculateCost(bestSolution, world)) {
                    bestSolution = currentSolution
                    operatorScoreMap[nextOperator] = operatorScoreMap[nextOperator]?.plus(2.0)!!
                    sinceLastBestFoundSolution = 0
                }
            } else if (feasibilityCheck(incumbentSolution,
                    world).isOk() && Random.nextDouble() < Math.E.pow(-deltaE / T)
            ) {
                currentSolution = incumbentSolution
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

