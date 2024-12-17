package solutions.y2024

import helpers.Coordinates2DLong
import helpers.Vector

class Day13 : PuzzleDay(2024) {
    private val buttonExtractRegex = """Button [A|B]: X\+(\d+), Y\+(\d+)""".toRegex()
    private val prizeExtractRegex = """Prize: X=(\d+), Y=(\d+)""".toRegex()

    private val clawMachines = readFileList.filter { it.isNotBlank() }.windowed(3, 3).map { gameInput ->
        val aVector = buttonExtractRegex.find(gameInput[0])?.groupValues?.drop(1)?.toVector()!!
        val bVector = buttonExtractRegex.find(gameInput[1])?.groupValues?.drop(1)?.toVector()!!
        val (xPrize, yPrize) = prizeExtractRegex.find(gameInput[2])?.groupValues?.drop(1)!!
        ClawMachine(aVector, bVector, Coordinates2DLong(xPrize.toLong(), yPrize.toLong()))
    }

    private fun List<String>.toVector(): Vector = Coordinates2DLong(0, 0).vector(
        Coordinates2DLong(x = this[0].toLong(), y = this[1].toLong())
    )

    private data class ClawMachine(val aVector: Vector, val bVector: Vector, val prize: Coordinates2DLong)

    private fun solveEqSystem(
        xComponentA: Int,
        yComponentA: Int,
        xComponentB: Int,
        yComponentB: Int,
        prize: Coordinates2DLong
    ): Pair<Long, Long>? {
        val determinant = xComponentA * yComponentB - yComponentA * xComponentB
        if (determinant == 0) return null

        val a = (prize.x * yComponentB - prize.y * xComponentB) / determinant
        val b = (prize.y * xComponentA - prize.x * yComponentA) / determinant

        return (a to b)
            .takeIf { a * xComponentA + b * xComponentB == prize.x && a * yComponentA + b * yComponentB == prize.y }
    }

    private fun List<ClawMachine>.computeMachines(): Long = map { machine ->
        val (_, _, xComponentA, yComponentA) = machine.aVector
        val (_, _, xComponentB, yComponentB) = machine.bVector

        solveEqSystem(xComponentA, yComponentA, xComponentB, yComponentB, machine.prize) ?: (0L to 0L)
    }.sumOf { (a, b) -> a * 3 + b * 1 }

    override fun part1(): Long = clawMachines.computeMachines()

    override fun part2(): Long = clawMachines
        .map {
            it.copy(
                prize = Coordinates2DLong(
                    x = it.prize.x + unitConversionDelta,
                    y = it.prize.y + unitConversionDelta
                )
            )
        }.computeMachines()

    private val unitConversionDelta = 10000000000000

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day13().run()
        }
    }
}
