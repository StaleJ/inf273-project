import algorithms.localSearch
import algorithms.simulatedAnnealing
import classes.World
import operators.oneReinsertAlgorithm
import utils.calculateCost
import utils.parseInput
import kotlin.system.measureTimeMillis


fun runInstance(
    algorithm: (s: MutableList<Int>, o: (s: MutableList<Int>, w: World) -> MutableList<Int>, w: World) -> MutableList<Int>,
    operator: (s: MutableList<Int>, w: World) -> MutableList<Int>,
    name: String
) {
    val calls = listOf(7, 18, 35, 80, 130, 300)
    val vehicles = listOf(3, 5, 7, 20, 40, 90)

    for ((i, v) in calls.withIndex()) {
        println("Instance name (e.g. CALL $v AND VEHICLE ${vehicles[i]})")
        val world: World = parseInput("src/main/resources/Call_${v}_Vehicle_${vehicles[i]}.txt")
        val initialSolution = createWorstCase(world)
        val initialCost = calculateCost(initialSolution, world)
        var bestSolution = initialSolution
        var average: Long = 0
        val time = measureTimeMillis {
            for (j in 0 until 10) {
                val incumbent = algorithm(initialSolution, operator, world)
                val incumbentCost = calculateCost(incumbent, world)
                average += incumbentCost
                if (incumbentCost < calculateCost(bestSolution, world)) {
                    bestSolution = incumbent
                }
            }
        }
        val bestCost = calculateCost(bestSolution, world)
        val improvement = 100 * (initialCost - bestCost) / initialCost
        println("              | Average objective | Best objective | Improvement (%) | Running time |")
        println("$name |    ${average / 10}   |    $bestCost    |    ${improvement}%    |    ${(time/1000)}ms\n")
        println(bestSolution)
        println()
    }

}


fun main() {
    runInstance(::localSearch, ::oneReinsertAlgorithm, "Local Search-1-insert")
    runInstance(::simulatedAnnealing, ::oneReinsertAlgorithm, "Simulated Annealing-1-insert")

}