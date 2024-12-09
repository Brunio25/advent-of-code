package util

import java.io.File
import java.net.URI
import java.nio.file.FileSystems

private val sep = FileSystems.getDefault().separator

object InputReader {
    lateinit var config: InputConfiguration

    private fun getResource(path: String): File =
        object {}::class.java.classLoader.getResource(path).let { File(URI(it.toString())) }

    fun getResource(year: Int, day: String): File =
        getResource("y$year${sep}day_$day${sep}${config.fileName}")
}
