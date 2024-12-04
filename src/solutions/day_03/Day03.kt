package solutions.day_03

import solutions.Day

class Day03 : Day() {
    private val input by lazy { readFileList.reduce { acc, s -> acc + s } }

    private val mulRegex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()

    private fun String.part1Compute(): Int = mulRegex.findAll(this)
        .sumOf { it.destructured.let { (l, r) -> l.toInt() * r.toInt() } }

    override fun part1(): Int = input.part1Compute()

    override fun part2(): Int = input
        .remove(dontToDo)
        .part1Compute()

    private val dontToDo = """(don't\(\).*?(?=do\(\)|$))""".toRegex()

    private fun String.remove(regex: Regex): String = replace(regex, "")

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day03().run()
        }
    }
}
