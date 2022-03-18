package operators

import classes.World
import kotlin.random.Random

fun threeExchange(currentSolution: MutableList<Int>, world: World): MutableList<Int> {

    val randomIndexOne = Random.nextInt(currentSolution.size)
    val randomIndexTwo = Random.nextInt(currentSolution.size)
    val randomIndexThree = Random.nextInt(currentSolution.size)
    val randomCallOne = currentSolution[randomIndexOne]
    val randomCallTwo = currentSolution[randomIndexTwo]
    val randomCallThree = currentSolution[randomIndexThree]


    when {
        randomCallOne == 0 && randomCallTwo == 0 && randomCallThree == 0 -> {
            return oneInsert(currentSolution, world)
        }
        randomCallOne != 0 && randomCallTwo != 0 && randomCallThree != 0 -> {
            val nextIndexOfCallTwo =
                when {
                    currentSolution.indexOf(randomCallOne) != randomIndexOne -> currentSolution.indexOf(randomCallOne)
                    else -> currentSolution.lastIndexOf(randomCallOne)
                }
            val nextIndexOfCallTwo =
                when {
                    currentSolution.indexOf(randomCallTwo) != randomIndexTwo -> currentSolution.indexOf(randomCallTwo)
                    else -> currentSolution.lastIndexOf(randomCallTwo)
                }
            currentSolution
        }

    }


}