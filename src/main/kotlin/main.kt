import algorithms.modifiedSimulatedAnnealing
import algorithms.simulatedAnnealing
import classes.World
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import operators.oneInsert
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
        var time: Long = 0
        val nIterations = 10
        for (j in 0 until nIterations) {
            val incumbent: MutableList<Int>
            time += measureTimeMillis { incumbent = algorithm(initialSolution, operator, world) }

            val incumbentCost = calculateCost(incumbent, world)
            average += incumbentCost
            if (incumbentCost < calculateCost(bestSolution, world)) {
                bestSolution = incumbent
            }

        }
        val bestCost = calculateCost(bestSolution, world)
        val improvement = 100 * (initialCost - bestCost) / initialCost

        println("              | Average objective | Best objective | Improvement (%) | Running time |")
        println("$name |    ${average / nIterations}   |    $bestCost    |    ${improvement}%    |    ${time / nIterations}ms\n")
        println(bestSolution)
        println()
    }


}


fun main() {
    val gson = Gson()
    val gsonPretty = GsonBuilder().setPrettyPrinting().create()
    val solutionMap = mutableMapOf<String, Any>()

    //runInstance(::localSearch, ::optimizeVehicle, "Local Search-1-insert")
    //runInstance(::localSearch, ::twoExchange, "Local two-exchange")
    //runInstance(::localSearch, ::threeExchange, "Local Search three-exchange")
    //runInstance(::simulatedAnnealing, ::oneInsert, "Simulated Annealing-1-insert")
    //runInstance(::simulatedAnnealing, ::twoExchange, "Simulated Annealing two-exchange")
    //runInstance(::simulatedAnnealing, ::threeExchange, "Simulated Annealing three-exchange")
    runInstance(::modifiedSimulatedAnnealing, ::oneInsert, "SA-new operators (equal weights)")
    //val jsonMap = gsonPretty.toJson(solutionMap)
    //println(jsonMap)
}