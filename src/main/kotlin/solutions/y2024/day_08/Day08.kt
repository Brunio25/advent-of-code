package solutions.y2024.day_08

import helpers.Coordinates2D
import helpers.Table
import util.Day

class Day08 : Day() {
    private val map: Table<Char> = Table(readFileList.map { it.toList() })
    private val antennaFrequencyGroups: Sequence<List<Coordinates2D>> = map.findAll { it != '.' }.collectToGroups()

    private fun getNumberOfAntiNodes(
        antiNodeMultiplierStart: Int,
        antiNodeMultiplierLimit: Int = antiNodeMultiplierStart
    ) = antennaFrequencyGroups
        .filter { it.size >= 2 }
        .flatMap { antennaFrequencyCoords ->
            antennaFrequencyCoords.flatMap {
                antennaFrequencyCoords.getAntennaAntiNodes(it, antiNodeMultiplierStart, antiNodeMultiplierLimit)
            }
        }
        .distinct()
        .count()

    private fun List<Coordinates2D>.getAntennaAntiNodes(
        coordinates: Coordinates2D,
        multiplierStart: Int,
        multiplierLimit: Int
    ): List<Coordinates2D> =
        filter { it != coordinates }
            .flatMap { antenna ->
                val baseVector = coordinates.vector(antenna)

                multiplierStart.rangeTo(multiplierLimit)
                    .map { baseVector.copy(magnitude = baseVector.magnitude * it) }
                    .map { coordinates + it }
                    .takeWhile { map.inTable(it) }
            }

    private fun Set<Coordinates2D>.collectToGroups(): Sequence<List<Coordinates2D>> = map { it to map[it] }
        .groupBy { it.second }
        .mapValues { entry -> entry.value.map { it.first } }
        .map { it.value }
        .asSequence()

    override fun part1(): Int = getNumberOfAntiNodes(2)
    override fun part2(): Int = getNumberOfAntiNodes(1, map.size)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day08().run()
        }
    }
}
