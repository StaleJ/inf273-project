import algorithms.AdaptiveLargeNeighborhoodSearch
import algorithms.Algorithm
import classes.Result
import classes.World
import com.google.gson.Gson
import operators.Escape.MoveDummy
import operators.OneInsert
import operators.Operator
import operators.greedy.GreedyDummy
import operators.greedy.InsertBest
import operators.greedy.OptimizeVehicle
import operators.kOperator.InsertK
import utils.calculateCost
import utils.parseInput
import java.io.File
import java.util.concurrent.TimeUnit
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
            val bestMap: HashMap<Int, Long> =
                hashMapOf(7 to 1134176, 18 to 2374420, 35 to 4940607, 80 to 10602358, 130 to 16812178, 300 to 36864922)
            val world: World = parseInput("src/main/resources/Call_${v}_Vehicle_${vehicles[i]}.txt")
            val initialSolution = createWorstCase(world)
            val initialCost = calculateCost(initialSolution, world)
            var bestSolution = initialSolution
            var average: Long = 0
            var time: Long = 0
            val nIterations = 10
            for (j in 0 until nIterations) {
                val incumbent: MutableList<Int>
                val instance =
                    measureTimeMillis { incumbent = algorithm.runSetOperator(initialSolution, operatorList, world) }
                time += instance
                val incumbentCost = calculateCost(incumbent, world)
                println("run ${j + 1} time: ${TimeUnit.MILLISECONDS.toSeconds(instance)}s score: $incumbentCost from best found = ${incumbentCost - bestMap[v]!!}")
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


    val operators: List<Operator> =
        listOf(InsertBest(), OptimizeVehicle(), InsertK(), OneInsert())
    solutionMap["ALNS"] = RunInstance().runInstanceOperatorSet(AdaptiveLargeNeighborhoodSearch(), operators, "ALNS")

    val jsonMap = gson.toJson(solutionMap)
    resultFile.writeText(jsonMap)
}