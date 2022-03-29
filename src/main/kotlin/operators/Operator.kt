package operators

import classes.World

abstract class Operator<E> {

    open fun operator(currentSolution: MutableList<E>, world: World): MutableList<E> {
        TODO("Needs implementations")
    }

    open fun operator(world: World): MutableList<E> {
        TODO("Needs implementations")
    }
}