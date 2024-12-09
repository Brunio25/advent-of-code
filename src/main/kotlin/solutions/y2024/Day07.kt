package solutions.y2024

private typealias Operation = (Long, Long) -> Long

class Day07 : PuzzleDay(2024) {
    private val equations: List<Pair<Long, List<Long>>> = readFileList
        .map { line ->
            val (objective, factors) = line.split(": ")
            objective.toLong() to factors.split(" ").map { it.toLong() }
        }

    private fun numberOfValidEquations(operations: List<Operation>): Long = equations
        .filter { (objective, factors) -> isValidEquation(objective, factors[0], factors.drop(1), operations) }
        .sumOf { it.first }

    private fun isValidEquation(objective: Long, total: Long, factors: List<Long>, operations: List<Operation>): Boolean =
        factors.takeIf { it.isNotEmpty() && objective >= total }?.let {
            operations.any { isValidEquation(objective, it(total, factors[0]), factors.drop(1), operations) }
        } ?: (objective == total)

    override fun part1(): Long = numberOfValidEquations(operationsPart1)

    private val operationsPart1: List<Operation> = listOf({ a, b -> a + b }, { a, b -> a * b })

    override fun part2(): Long = numberOfValidEquations(operationPart2)

    private val operationPart2: List<Operation> = operationsPart1 + { a, b -> "$a$b".toLong()}

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Day07().run()
        }
    }
}
