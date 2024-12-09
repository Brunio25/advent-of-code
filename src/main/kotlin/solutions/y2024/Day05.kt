package solutions.y2024

import helpers.Graph
import helpers.GraphBuilder
import helpers.extensions.middle
import helpers.extensions.split

class Day05 : PuzzleDay(2024) {
    private val rules: Graph<Int>
    private val updates: List<List<Int>>

    init {
        val rulesAndUpdates: Pair<List<String>, List<String>> = readFileList.split("")
        rules = rulesAndUpdates.first
            .map { line -> line.split("|").let { it[0].toInt() to it[1].toInt() } }
            .fold(GraphBuilder<Int>()) { acc, pair -> acc.addArc(pair.first, pair.second) }
            .toGraph()

        updates = rulesAndUpdates.second
            .map { line -> line.split(",").map { it.toInt() } }
    }

    private fun List<Int>.isValidUpdate(): Boolean {
        val transverse = rules.getTransverse()
        return runCatching {
            forEach { transverse.goTo(it) }
            true
        }.getOrDefault(false)
    }

    override fun part1(): Int = updates
        .filter { it.isValidUpdate() }
        .sumOf { it.middle() }

    override fun part2(): Int = updates
        .filter { !it.isValidUpdate() }
        .map { update ->
            update.correctUpdate()
        }
        .sumOf { it.middle() }


    private fun List<Int>.correctUpdate() = sortedWith { e1, e2 ->
        val transverse = rules.getTransverse().from(e1)
        runCatching {
            transverse.goTo(e2)
            -1
        }.getOrDefault(1)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day05().run()
        }
    }
}
