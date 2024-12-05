package util

import java.io.File
import java.nio.file.FileSystems

private val sep = FileSystems.getDefault().separator

fun getResource(path: String): File = object {}::class.java.classLoader.getResource(path).let { File(path) }

fun getResource(year: Int, day: Int, isTest: Boolean): File =
    getResource("y$year${sep}day_$day${sep}${if (isTest) "test_input.txt" else "input.txt"}")
