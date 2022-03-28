package operators

import classes.World
import utils.parseInput
import kotlin.random.Random

fun threePermutationAndOneInsert(currentSolution: MutableList<Int>, world: World) {
    val randomBoolean: Boolean = Random.nextDouble() < 0.7
    var callOne: Int
    var callTwo: Int
    var callThree: Int
    var randomIndex = Random.nextInt(currentSolution.size)

    if (randomIndex < world.calls.size - 1) {
        callOne = currentSolution[randomIndex]
        callTwo = currentSolution[randomIndex + 1]
        callThree = currentSolution[randomIndex + 2]
        while (randomIndex >= world.calls.size - 2 || callOne == 0 || callTwo == 0 || callThree == 0) {
            randomIndex = Random.nextInt(currentSolution.size)
            callOne = currentSolution[randomIndex]
            callTwo = currentSolution[randomIndex + 1]
            callThree = currentSolution[randomIndex + 2]
        }
        println("$callOne, $callTwo, $callThree")
    }
}

fun main() {
    val world = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    val initialSolution = mutableListOf(4, 4, 7, 7, 0, 2, 2, 0, 1, 5, 5, 3, 3, 1, 0, 6, 6)
    for (v in 0 until 10) {
        threePermutationAndOneInsert(initialSolution, world)
    }
}