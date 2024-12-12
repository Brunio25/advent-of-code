package helpers.graphs

import helpers.Coordinates2D
import helpers.Table
import helpers.Table.Companion.Direction
import helpers.TableCell

class TableGraph<T>(
    val table: Table<T>,
    private vararg val direction: Direction
) : Graph<TableCell<T>> {
    override val arcs: MultiMap<TableCell<T>, TableCell<T>> = table.associateWith { table.getAdjacent(it.coordinates) }

    private fun Table<T>.getAdjacent(from: Coordinates2D): Set<TableCell<T>> =
        direction.map { it.calc(from) }.filter { inTable(it) }.map { TableCell(it, table[it]) }.toSet()
}

