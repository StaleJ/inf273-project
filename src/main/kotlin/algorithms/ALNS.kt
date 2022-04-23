package algorithms

import classes.World
import operators.Operator
import operators.greedy.InsertBest
import operators.kOperator.InsertK
import java.util.OptionalDouble

fun ALNS(
    initialSolution: MutableList<Int>,
    world: World
): MutableList<Int> {
    val op1: Pair<Operator, Double> = Pair(InsertK(), 33.0)
    val op2: Pair<Operator, Double> = Pair(InsertBest(), 33.0)


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