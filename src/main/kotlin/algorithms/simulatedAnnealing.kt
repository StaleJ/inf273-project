package algorithms

import classes.World
import utils.calculateCost
import utils.feasibilityCheck
import kotlin.math.ln
import kotlin.math.pow
import kotlin.random.Random


fun simulatedAnnealing(
    initialSolution: MutableList<Int>,
    operator: (s: MutableList<Int>, w: World) -> MutableList<Int>,
    world: World,
    finalTemperature: Double = 0.1
): MutableList<Int> {
    var incumbent = initialSolution
    var bestSolution = initialSolution
    val deltaW = mutableListOf<Long>()

    // Warm up
    for (w in 0 until 100) {
        val newSolution = operator(incumbent, world)
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
        val newSolution = operator(incumbent, world)
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