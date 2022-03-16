package operators

import classes.World
import createWorstCase
import utils.parseInput
import kotlin.random.Random

fun twoExchange(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
    /*

    [0,0,0,1,1,2,2,3,3,4,4] = size = 11
    n = 7, m = 9
    l[7] = 3 og l[9] = 4
    [0,0,0,0,0,0,1,1,2,2,4,4,3,3]
    n = 0, m = 3
    l[0] = 0 og l[3] = 1
    [1,1,0,0,0,2,2,4,4,3,3]
    [1,1,0,2,2,0,0,4,4,3,3]
    [1,1,0,0,2,2,0,0,0,0,4,4,3,3] If randoms are intern move just move on index

    [1,1,0,2,2,3,3,0,4,4,0] Else move in pairs
    */

    val randomIndexOne = Random.nextInt(currentSolution.size)
    val randomIndexTwo = Random.nextInt(currentSolution.size)
    val randomCallOne = currentSolution[randomIndexOne]
    val randomCallTwo = currentSolution[randomIndexTwo]


    if (randomCallOne == 0 && randomCallTwo == 0) { // if both are vehicles then do nothing
        return currentSolution
    }

    if (randomCallOne != 0 && randomCallTwo != 0) { // if both are calls just swap calls on index
        val nextIndexOfCallOne =
            if (currentSolution.indexOf(randomCallOne) != randomIndexOne) currentSolution.indexOf(randomCallOne) else currentSolution.lastIndexOf(
                randomCallOne
            )
        val nextIndexOfCallTwo =
            if (currentSolution.indexOf(randomCallTwo) != randomIndexTwo) currentSolution.indexOf(randomCallTwo) else currentSolution.lastIndexOf(
                randomCallTwo
            )
        currentSolution[randomIndexOne] = randomCallTwo
        currentSolution[nextIndexOfCallOne] = randomCallTwo
        currentSolution[randomIndexTwo] = randomCallOne
        currentSolution[nextIndexOfCallTwo] = randomCallOne
    } else {
        // TODO: Fix when one call is 0
        val paddedSolution = mutableListOf<Int>()
        var car = -1

        for (call in currentSolution) {
            if (call != 0) {
                paddedSolution.add(call)
            } else {
                paddedSolution.add(call)
                paddedSolution.add(car)
                car -= 1
            }
        }


    }






    return currentSolution
}


fun main() {
    val world: World = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    val initialSolution = createWorstCase(world)
    println(twoExchange(initialSolution, world))
}