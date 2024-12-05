package helpers.extensions

fun <T> List<T>.split(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val elemIndex = indexOfFirst { predicate(it) }
    return if (elemIndex != -1) {
        subList(0, elemIndex) to subList(elemIndex + 1, size)
    } else {
        this to emptyList()
    }
}

fun <T> List<T>.split(element: T): Pair<List<T>, List<T>> = split { it == element }
fun List<String>.split(regex: Regex) = split { regex.matches(it) }

fun <T> MutableList<T>.swap(i1: Int, i2: Int) {
    val e1 = this[i1]
    this[i1] = this[i2]
    this[i2] = e1
}

fun <T> List<T>.swap(i1: Int, i2: Int): List<T> = toMutableList().also { it.swap(i1, i2) }

fun <T> List<T>.middle(): T = this[size / 2]
