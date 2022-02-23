import utils.calculateCost
import utils.parseInput
import kotlin.system.measureTimeMillis

fun main() {

    val calls = listOf(7, 18, 35, 80, 130, 300)
    val vehicles = listOf(3, 5, 7, 20, 40, 90)


    for ((i, v) in calls.withIndex()) {
        println("Instance name (e.g. CALL $v AND VEHICLE ${vehicles[i]})")
        val world = parseInput("src/main/resources/Call_${v}_Vehicle_${vehicles[i]}.txt")
        val initialCase = createWorstCase(world)
        val initialCost = calculateCost(initialCase, world)
        var best = initialCase
        var average: Long = 0
        val timeInMillis = measureTimeMillis {
            for (j in 0 until 10) {
                val randomCase = random(world)
                val randomCost = calculateCost(randomCase, world)
                average += randomCost
                if (randomCost < calculateCost(best, world)) {
                    best = randomCase
                }
            }
        }
        val bestCost = calculateCost(best, world)
        val improvement = 100 * (initialCost - bestCost) / initialCost
        println("              | Average objective | Best objective | Improvement (%) | Running time |")
        println("Random search |    ${average / 10}   |    $bestCost    |    ${improvement}%    |    ${timeInMillis/1000}s\n")
        println(best)
        println()
    }


}