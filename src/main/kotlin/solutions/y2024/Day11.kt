package solutions.y2024

import helpers.extensions.middle

class Day11 : PuzzleDay(2024) {
    private val input: Map<Long, Long> = readFileList[0].split("\\s+".toRegex()).map { it.toLong() }
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }

    private fun MutableMap<Long, Long>.applyRule(l: Long, stones: Map<Long, Long>) {
        val lCount = stones.getValue(l)

        if (l == 0L) {
            this[1] = getOrPut(1L) { 0 } + lCount
        } else if (l.toString().length % 2 == 0) {
            val (l1, l2) = l.splitMiddle()
            this[l1] = getOrPut(l1) { 0 } + lCount
            this[l2] = getOrPut(l2) { 0 } + lCount
        } else {
            val lTransformed = l * 2024
            this[lTransformed] = getOrPut(lTransformed) { 0 } + lCount
        }

        computeIfPresent(l) { _, v -> v - lCount }
        remove(l, 0)
    }

    private fun Long.splitMiddle(): Pair<Long, Long> {
        val strNum = toString()
        val middle = strNum.middle()
        return strNum.substring(0, middle).toLong() to strNum.substring(middle).toLong()
    }

    private fun countStones(blinks: Int): Long {
        var stones: MutableMap<Long, Long> = input.toMutableMap()

        for (i in 1..blinks) {
            val tempMap = stones.toMutableMap()
            val keys = tempMap.keys.toSet()
            keys.forEach { stone ->
                tempMap.applyRule(stone, stones)
            }
            stones = tempMap
        }

        return stones.values.sum()
    }

    override fun part1(): Long = countStones(25)


    override fun part2(): Long = countStones(75)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day11().run()
        }
    }
}
