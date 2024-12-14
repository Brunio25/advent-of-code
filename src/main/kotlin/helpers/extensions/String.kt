package helpers.extensions

fun String.middle(): Int {
    if (length % 2 != 0) throw IllegalArgumentException("\"$this\" length must be even")
    return length / 2
}
