package util

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import solutions.y2024.PuzzleDay
import util.InputConfiguration.REAL_INPUT
import util.RunConfiguration.RUN_LATEST_SOLUTION

object PuzzleRunner {
    private fun runSolutions(configs: Pair<RunConfiguration, InputConfiguration>) {
        InputReader.config = configs.second
        getDaysToRun(configs).forEach {
            it.createInstance().run()
        }

    }

    private fun getDaysToRun(configs: Pair<RunConfiguration, InputConfiguration>): List<KClass<out PuzzleDay>> =
        PuzzleDay::class.sealedSubclasses.sortedBy { it.simpleName }.let {
            if (RUN_LATEST_SOLUTION == configs.first) it.takeLast(1) else it
        }

    private fun getRunConfigurationsFromArguments(args: Array<out String>): Pair<RunConfiguration, InputConfiguration> =
        args.fold(RUN_LATEST_SOLUTION to REAL_INPUT) { acc, arg ->
            RunConfiguration.fromArg(arg)?.let { acc.copy(first = it) }
                ?: InputConfiguration.fromArg(arg)?.let { acc.copy(second = it) }
                ?: acc
        }

    @JvmStatic
    fun main(vararg args: String) {
        runSolutions(getRunConfigurationsFromArguments(args))
    }
}

enum class RunConfiguration(private vararg val args: String) {
    RUN_LATEST_SOLUTION,
    RUN_ALL_SOLUTIONS("-a", "--all");

    companion object {
        fun fromArg(arg: String): RunConfiguration? = entries.find { arg.trim() in it.args }
    }
}

enum class InputConfiguration(val fileName: String, private vararg val args: String) {
    REAL_INPUT("input.txt"),
    TEST_INPUT("test_input.txt", "-t", "--test");

    companion object {
        fun fromArg(arg: String): InputConfiguration? = entries.find { arg in it.args }
    }
}
