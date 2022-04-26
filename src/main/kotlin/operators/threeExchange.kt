package operators

import classes.World
import kotlin.random.Random

class ThreeExchange : Operator {
    override fun run(solution: MutableList<Int>, world: World): MutableList<Int> {
        var randomIndexOne = Random.nextInt(solution.size)
        var randomIndexTwo = Random.nextInt(solution.size)
        var randomIndexThree = Random.nextInt(solution.size)
        var randomCallOne = solution[randomIndexOne]
        var randomCallTwo = solution[randomIndexTwo]
        var randomCallThree = solution[randomIndexThree]

        while (randomCallOne == randomCallTwo || randomCallOne == randomCallThree || randomCallTwo == randomIndexThree) {
            randomIndexOne = Random.nextInt(solution.size)
            randomIndexTwo = Random.nextInt(solution.size)
            randomIndexThree = Random.nextInt(solution.size)
            randomCallOne = solution[randomIndexOne]
            randomCallTwo = solution[randomIndexTwo]
            randomCallThree = solution[randomIndexThree]

        }



        when {
            randomCallOne == 0 && randomCallTwo == 0 && randomCallThree == 0 -> {
                return OneInsert().run(solution, world)
            }
            randomCallOne != 0 && randomCallTwo != 0 && randomCallThree != 0 -> {
                val nextIndexOfCallOne = when {
                    solution.indexOf(randomCallOne) != randomIndexOne -> solution.indexOf(randomCallOne)
                    else -> solution.lastIndexOf(randomCallOne)
                }
                val nextIndexOfCallTwo = when {
                    solution.indexOf(randomCallTwo) != randomIndexTwo -> solution.indexOf(randomCallTwo)
                    else -> solution.lastIndexOf(randomCallTwo)
                }
                val nextIndexOfCallThree = when {
                    solution.indexOf(randomCallThree) != randomIndexThree -> solution.indexOf(randomCallThree)
                    else -> solution.lastIndexOf(randomCallThree)
                }
                solution[randomIndexOne] = randomCallTwo
                solution[nextIndexOfCallOne] = randomCallTwo
                solution[randomIndexTwo] = randomCallThree
                solution[nextIndexOfCallTwo] = randomCallThree
                solution[randomIndexThree] = randomCallOne
                solution[nextIndexOfCallThree] = randomCallOne
                return solution
            }
            else -> {
                return OneInsert().run(solution, world)

            }

        }
    }

}
