package algorithms

import classes.World
import operators.Operator

interface Algorithm {

    fun runWithOneOperator(initialSolution: MutableList<Int>, operator: Operator, world: World): MutableList<Int> {
        return initialSolution
    }

    fun runSetOperator(initialSolution: MutableList<Int>, operators: List<Operator>, world: World): MutableList<Int> {
        return initialSolution
    }
}