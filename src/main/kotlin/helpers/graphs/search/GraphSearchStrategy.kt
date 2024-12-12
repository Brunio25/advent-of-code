package helpers.graphs.search

import helpers.graphs.Graph

interface GraphSearchStrategy<T> {
    val graph: Graph<T>
    val comparator: Comparator<T>
    val startNode: T

    fun search(): Set<List<T>>
}
