package util

import helpers.extensions.measureTimeMillis
import java.io.File
import java.nio.file.FileSystems

abstract class Day(
    useCompleteInput: Boolean = true
) {
    private val completeInputFileName = "input.txt"
    private val testInputFileName = "test_input.txt"
    private val sep = FileSystems.getDefault().separator
    private val day = javaClass.simpleName.substringAfter("Day")

    private val inputPath =
        "src${sep}main${sep}kotlin${sep}solutions${sep}y2024${sep}day_$day${sep}inputs${sep}${if (useCompleteInput) completeInputFileName else testInputFileName}"

    val readFileList: List<String> = File(inputPath).useLines { it.toList() }

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
