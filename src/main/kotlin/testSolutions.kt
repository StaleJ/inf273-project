import classes.World
import utils.calculateCost
import utils.feasibilityCheck
import utils.parseInput

fun main() {
    val world = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    val list = listOf(5, 5, 2, 2, 0, 7, 7, 0, 1, 3, 3, 1, 0, 6, 6, 4, 4).toMutableList()
    testCall7Vehicle3(world,list)

}


fun testCall7Vehicle3(world: World, list: MutableList<Int>) {

    val isFeasible = feasibilityCheck(list, world)
    val cost = calculateCost(list, world)

    println(isFeasible.message)
    println(cost)
    println(isFeasible.isOk())
}