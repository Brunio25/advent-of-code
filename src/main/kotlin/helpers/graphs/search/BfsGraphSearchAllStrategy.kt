package helpers.graphs.search

import helpers.graphs.Graph

class BfsGraphSearchAllStrategy<T>(
    override val graph: Graph<T>,
    override val comparator: Comparator<T> = Comparator { _, _ -> 1 },
    override val startNode: T
) : GraphSearchStrategy<T> {
    companion object {
        private data class NodePath<T>(
            val node: T,
            val path: List<T>
        )
    }

    private val queue: ArrayDeque<NodePath<T>> = ArrayDeque()

    init {
        add(startNode, listOf(startNode))
    }

    override fun search(): Set<List<T>> { // make it more generic
        val possiblePaths: MutableSet<List<T>> = mutableSetOf()

        while (queue.isNotEmpty()) {
            val (currentNode, path) = queue.removeFirst()

            graph.getVertex(currentNode)
                .forEach { neighbour ->
                    when (comparator.compare(currentNode, neighbour)) {
                        0 -> possiblePaths.add(path + neighbour)
                        1 -> add(neighbour, path + neighbour)
                        -1 -> {}
                    }
                }
        }

        return possiblePaths
    }

    private fun add(element: T, path: List<T>) {
        queue.addLast(NodePath(element, path))
    }
}
