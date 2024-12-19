package solutions.y2024

import helpers.Coordinates2D
import helpers.Vector
import helpers.extensions.multiplyOf

class Day14 : PuzzleDay(2024) {
    private val extractRobotRegex = "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)".toRegex()
    private val tilesWidth = 101
    private val tilesHeight = 103
    private val xMiddle = (tilesWidth - 1) / 2
    private val yMiddle = (tilesHeight - 1) / 2
    private val robots: List<Robot> = readFileList.map { line ->
        val (x, y, xComponent, yComponent) = extractRobotRegex.find(line)?.groupValues?.drop(1)?.map { it.toInt() }!!
        Robot(Coordinates2D(x, y), Vector.fromOrigin(xComponent, yComponent))
    }

    private data class Robot(val initialPos: Coordinates2D, val vector: Vector)

    //   1 2
    // 1 2 3
    // 2 4 5
    private fun Coordinates2D.quadrant(): Int {
        val x = if (this.x < xMiddle) 0 else 1
        val y = if (this.y < yMiddle) 2 else 4

        return x + y
    }

    override fun part1(): Long = robots
        .map { robot ->
            val sumPos = robot.initialPos + robot.vector.copy(magnitude = robot.vector.magnitude * 100)
            val finalPos = Coordinates2D(
                x = (sumPos.x % tilesWidth + tilesWidth) % tilesWidth,
                y = (sumPos.y % tilesHeight + tilesHeight) % tilesHeight
            )
            finalPos
        }
        .filter { it.x != xMiddle && it.y != yMiddle }
        .groupBy { it.quadrant() }.values
        .multiplyOf { it.size.toLong() }

    override fun part2(): Long = TODO()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day14().run()
        }
    }
}
