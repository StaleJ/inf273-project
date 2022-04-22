package algorithms

import classes.World
import operators.greedy.insertBest
import operators.greedy.optimizeVehicle
import operators.oneInsert
import operators.threeExchange
import operators.twoExchange
import utils.RandomCollection
import utils.calculateCost
import utils.feasibilityCheck
import kotlin.math.ln
import kotlin.math.pow
import kotlin.random.Random

fun modifiedSimulatedAnnealing(
    initialSolution: MutableList<Int>,
    operator: (s: MutableList<Int>, w: World) -> MutableList<Int>, // Not in use
    world: World
): MutableList<Int> {
    val OP1 = ::oneInsert
    val OP2 = ::insertBest
    val OP3 = ::optimizeVehicle
    val finalTemperature = 0.1
    val P1 = 33.0
    val P2 = 33.0
    val P3 = 33.0
    var incumbent = initialSolution
    var bestSolution = initialSolution
    val deltaW = mutableListOf<Long>()

    val randomSelection =
        RandomCollection<(s: MutableList<Int>, w: World) -> MutableList<Int>>().add(P1, OP1).add(P2, OP2).add(P3, OP3)

    // Warm up
    for (w in 0 until 100) {
        val nextOperator = randomSelection.next()
        val newSolution = nextOperator(incumbent, world)
        val deltaE = calculateCost(newSolution, world) - calculateCost(incumbent, world)
        if (feasibilityCheck(newSolution, world).isOk() && deltaE < 0) {
            incumbent = newSolution
            if (calculateCost(incumbent, world) < calculateCost(bestSolution, world)) {
                bestSolution = incumbent
            }
        } else if (feasibilityCheck(newSolution, world).isOk()) {
            if (Random.nextDouble() < 0.8) {
                incumbent = newSolution
            }
            deltaW.add(deltaE)
        }
    }
    val deltaAverage = deltaW.average()
    val t0 = -deltaAverage / ln(0.8)
    val alpha = (finalTemperature / t0).pow(1 / 9900)
    var T = t0
    for (i in 0 until 9900) {
        val nextOperator = randomSelection.next()
        val newSolution = nextOperator(incumbent, world)
        val deltaE = calculateCost(newSolution, world) - calculateCost(incumbent, world)
        if (feasibilityCheck(newSolution, world).isOk() && deltaE < 0) {
            incumbent = newSolution
            if (calculateCost(incumbent, world) < calculateCost(bestSolution, world)) {
                bestSolution = incumbent
            }
        } else if (feasibilityCheck(newSolution, world).isOk() && Random.nextDouble() < Math.E.pow(-deltaE / T)) {
            incumbent = newSolution
        }
        T *= alpha

    }

    return bestSolution
}