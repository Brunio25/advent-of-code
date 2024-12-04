package solutions.day_04

import helpers.Coordinates2D
import kotlin.text.RegexOption.IGNORE_CASE
import solutions.Day
import solutions.day_04.Day04.Direction.DOWN
import solutions.day_04.Day04.Direction.LEFT
import solutions.day_04.Day04.Direction.LEFT_DOWN
import solutions.day_04.Day04.Direction.LEFT_UP
import solutions.day_04.Day04.Direction.RIGHT
import solutions.day_04.Day04.Direction.RIGHT_DOWN
import solutions.day_04.Day04.Direction.RIGHT_UP
import solutions.day_04.Day04.Direction.UP

class Day04 : Day() {
    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        LEFT_UP,
        RIGHT_UP,
        LEFT_DOWN,
        RIGHT_DOWN
    }

    private fun getSubstring(x: Int, y: Int, direction: Direction, wordLength: Int): String =
        getRange(x, y, direction, wordLength)
            .map { (x, y) -> readFileList[y][x] }
            .joinToString("")

    private fun getRange(x: Int, y: Int, direction: Direction, wordLength: Int): List<Coordinates2D> = when (direction) {
        UP -> y.downTo(y - wordLength).map { Coordinates2D(x, it) }
        DOWN -> y.rangeTo(y + wordLength).map { Coordinates2D(x, it) }
        LEFT -> x.downTo(x - wordLength).map { Coordinates2D(it, y) }
        RIGHT -> x.rangeTo(x + wordLength).map { Coordinates2D(it, y) }
        LEFT_UP -> getRange(x, y, LEFT, wordLength).zip(getRange(x, y, UP, wordLength)) { left, up -> Coordinates2D(left.x, up.y) }
        RIGHT_UP -> getRange(x, y, RIGHT, wordLength).zip(getRange(x, y, UP, wordLength)) { right, up -> Coordinates2D(right.x, up.y) }
        LEFT_DOWN -> getRange(x, y, LEFT, wordLength).zip(getRange(x, y, DOWN, wordLength)) { left, down -> Coordinates2D(left.x, down.y) }
        RIGHT_DOWN -> getRange(x, y, RIGHT, wordLength).zip(getRange(x, y, DOWN, wordLength)) { right, down -> Coordinates2D(right.x, down.y) }
    }

    override fun part1(): Int = readFileList
        .mapIndexed { y, line ->
            "X".toRegex(IGNORE_CASE).findAll(line)
                .sumOf { match ->
                    getSubstringInAllDirections(match.range.first, y)
                        .count { it == pat1Word || it.reversed() == pat1Word }
                }
        }.sum()

    private val pat1Word = "XMAS"
    private val part1WordLength = pat1Word.length - 1

    private fun getSubstringInAllDirections(x: Int, y: Int): List<String> =
        Direction.entries.mapNotNull { runCatching { getSubstring(x, y, it, part1WordLength) }.getOrNull() }

    override fun part2(): Int = readFileList
        .mapIndexed { y, line ->
            "A".toRegex(IGNORE_CASE).findAll(line)
                .count { match ->
                    getSubstringFromCenter(match.range.first, y)
                        ?.all { it == part2Word || it.reversed() == part2Word }
                        ?: false
                }
        }.sum()

    private val part2Word = "MAS"
    private val part2WordLength = 1

    private fun getSubstringFromCenter(x: Int, y: Int): List<String>? = runCatching {
        listOf(
            getSubstring(x, y, LEFT_UP, part2WordLength).reversed() + getSubstring(x, y, RIGHT_DOWN, part2WordLength).drop(1),
            getSubstring(x, y, LEFT_DOWN, part2WordLength).reversed() + getSubstring(x, y, RIGHT_UP, part2WordLength).drop(1)
        )
    }.getOrNull()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day04().run()
        }
    }
}
