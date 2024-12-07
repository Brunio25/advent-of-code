package solutions.y2024.day_06

import helpers.Coordinates2D
import helpers.Table
import helpers.Table.Companion.Direction
import helpers.Table.Companion.Direction.DOWN
import helpers.Table.Companion.Direction.LEFT
import helpers.Table.Companion.Direction.RIGHT
import helpers.Table.Companion.Direction.UP
import util.Day

class Day06 : Day() {
    private val board: Table<Char> = readFileList
        .map { it.toList() }
        .let { Table(it) }

    private val directionMapping = mapOf(
        '^' to UP,
        '<' to LEFT,
        '>' to RIGHT,
        'v' to DOWN
    )

    private val directionRotation: Map<Direction, Direction> = mapOf(
        UP to RIGHT,
        RIGHT to DOWN,
        DOWN to LEFT,
        LEFT to UP
    )

    private fun Table<Char>.findGuard(): Coordinates2D = directionMapping.keys.firstNotNullOf { findOrNull(it) }

    override fun part1(): Int {
        val visitedCells: MutableSet<Pair<Coordinates2D, Direction>> = mutableSetOf()
        var guardCoords = board.findGuard()
        var currentDirection: Direction = board[guardCoords].let { directionMapping[it]!! }

        while (board.inTable(guardCoords)) {
            visitedCells.add(guardCoords to currentDirection)
            val nextCoords = board.getNext(guardCoords, currentDirection)
            if (board.inTable(nextCoords) && board[nextCoords] == '#') {
                currentDirection = directionRotation[currentDirection]!!
            } else {
                guardCoords = nextCoords
            }
        }

        return visitedCells.map { it.first }.distinct().count()
    }

    override fun part2(): Int {
        val guardCoords = board.findGuard()
        val rows: MutableList<MutableList<Char>> = board.getRows().map { it.toMutableList() }.toMutableList()


        return rows.indices.flatMap { row -> rows[row].indices.map { Coordinates2D(it, row) } }
            .map { board.replaceAt(it, '#') }
            .count { hasLoop(it, guardCoords) }
    }

    private fun Table<Char>.replaceAt(coordinates: Coordinates2D, element: Char): Table<Char> = matrix.map { it.toMutableList() }.toMutableList()
        .apply { this[coordinates.y][coordinates.x] = element }
        .let { Table(it) }

    private fun hasLoop(board: Table<Char>, initialGuardCoords: Coordinates2D): Boolean {
        var guardCoords = initialGuardCoords // Dumb solution to handle obstacle on start position
        var currentDirection = UP // Dumb solution to handle obstacle on start position

        val visitedCells = mutableSetOf<Pair<Coordinates2D, Direction>>()

        while (board.inTable(guardCoords)) {
            if (guardCoords to currentDirection in visitedCells) return true

            visitedCells.add(guardCoords to currentDirection)

            val nextCoords = board.getNext(guardCoords, currentDirection)
            if (board.inTable(nextCoords) && board[nextCoords] == '#') {
                currentDirection = directionRotation[currentDirection]!!
            } else {
                guardCoords = nextCoords
            }
        }
        return false
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day06().run()
        }
    }
}
