package algorithms

import classes.World

fun GAMF(
    initialSolution: MutableList<Int>,
    op: (s: MutableList<Int>, w: World) -> MutableList<Int>,
    world: World
): MutableList<Int> {
    var bestSolution = initialSolution
    var i = 0  // iterations since best solution found

    for (r in 0 until 25000) {
        if (i > 100) {
           TODO("Escape algo to bestSolution")
        }
        var incumbent = bestSolution
        var herustic = TODO("Select heuristic")



        i++
    }




    return bestSolution
}