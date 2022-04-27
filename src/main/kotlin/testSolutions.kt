import classes.World
import utils.calculateCost
import utils.feasibilityCheck
import utils.parseInput

fun main() {
    val world = parseInput("src/main/resources/Call_18_Vehicle_5.txt")
    val list = listOf(4, 4, 15, 15, 11, 12, 11, 16, 16, 12,0,0,0,0).toMutableList()
    testCall7Vehicle3(world,list)

}


fun testCall7Vehicle3(world: World, list: MutableList<Int>) {

    val isFeasible = feasibilityCheck(list, world)
    val cost = calculateCost(list, world)

    println(isFeasible.message)
    println(cost)
    println(isFeasible.isOk())
}