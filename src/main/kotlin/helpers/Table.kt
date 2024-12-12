package helpers

import helpers.Table.Companion.Direction.UP
import helpers.Table.Companion.Direction.DOWN
import helpers.Table.Companion.Direction.LEFT
import helpers.Table.Companion.Direction.RIGHT
import helpers.Table.Companion.Direction.LEFT_UP
import helpers.Table.Companion.Direction.RIGHT_UP
import helpers.Table.Companion.Direction.LEFT_DOWN
import helpers.Table.Companion.Direction.RIGHT_DOWN

class Table<T>(val matrix: List<List<T>>) : Iterable<TableCell<T>> {
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

    val size: Int = matrix.size

    operator fun get(coordinates: Coordinates2D): T = matrix[coordinates.y][coordinates.x]

    override operator fun iterator(): Iterator<TableCell<T>> = TableIterator()

    fun getRows(): List<List<T>> = matrix

    fun count(predicate: (T) -> Boolean): Int = matrix.sumOf { it.count(predicate) }

    fun findAll(predicate: (T) -> Boolean): Set<Coordinates2D> = matrix.flatMapIndexed { y, row ->
        row.withIndex().filter { (_, col) -> predicate(col) }.map { (x, _) -> Coordinates2D(x, y) }
    }.toSet()

    fun find(element: T): Coordinates2D {
        for (r in matrix.indices) {
            matrix[r].indexOf(element).takeIf { it != -1 }
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
        getRange(from, to, direction).map { (x, y) -> matrix[y][x] }

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
        coordinates.y in matrix.indices && coordinates.x in matrix[0].indices

    private inner class TableIterator : AbstractIterator<TableCell<T>>() {
        private var index: Int = 0

        override fun computeNext() {
            val y = index / size
            val x = index % size
            if (y > size - 1) {
                done()
                return
            }

            setNext(TableCell(coordinates = Coordinates2D(x, y), value = matrix[y][index % size]))
            index += 1
        }
    }
}

data class TableCell<T>(val coordinates: Coordinates2D, val value: T)
