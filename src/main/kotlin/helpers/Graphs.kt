package helpers

typealias MutableMultiMap<K, V> = MutableMap<K, List<V>>
typealias MultiMap<K, V> = Map<K, List<V>>

class Graph<T>(private val arcs: MultiMap<T, T>) { // TODO: Improve this Collection
    fun getVertex(origin: T) = arcs.getOrDefault(origin, emptyList())

    fun getAllVertexes(): List<T> = arcs.keys.toList()

    fun getTransverse(): GraphTransverse<T> = GraphTransverse(this)
}

class GraphTransverse<T>(private val graph: Graph<T>) { // TODO: Improve this Collection, possibly add transverse types
    private var currentVertex: T? = null

    fun from(origin: T): GraphTransverse<T> {
        currentVertex = origin
        return this
    }

    fun goTo(destination: T) =
        if (destination in pathsAvailable()) currentVertex = destination else throw NoSuchElementException()

    fun pathsAvailable(): List<T> = currentVertex?.let { graph.getVertex(it) } ?: graph.getAllVertexes()
}

class GraphBuilder<T> {
    private val arcs: MutableMultiMap<T, T> = mutableMapOf()

    fun toGraph(): Graph<T> = Graph(arcs)

    fun addArc(origin: T, destination: T): GraphBuilder<T> {
        if (!arcs.contains(origin)) initEntry(origin)
        arcs.computeIfPresent(origin) { _, dests -> dests.toMutableList().also { it.add(destination) } }

        return this
    }

    fun put(origin: T, destination: List<T>) = arcs.put(origin, destination)

    private fun initEntry(origin: T) = arcs.put(origin, emptyList())
}
