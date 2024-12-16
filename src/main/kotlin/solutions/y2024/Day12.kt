package solutions.y2024

import helpers.Coordinates2D
import helpers.Table
import helpers.Table.Companion.Direction.DOWN
import helpers.Table.Companion.Direction.LEFT
import helpers.Table.Companion.Direction.RIGHT
import helpers.Table.Companion.Direction.UP

class Day12 : PuzzleDay(2024) { // Inspired my someone else's solution, couldn't wrap my head around this
    private val table: Table<Char> = Table(readFileList.map { it.toList() })
    private val relevantDirections = setOf(UP, RIGHT, DOWN, LEFT)

    private fun Coordinates2D.isAdjacent(other: Coordinates2D): Boolean =
        relevantDirections.any { table.getNext(this, it) == other }

    private fun List<Coordinates2D>.perimeter(): Int = sumOf { coord -> 4 - count { coord.isAdjacent(it) } }

    private fun List<Coordinates2D>.joinAdjacent(): List<List<Coordinates2D>> = fold(emptyList()) { acc, coord ->
        val (currRegion, otherRegions) = acc.partition { region -> region.any { coord.isAdjacent(it) } }
        val new = currRegion.flatten() + coord
        otherRegions + listOf(new)
    }

    private fun costOfRegions(): Long = table.groupBy({ it.value }, { it.coordinates }).values
        .flatMap { it.joinAdjacent() }
        .sumOf { region -> (region.size * region.perimeter()).toLong() }

    override fun part1(): Long = costOfRegions()

    override fun part2(): Long = TODO()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day12().run()
        }
    }
}
