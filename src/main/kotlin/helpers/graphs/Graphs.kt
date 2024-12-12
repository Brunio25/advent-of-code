package helpers.graphs

import helpers.graphs.search.GraphSearchStrategy
import helpers.graphs.traverse.GraphTraverse
import helpers.graphs.traverse.SimpleGraphTraverse

typealias MutableMultiMap<K, V> = MutableMap<K, Set<V>>
typealias MultiMap<K, V> = Map<K, Set<V>>

interface Graph<T> {
    val arcs: MultiMap<T, T>

    fun getVertex(origin: T): Set<T> = arcs.getOrDefault(origin, emptySet())
    fun getAllVertexes(): Set<T> = arcs.keys
    fun getTraverse(origin: T? = null): GraphTraverse<T> = SimpleGraphTraverse(this, origin)
    fun getSearch(strategy: GraphSearchStrategy<T>): Set<List<T>> = strategy.search()

    interface GraphBuilder<T> {
        val arcs: MutableMultiMap<T, T>

        fun toGraph(): Graph<T>
        fun addArc(origin: T, destination: T): GraphBuilder<T>
        fun put(origin: T, destination: Set<T>): GraphBuilder<T>

        fun initEntry(origin: T) = arcs.put(origin, emptySet())
    }
}
