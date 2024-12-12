package solutions.y2024

import helpers.Coordinates2D
import helpers.Table
import helpers.Table.Companion.Direction.DOWN
import helpers.Table.Companion.Direction.LEFT
import helpers.Table.Companion.Direction.RIGHT
import helpers.Table.Companion.Direction.UP
import helpers.TableCell
import helpers.graphs.TableGraph
import helpers.graphs.search.BfsGraphSearchAllStrategy
import helpers.graphs.search.GraphSearchStrategy

class Day10 : PuzzleDay(2024) {
    private val topographicMap: TableGraph<Int>
    private val trailheads: Set<Coordinates2D>
    private val comparator: Comparator<TableCell<Int>> = Comparator { c1, c2 ->
        if (c2.value - c1.value == 1) {
            if (c2.value == 9) 0
            else 1
        } else -1
    }

    init {
        val table = Table(readFileList.toIntMatrix())
        topographicMap = TableGraph(table, UP, RIGHT, LEFT, DOWN)
        trailheads = table.findAll { it == 0 }
    }

    private fun List<String>.toIntMatrix(): List<List<Int>> = map { line -> line.map { it.digitToInt() } }

    val bfsStrategyProvider: (TableCell<Int>) -> GraphSearchStrategy<TableCell<Int>> = {
        BfsGraphSearchAllStrategy(topographicMap, comparator, it)
    }

    override fun part1(): Int = trailheads
        .asSequence()
        .map { TableCell(it, topographicMap.table[it]) }
        .map { cell -> topographicMap.getSearch(bfsStrategyProvider(cell)) }
        .map { set -> set.map { it.last() } }
        .map { trailHead -> trailHead.distinctBy { it } }
        .sumOf { it.size }


    override fun part2(): Int = trailheads
        .asSequence()
        .map { TableCell(it, topographicMap.table[it]) }
        .map { cell -> topographicMap.getSearch(bfsStrategyProvider(cell)) }
        .map { set -> set.map { it.last() } }
        .sumOf { it.size }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day10().run()
        }
    }
}
