import algorithms.AdaptiveLargeNeighborhoodSearch
import algorithms.Algorithm
import classes.Result
import classes.World
import com.google.gson.Gson
import operators.OneInsert
import operators.Operator
import operators.greedy.InsertBest
import operators.greedy.OptimizeVehicle
import operators.kOperator.InsertK
import utils.calculateCost
import utils.parseInput
import java.io.File
import kotlin.system.measureTimeMillis


class RunInstance {

    fun runInstanceOneOperator(
        algorithm: Algorithm,
        operator: Operator,
        name: String,
    ): MutableList<Result> {
        val calls = listOf(7, 18, 35, 80, 130, 300)
        val vehicles = listOf(3, 5, 7, 20, 40, 90)
        val listResult = mutableListOf<Result>()
        println(name)
        for ((i, v) in calls.withIndex()) {

            val world: World = parseInput("src/main/resources/Call_${v}_Vehicle_${vehicles[i]}.txt")
            val initialSolution = createWorstCase(world)
            val initialCost = calculateCost(initialSolution, world)
            var bestSolution = initialSolution
            var average: Long = 0
            var time: Long = 0
            val nIterations = 10
            for (j in 0 until nIterations) {
                val incumbent: MutableList<Int>
                time += measureTimeMillis { incumbent = algorithm.runWithOneOperator(initialSolution, operator, world) }

                val incumbentCost = calculateCost(incumbent, world)
                average += incumbentCost
                if (incumbentCost < calculateCost(bestSolution, world)) {
                    bestSolution = incumbent
                }

            }
            val bestCost = calculateCost(bestSolution, world)
            val improvement = 100 * (initialCost - bestCost) / initialCost

            val result = Result("CALL $v AND VEHICLE ${vehicles[i]}",
                average / nIterations,
                bestCost,
                improvement,
                time / nIterations,
                bestSolution)

            listResult.add(result)
            println(result.toString())


        }

        return listResult

    }

    fun runInstanceOperatorSet(algorithm: Algorithm, operatorList: List<Operator>, name: String): MutableList<Result> {
        val calls = listOf(7, 18, 35, 80, 130, 300)
        val vehicles = listOf(3, 5, 7, 20, 40, 90)
        val listResult = mutableListOf<Result>()
        println(name)
        for ((i, v) in calls.withIndex()) {

            val world: World = parseInput("src/main/resources/Call_${v}_Vehicle_${vehicles[i]}.txt")
            val initialSolution = createWorstCase(world)
            val initialCost = calculateCost(initialSolution, world)
            var bestSolution = initialSolution
            var average: Long = 0
            var time: Long = 0
            val nIterations = 10
            for (j in 0 until nIterations) {
                val incumbent: MutableList<Int>
                time += measureTimeMillis { incumbent = algorithm.runSetOperator(initialSolution, operatorList, world) }

                val incumbentCost = calculateCost(incumbent, world)
                average += incumbentCost
                if (incumbentCost < calculateCost(bestSolution, world)) {
                    bestSolution = incumbent
                }

            }
            val bestCost = calculateCost(bestSolution, world)
            val improvement = 100 * (initialCost - bestCost) / initialCost

            val result = Result("CALL $v AND VEHICLE ${vehicles[i]}",
                average / nIterations,
                bestCost,
                improvement,
                time / nIterations,
                bestSolution)

            listResult.add(result)
            println(result.toString())


        }

        return listResult


    }


    fun examRun() {
        TODO("Needs implement exam run")
    }
}


fun main() {
    val gson = Gson()
    val solutionMap = mutableMapOf<String, Any>()
    val resultFile = File("results/result.json")

    //val simulatedAnnealingOneInsert = runInstance(::simulatedAnnealing, ::oneInsert, "Simulated Annealing-1-insert")
    //val simulatedAnnealingTwoExchange =runInstance(::simulatedAnnealing, ::twoExchange, "Simulated Annealing two-exchange")
    //val simulatedAnnealingThreeExchange = runInstance(::simulatedAnnealing, ::threeExchange, "Simulated Annealing three-exchange")
    //runInstance(::modifiedSimulatedAnnealing, ::oneInsert, "SA-new operators (equal weights)")
    //solutionMap["Local Search-1-insert"] = runInstance(::localSearch, ::oneInsert, "Local Search-1-insert")
    //solutionMap["Local two-exchange"] = runInstance(::localSearch, ::twoExchange, "Local two-exchange")
    //solutionMap["Local Search three-exchange"] =
    //    runInstance(::localSearch, ::threeExchange, "Local Search three-exchange")
    val operators: List<Operator> = listOf(InsertBest(), OptimizeVehicle(), InsertK(), OneInsert())
    RunInstance().runInstanceOperatorSet(AdaptiveLargeNeighborhoodSearch(), operators, "ALNS")
    //runInstance(::localSearch, ::kInsert, "Local Search kInsert")

    val jsonMap = gson.toJson(solutionMap)
    resultFile.writeText(jsonMap)
}