package solutions.day_02

import solutions.Day
import kotlin.math.absoluteValue

class Day02 : Day() {
    private val inputList by lazy { readFileList.map { line -> line.split("\\s+".toRegex()).map(String::toInt) } }

    override fun part1(): Int = inputList
        .filter { it.isSortedAsc() || it.isSortedDesc() }
        .count { it.zipWithNext().all { (a, b) -> (b - a).absoluteValue <= 3 } }

    private fun List<Int>.isSortedAsc() = zipWithNext().all { (a, b) -> a < b }
    private fun List<Int>.isSortedDesc() = zipWithNext().all { (a, b) -> a > b }

    override fun part2(): Int = inputList.count { report ->
        report.isSafe() ||
            report.indices.any {
                report.toMutableList()
                    .apply { removeAt(it) }
                    .isSafe()
            }
    }

    private fun List<Int>.isSafe() = zipWithNext { a, b -> a - b }
        .let { delta ->
            (delta.all { it < 0 } || delta.all { it > 0 })
                && delta.all { it.absoluteValue in 1..3 }
        }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day02().run()
        }
    }
}
