package operators

import classes.World
import createWorstCase
import utils.parseInput
import kotlin.math.max
import kotlin.random.Random

fun twoExchange(currentSolution: MutableList<Int>, world: World): MutableList<Int> {

    val randomIndexOne = Random.nextInt(currentSolution.size)
    val randomIndexTwo = Random.nextInt(currentSolution.size)
    val randomCallOne = currentSolution[randomIndexOne]
    val randomCallTwo = currentSolution[randomIndexTwo]


    when {
        randomCallOne == 0 && randomCallTwo == 0 -> { // if both are vehicles then do one insert
            return oneInsert(currentSolution, world)
        }
        randomCallOne != 0 && randomCallTwo != 0 -> { // if both are calls just swap calls on index
            val nextIndexOfCallOne =
                when {
                    currentSolution.indexOf(randomCallOne) != randomIndexOne -> currentSolution.indexOf(randomCallOne)
                    else -> currentSolution.lastIndexOf(
                        randomCallOne
                    )
                }
            val nextIndexOfCallTwo =
                when {
                    currentSolution.indexOf(randomCallTwo) != randomIndexTwo -> currentSolution.indexOf(randomCallTwo)
                    else -> currentSolution.lastIndexOf(
                        randomCallTwo
                    )
                }
            currentSolution[randomIndexOne] = randomCallTwo
            currentSolution[nextIndexOfCallOne] = randomCallTwo
            currentSolution[randomIndexTwo] = randomCallOne
            currentSolution[nextIndexOfCallTwo] = randomCallOne
            return currentSolution

        }
        else -> {
            val vehicles: HashMap<Int, MutableList<Int>> = hashMapOf()
            val randomCall = max(randomCallOne, randomCallTwo)
            val randomVehicle = world.vehicles.random().id

            for (i in 0 until world.vehicles.size + 1) {
                vehicles[i] = mutableListOf()
            }

            var i = 0
            for (c in currentSolution) {
                if (c != 0) vehicles[i]?.add(c) else i++
            }

            for (v in vehicles.values) {
                if (v.contains(randomCall)) {
                    v.removeIf { x -> x == randomCall }
                    break
                }
            }
            if (vehicles[randomVehicle] != null && vehicles[randomVehicle]!!.isEmpty()) {
                vehicles[randomVehicle]?.add(randomCall)
                vehicles[randomVehicle]?.add(randomCall)
            } else {
                var n = vehicles[randomVehicle]?.size?.let { Random.nextInt(0, it) }
                vehicles[randomVehicle]?.add(n!!, randomCall)
                n = vehicles[randomVehicle]?.size?.let { Random.nextInt(0, it) }
                vehicles[randomVehicle]?.add(n!!, randomCall)
            }
            val newSolution = mutableListOf<Int>()

            for (v in vehicles.values) {
                newSolution += v
                newSolution.add(0)
            }
            newSolution.removeLast()


            return newSolution

        }
    }


}
