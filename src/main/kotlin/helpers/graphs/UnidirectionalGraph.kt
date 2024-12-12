package helpers.graphs

import helpers.graphs.Graph.GraphBuilder

class UnidirectionalGraph<T>(override val arcs: MultiMap<T, T>) : Graph<T> {
    companion object {
        class UnidirectionalGraphBuilder<T> : GraphBuilder<T> {
            override val arcs: MutableMultiMap<T, T> = mutableMapOf()

            override fun toGraph(): UnidirectionalGraph<T> = UnidirectionalGraph(arcs)

            override fun addArc(origin: T, destination: T): UnidirectionalGraphBuilder<T> {
                if (origin !in arcs) initEntry(origin)
                arcs.computeIfPresent(origin) { _, dests -> dests + destination }

                return this
            }

            override fun put(origin: T, destination: Set<T>): UnidirectionalGraphBuilder<T> {
                destination.forEach { addArc(origin, it) }
                return this
            }
        }
    }
}
