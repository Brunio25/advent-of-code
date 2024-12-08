package helpers

import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

data class Coordinates2D(val x: Int, val y: Int) {
    operator fun plus(vector: Vector): Coordinates2D = Coordinates2D(
        x = (x + vector.magnitude * cos(vector.angle)).roundToInt(),
        y = (y + vector.magnitude * sin(vector.angle)).roundToInt()
    )

    fun vector(other: Coordinates2D): Vector {
        val xDelta = (other.x - x).toDouble()
        val yDelta = (other.y - y).toDouble()

        val angle = atan(yDelta / xDelta)
        val hypotenuse = yDelta / sin(angle)

        return Vector(angle, hypotenuse)
    }
}

data class Vector(val angle: Double, val magnitude: Double)
