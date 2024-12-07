package solutions.y2024.day_06

import helpers.Coordinates2D
import solutions.y2024.day_06.Table.Companion.Direction
import solutions.y2024.day_06.Table.Companion.Direction.DOWN
import solutions.y2024.day_06.Table.Companion.Direction.LEFT
import solutions.y2024.day_06.Table.Companion.Direction.LEFT_DOWN
import solutions.y2024.day_06.Table.Companion.Direction.LEFT_UP
import solutions.y2024.day_06.Table.Companion.Direction.RIGHT
import solutions.y2024.day_06.Table.Companion.Direction.RIGHT_DOWN
import solutions.y2024.day_06.Table.Companion.Direction.RIGHT_UP
import solutions.y2024.day_06.Table.Companion.Direction.UP
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

    private val directionRotation: Map<Char, Char> = mapOf(
        '^' to '>',
        '>' to 'v',
        'v' to '<',
        '<' to '^'
    )

    private fun Table<Char>.findGuard(): Coordinates2D = directionMapping.keys.firstNotNullOf { findOrNull(it) }
    private fun Table<Char>.rotateGuard(coordinates: Coordinates2D) {
        this[coordinates] = directionRotation[this[coordinates]]!!
    }

    private fun Table<Char>.moveGuard(origin: Coordinates2D, destination: Coordinates2D) {
        this[destination] = this[origin]
        this[origin] = 'X'
    }

    override fun part1(): Int {
        do {
            val coords = board.findGuard()
            val direction = board[coords].let { directionMapping.getOrElse(it) { throw NoSuchElementException() } }

            val nextElem = board.getNext(coords, direction)
            if (!board.inTable(nextElem)) break

            if (board[nextElem] == '#') {
                board.rotateGuard(coords)
                continue
            }

            board.moveGuard(coords, nextElem)
        } while (true)


        return board.count { it == 'X' } + 1
    }

    override fun part2(): Int {
        val originalGuardPos = Coordinates2D(4, 6)
        val regularPath = board.findAll { it == 'X' }
            .flatMap { coords -> listOf(UP.calc(coords), RIGHT.calc(coords), DOWN.calc(coords), LEFT.calc(coords), coords) }
            .distinct()
            .filter { board[it] == '#' }
            .filter { it != originalGuardPos }

        board.resetMutableTable()
        var counter = 0

        for (newObstacleCoords in regularPath) {
            val takenPath: MutableList<Pair<Coordinates2D, Direction>> = mutableListOf()

            board[newObstacleCoords] = '#'
            do {
                val coords = board.findGuard()
                val direction = board[coords].let { directionMapping.getOrElse(it) { throw NoSuchElementException() } }

                val nextElem = board.getNext(coords, direction)
                if (!board.inTable(nextElem)) break

                if (coords to direction in takenPath) {
                    counter++
                    break
                }

                if (board[nextElem] == '#') {
                    board.rotateGuard(coords)
                    continue
                }

                board.moveGuard(coords, nextElem)
                takenPath.addLast(coords to direction)
            } while (true)

            board.resetMutableTable()
        }

        return counter
    }

    private val unwantedCells: Set<Char> = setOf('#') + directionRotation.keys

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day06().run()
        }
    }
}

private class Table<T : Any>(private val matrix: List<List<T>>) {
    companion object {
        enum class Direction(val calc: (Coordinates2D) -> Coordinates2D) {
            UP({ (x, y) -> Coordinates2D(x, y - 1) }),
            DOWN({ (x, y) -> Coordinates2D(x, y + 1) }),
            LEFT({ (x, y) -> Coordinates2D(x - 1, y) }),
            RIGHT({ (x, y) -> Coordinates2D(x + 1, y) }),
            LEFT_UP({ (x, y) -> Coordinates2D(x - 1, y - 1) }),
            RIGHT_UP({ (x, y) -> Coordinates2D(x + 1, y - 1) }),
            LEFT_DOWN({ (x, y) -> Coordinates2D(x - 1, y + 1) }),
            RIGHT_DOWN({ (x, y) -> Coordinates2D(x + 1, y + 1) }),
        }
    }

    private lateinit var mutableMatrix: MutableList<MutableList<T>>

    init {
        resetMutableTable()
    }

    fun resetMutableTable() { mutableMatrix = matrix.map { it.toMutableList() }.toMutableList() }

    operator fun get(coordinates: Coordinates2D): T = mutableMatrix[coordinates.y][coordinates.x]

    operator fun set(coordinates: Coordinates2D, element: T) {
        mutableMatrix[coordinates.y][coordinates.x] = element
    }

    fun getRows(): List<List<T>> = matrix
    fun getMutableRows(): MutableList<MutableList<T>> = mutableMatrix

    fun count(predicate: (T) -> Boolean): Int = mutableMatrix.sumOf { it.count(predicate) }

    fun findAll(predicate: (T) -> Boolean): List<Coordinates2D> = mutableMatrix.flatMapIndexed { y, row ->
        row.withIndex().filter { (_, col) -> predicate(col) }.map { (x, _) -> Coordinates2D(x, y) }
    }

    fun swap(origin: Coordinates2D, destination: Coordinates2D) {
        val e1 = this[origin]
        this[origin] = this[destination]
        this[destination] = e1
    }

    fun find(element: T): Coordinates2D {
        for (r in mutableMatrix.indices) {
            mutableMatrix[r].indexOf(element).takeIf { it != -1 }
                ?.let { return Coordinates2D(it, r) }
        }

        throw NoSuchElementException()
    }

    fun findOrNull(element: T): Coordinates2D? = runCatching { find(element) }.getOrNull()

    fun fromPlus(from: Coordinates2D, direction: Direction, length: Int): Coordinates2D {
        var coords = from
        for (i in 1..length) {
            coords = direction.calc(coords)
        }

        return coords
    }

    fun getNext(from: Coordinates2D, direction: Direction): Coordinates2D = fromPlus(from, direction, 1)

    fun getFromTo(from: Coordinates2D, to: Coordinates2D, direction: Direction): List<T> =
        getRange(from, to, direction).map { (x, y) -> mutableMatrix[y][x] }

    private fun getRange(origin: Coordinates2D, destination: Coordinates2D, direction: Direction): List<Coordinates2D> {
        val (ox, oy) = origin
        val (dx, dy) = destination
        return when (direction) {
            UP -> oy.downTo(dy).map { Coordinates2D(ox, it) }
            DOWN -> oy.rangeTo(dy).map { Coordinates2D(ox, it) }
            LEFT -> ox.downTo(dx).map { Coordinates2D(it, oy) }
            RIGHT -> ox.rangeTo(dx).map { Coordinates2D(it, oy) }
            LEFT_UP -> getRange(origin, destination, LEFT).zip(getRange(origin, destination, UP)) { left, up -> Coordinates2D(left.x, up.y) }
            RIGHT_UP -> getRange(origin, destination, RIGHT).zip(getRange(origin, destination, UP)) { right, up -> Coordinates2D(right.x, up.y) }
            LEFT_DOWN -> getRange(origin, destination, LEFT).zip(getRange(origin, destination, DOWN)) { left, down -> Coordinates2D(left.x, down.y) }
            RIGHT_DOWN -> getRange(origin, destination, RIGHT).zip(getRange(origin, destination, DOWN)) { right, down -> Coordinates2D(right.x, down.y) }
        }
    }

    fun inTable(coordinates: Coordinates2D): Boolean =
        coordinates.y in mutableMatrix.indices && coordinates.x in mutableMatrix[0].indices
}
