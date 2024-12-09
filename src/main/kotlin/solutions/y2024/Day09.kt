package solutions.y2024

import helpers.extensions.swap
import helpers.extensions.swapRange
import kotlin.math.min

class Day09 : PuzzleDay(2024) {
    private val chunkedDiskMap: Sequence<String> = readFileList.joinToString("")
        .asSequence()
        .chunked(2)
        .withIndex()
        .flatMap { (index, chunk) ->
            generateBlocks(index, chunk[0])
                .let {
                    if (chunk.size == 2) it + generateFreeSpace(chunk[1])
                    else it
                }
        }

    private fun generateFreeSpace(id: Char): List<String> = (1..id.digitToInt()).map { "." }
    private fun generateBlocks(e: Int, id: Char): List<String> = (1..id.digitToInt()).map { e.toString() }

    private fun Sequence<String>.calculateChecksum(): Long = withIndex()
        .filter { it.value != "." }
        .sumOf { (index, id) -> index * id.toLong() }

    override fun part1(): Long = chunkedDiskMap
        .leftAlignBlocks()
        .calculateChecksum()


    private fun Sequence<String>.leftAlignBlocks(): Sequence<String> {
        val result = toMutableList()
        var leftPointer = 0
        var rightPointer = result.lastIndex

        while (leftPointer < rightPointer) {
            if (result[leftPointer] == "." && result[rightPointer] != ".")
                result.swap(leftPointer, rightPointer)

            if (result[leftPointer] != ".") leftPointer += 1
            if (result[rightPointer] == ".") rightPointer -= 1
        }

        return result.asSequence()
    }

    override fun part2(): Long = chunkedDiskMap
        .leftAlignBlocksThatFit()
        .calculateChecksum()

    private fun Sequence<String>.leftAlignBlocksThatFit(): Sequence<String> {
        val result = toMutableList()
        var rightPointer = result.lastIndex

        mainLoop@ while (rightPointer >= 0) {
            if (result[rightPointer] != ".") {
                val blockEnd = rightPointer
                val fileId = result[rightPointer]

                while (result[rightPointer] == fileId) {
                    rightPointer -= 1
                    if (rightPointer < 0) break@mainLoop
                }

                val blockStart = rightPointer + 1
                val block = blockStart..blockEnd
                
                result.firstConsecutiveFreeSpace(block.count(), min(blockStart, result.size - block.count()))
                    ?.let { result.swapRange(block, it) }
            }

            if (result[rightPointer] == ".") rightPointer -= 1
        }

        return result.asSequence()
    }

    private fun MutableList<String>.firstConsecutiveFreeSpace(n: Int, limit: Int): IntRange? = subList(0, limit).indices
        .dropWhile { this[it] != "." }
        .firstOrNull { index ->
            slice(index..<index + n).all { it == "." }
        }?.let { it..<it + n }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day09().run()
        }
    }
}
