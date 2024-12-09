package solutions.y2024

import helpers.extensions.measureTimeMillis
import util.InputReader

sealed class PuzzleDay(
    year: Int,
) {
    private val day = javaClass.simpleName.substringAfter("Day")

    val readFileList: List<String> = InputReader.getResource(year, day).useLines { it.toList() }

    abstract fun part1(): Any
    abstract fun part2(): Any

    fun run() {
        val (p1Time, p1) = try {
            measureTimeMillis { part1() }
        } catch (e: NotImplementedError) {
            0 to "Not Implemented"
        }
        val (p2Time, p2) = try {
            measureTimeMillis { part2() }
        } catch (e: NotImplementedError) {
            0 to "Not Implemented"
        }

        val message = """
            Day $day:
                Part 1: $p1 | $p1Time ms
                Part 2: $p2 | $p2Time ms
        """.trimIndent()

        println(message)
    }
}
