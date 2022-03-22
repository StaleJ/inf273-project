package operators

import classes.World
import kotlin.random.Random

fun threeExchange(currentSolution: MutableList<Int>, world: World): MutableList<Int> {
    var randomIndexOne = Random.nextInt(currentSolution.size)
    var randomIndexTwo = Random.nextInt(currentSolution.size)
    var randomIndexThree = Random.nextInt(currentSolution.size)
    var randomCallOne = currentSolution[randomIndexOne]
    var randomCallTwo = currentSolution[randomIndexTwo]
    var randomCallThree = currentSolution[randomIndexThree]

    while (randomCallOne == randomCallTwo || randomCallOne == randomCallThree || randomCallTwo == randomIndexThree) {
        randomIndexOne = Random.nextInt(currentSolution.size)
        randomIndexTwo = Random.nextInt(currentSolution.size)
        randomIndexThree = Random.nextInt(currentSolution.size)
        randomCallOne = currentSolution[randomIndexOne]
        randomCallTwo = currentSolution[randomIndexTwo]
        randomCallThree = currentSolution[randomIndexThree]

    }



    when {
        randomCallOne == 0 && randomCallTwo == 0 && randomCallThree == 0 -> {
            return oneInsert(currentSolution, world)
        }
        randomCallOne != 0 && randomCallTwo != 0 && randomCallThree != 0 -> {
            val nextIndexOfCallOne = when {
                currentSolution.indexOf(randomCallOne) != randomIndexOne -> currentSolution.indexOf(randomCallOne)
                else -> currentSolution.lastIndexOf(randomCallOne)
            }
            val nextIndexOfCallTwo = when {
                currentSolution.indexOf(randomCallTwo) != randomIndexTwo -> currentSolution.indexOf(randomCallTwo)
                else -> currentSolution.lastIndexOf(randomCallTwo)
            }
            val nextIndexOfCallThree = when {
                currentSolution.indexOf(randomCallThree) != randomIndexThree -> currentSolution.indexOf(randomCallThree)
                else -> currentSolution.lastIndexOf(randomCallThree)
            }
            currentSolution[randomIndexOne] = randomCallTwo
            currentSolution[nextIndexOfCallOne] = randomCallTwo
            currentSolution[randomIndexTwo] = randomCallThree
            currentSolution[nextIndexOfCallTwo] = randomCallThree
            currentSolution[randomIndexThree] = randomCallOne
            currentSolution[nextIndexOfCallThree] = randomCallOne
            return currentSolution
        }
        else -> {
            var newSolution = oneInsert(currentSolution, world)
            newSolution = oneInsert(newSolution, world)
            return newSolution

        }

    }


}