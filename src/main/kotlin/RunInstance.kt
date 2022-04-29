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
import java.nio.file.Files
import java.nio.file.Paths
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

    fun runInstanceOperatorSet(
        algorithm: Algorithm,
        operatorList: List<Operator>,
        name: String,
        files: MutableList<World>,
    ): MutableList<Result> {
        val calls = listOf(7, 18, 35, 80, 130, 300)
        val vehicles = listOf(3, 5, 7, 20, 40, 90)
        val listResult = mutableListOf<Result>()

        println(name)
        for ((i, v) in files.withIndex()) {
            val bestMap: HashMap<Int, Long> =
                hashMapOf(7 to 1134176, 18 to 2922974, 35 to 8947883, 80 to 15994246, 130 to 21467471, 300 to 44455891)
            val world: World = v
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
                println("run ${j + 1} time: ${TimeUnit.MILLISECONDS.toSeconds(instance)}s score: $incumbentCost")
                average += incumbentCost
                if (incumbentCost < calculateCost(bestSolution, world)) {
                    bestSolution = incumbent
                }

            }
            val bestCost = calculateCost(bestSolution, world)
            val improvement = 100 * (initialCost - bestCost) / initialCost

            val result = Result("Instance ${i + 1}# CALL ${world.calls.size} AND VEHICLE ${world.vehicles.size}",
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
    //val resultFile = File("results/result.json")
    val files: MutableList<World> = mutableListOf()
    File("resources/").walk().forEach {
        if (it.toString().endsWith(".txt"))
            files.add(parseInput(it.toString()))
    }
    Files.createDirectories(Paths.get("resources")) // Creates directory if not
    files.sortBy { it.calls.size }


    val operators: List<Operator> =
        listOf(InsertBest(), OptimizeVehicle(), InsertK(), OneInsert(), MoveDummy(), GreedyDummy())
    solutionMap["ALNS"] =
        RunInstance().runInstanceOperatorSet(AdaptiveLargeNeighborhoodSearch(), operators, "ALNS", files)

//    val jsonMap = gson.toJson(solutionMap)
//    resultFile.writeText(jsonMap)
}