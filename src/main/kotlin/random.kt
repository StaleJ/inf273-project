import classes.World
import utils.calculateCost
import utils.feasibilityCheck


fun random(world: World): MutableList<Int> {
    val initialSolution = createWorstCase(world)
    var bestSolution = initialSolution

    for (i in 0 until 10000) {
        val current = randomSolution(world)
        if (feasibilityCheck(current, world).isOk()) {
            if (calculateCost(current, world) < calculateCost(bestSolution, world)) {
                bestSolution = current
            }
        }
    }

    return bestSolution

}

fun createWorstCase(world: World): MutableList<Int> {
    val numberOfCalls: Int = world.calls.size
    val numberOfVehicles: Int = world.vehicles.size
    val worstCase: MutableList<Int> = emptyList<Int>().toMutableList()

    for (i in 1..numberOfVehicles) {
        worstCase.add(0)
    }

    for (i in 1..numberOfCalls) {
        worstCase.add(i)
        worstCase.add(i)
    }

    return worstCase

}

private fun randomSolution(world: World): MutableList<Int> {
    val numberOfVehicle = world.vehicles.size
    val numberOfCalls = world.calls.size
    val list = emptyList<Int>().toMutableList()
    val solution: MutableList<Int> = emptyList<Int>().toMutableList()
    var tempList = emptyList<Int>().toMutableList()

    for (i in 0 until numberOfVehicle) {
        list.add(0)
    }

    for (i in 1..numberOfCalls) {
        list.add(i)
    }

    for (i in list.indices) {
        val randomElement = list.random()

        if (randomElement != 0) {
            tempList.add(randomElement)
            tempList.add(randomElement)
            list.removeIf { x -> tempList.contains(x) }
        } else {
            tempList.shuffle()
            solution.addAll(tempList)
            solution.add(0)
            list.remove(0)
            tempList = emptyList<Int>().toMutableList()
        }
    }

    if (tempList.isNotEmpty()) {
        tempList.shuffle()
        solution.addAll(tempList)
    }

    return solution
}





