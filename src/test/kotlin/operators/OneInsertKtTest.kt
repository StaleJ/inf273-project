package operators

import classes.Vehicle
import classes.World
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.parseInput

internal class OneInsertKtTest {
    private lateinit var world: World


    @BeforeEach
    fun setUp() {
        world = parseInput("src/main/resources/Call_7_Vehicle_3.txt")
    }

    @Test
    fun `test canTakeCall give correct vehicle`() {
        val testCall = 1
        val listVehicle: List<Vehicle> = canTakeCall(testCall, world)
        assertEquals(world.vehicles[2], listVehicle[0])
    }
}