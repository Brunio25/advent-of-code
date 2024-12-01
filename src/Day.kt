import java.io.File
import kotlin.system.measureNanoTime

abstract class Day(
    useCompleteInput: Boolean = true
) {
    private val completeInputFileName = "input.txt"
    private val testInputFileName = "test_input.txt"
    private val day = javaClass.simpleName.substringAfter("Day")

    private val inputPath = "src\\day_$day\\inputs\\${if (useCompleteInput) completeInputFileName else testInputFileName}"

    val readFileList: List<String> by lazy { File(inputPath).useLines { it.toList() } }

    abstract fun part1(): Any
    abstract fun part2(): Any

    fun run() {
        println("Day $day:")

        measureNanoTime {
            print("\tPart 1: ${part1()} | ")
        }.let { println("$it ns\n") }

        measureNanoTime {
            print("\tPart 2: ${part2()} | ")
        }.let { println("$it ns\n") }
    }
}
