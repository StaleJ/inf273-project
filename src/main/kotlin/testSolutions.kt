import classes.World
import utils.calculateCost
import utils.feasibilityCheck
import utils.parseInput

fun main() {
    val world = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    val list = listOf(4, 4, 2, 2, 0, 5, 5, 3, 3, 0, 7, 7, 0, 6, 6, 1, 1).toMutableList()
    testCall7Vehicle3(world,list)

}


fun testCall7Vehicle3(world: World, list: MutableList<Int>) {

    val isFeasible = feasibilityCheck(list, world)
    val cost = calculateCost(list, world)

    println(isFeasible.message)
    println(cost)
    println(isFeasible.isOk())
}