package helpers.graphs.traverse

import helpers.graphs.Graph

interface GraphTraverse<T> {
    val graph: Graph<T>
    val startNode: T?

    fun goTo(destination: T)
    fun pathsAvailable(): Set<T>
}

class SimpleGraphTraverse<T>(
    override val graph: Graph<T>,
    override val startNode: T? = null
) : GraphTraverse<T> {
    private var currentVertex: T? = startNode

    override fun goTo(destination: T) =
        if (destination in pathsAvailable()) currentVertex = destination else throw NoSuchElementException()

    override fun pathsAvailable(): Set<T> = currentVertex?.let { graph.getVertex(it) } ?: graph.getAllVertexes()
}
