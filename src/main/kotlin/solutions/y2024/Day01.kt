package solutions.y2024

import kotlin.math.abs

class Day01 : PuzzleDay(2024) {
    private val inputList by lazy {
        readFileList.map { line ->
            line.split("\\s+".toRegex())
                .let { it[0].toInt() to it[1].toInt() }
        }
    }

    override fun part1(): Int = inputList
        .let { list ->
            val (left, right) = list.unzip()
            left.sorted().zip(right.sorted())
        }
        .sumOf { abs(it.second - it.first) }

    override fun part2(): Int {
        val (left, right) = inputList.unzip()

        val countMap = right.groupBy { it }
            .mapValues { it.value.size }

        return left.sumOf { it * countMap.getOrDefault(it, 0) }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day01().run()
        }
    }
}
