package day_03

import Day

class Day03 : Day() {
    private val input by lazy { readFileList.reduce { acc, s -> acc + s } }

    override fun part1(): Int = mulRegex.findAll(input)
        .map { it.destructured.let { (l, r) -> l.toInt() * r.toInt() } }
        .sum()

    private val mulRegex = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()

    override fun part2(): Int = TODO()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day03().run()
        }
    }
}
