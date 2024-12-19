package helpers

import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.roundToLong
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

data class Coordinates2DLong(val x: Long, val y: Long) {
    operator fun plus(vector: Vector): Coordinates2DLong = Coordinates2DLong(
        x = (x + vector.magnitude * cos(vector.angle)).roundToLong(),
        y = (y + vector.magnitude * sin(vector.angle)).roundToLong()
    )

    fun vector(other: Coordinates2DLong): Vector {
        val xDelta = (other.x - x).toDouble()
        val yDelta = (other.y - y).toDouble()

        val angle = atan(yDelta / xDelta)
        val hypotenuse = yDelta / sin(angle)

        return Vector(angle, hypotenuse)
    }
}

data class Vector(val angle: Double, val magnitude: Double) {
    companion object {
        fun fromOrigin(x: Int, y: Int): Vector = Coordinates2D(0, 0).vector(Coordinates2D(x, y))
    }

    val xComponent: Int by lazy { (magnitude * cos(angle)).roundToInt() }
    val yComponent: Int by lazy { (magnitude * sin(angle)).roundToInt() }

    operator fun component3() = xComponent
    operator fun component4() = yComponent
}
