package operators

import classes.World
import kotlin.random.Random

interface Operator {

    fun run(solution: MutableList<Int>, world: World): MutableList<Int>

    fun removeCallFromVehicle(call: Int, vehicles: HashMap<Int, MutableList<Int>>) {
        for (v in vehicles.values) {
            if (v.contains(call)) {
                v.removeIf { x -> x == call }
                break
            }
        }

    }

    fun insertCallInVehicle(
        call: Int,
        vehicle: Int,
        vehicles: HashMap<Int, MutableList<Int>>,
    ) {

        if (vehicles[vehicle] != null && vehicles[vehicle]!!.isEmpty()) {
            vehicles[vehicle]?.add(call)
            vehicles[vehicle]?.add(call)
        } else {
            var n = vehicles[vehicle]?.size?.let { Random.nextInt(0, it) }
            vehicles[vehicle]?.add(n!!, call)
            n = vehicles[vehicle]?.size?.let { Random.nextInt(0, it) }
            vehicles[vehicle]?.add(n!!, call)
        }


    }


    fun createVehicleMap(currentSolution: MutableList<Int>, world: World): HashMap<Int, MutableList<Int>> {
        val vehicles: HashMap<Int, MutableList<Int>> = hashMapOf()
        for (i in 0 until world.vehicles.size + 1) {
            vehicles[i] = mutableListOf()
        }

        var i = 0
        for (c in currentSolution) {
            if (c == 0) {
                i++
            } else {
                vehicles[i]?.add(c)
            }
        }
        return vehicles
    }
}



