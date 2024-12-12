package helpers.graphs

import helpers.graphs.Graph.GraphBuilder
import helpers.graphs.traverse.SimpleGraphTraverse

class BidirectionalGraph<T>(override val arcs: MultiMap<T, T>) : Graph<T> {
    override fun getTraverse(origin: T?): SimpleGraphTraverse<T> = SimpleGraphTraverse(this, origin)

    companion object {
        class BidirectionalGraphBuilder<T> : GraphBuilder<T> {
            override val arcs: MutableMultiMap<T, T> = mutableMapOf()

            override fun toGraph(): BidirectionalGraph<T> = BidirectionalGraph(arcs)

            override fun put(origin: T, destination: Set<T>): GraphBuilder<T> {
                destination.forEach { addArc(origin, it) }
                return this
            }

            override fun addArc(origin: T, destination: T): GraphBuilder<T> {
                if (origin !in arcs) initEntry(origin)
                if (destination !in arcs) initEntry(destination)

                arcs.computeIfPresent(origin) { _, neighbours -> neighbours + destination }
                arcs.computeIfPresent(destination) { _, neighbours -> neighbours + origin }
                return this
            }
        }
    }
}
