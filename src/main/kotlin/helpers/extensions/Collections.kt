package helpers.extensions

fun <T> List<T>.elementAtMiddle(): T = this[middle()]

fun <T> List<T>.middle(): Int = size / 2

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

fun <T> MutableList<T>.swapRange(r1: IntRange, r2: IntRange) {
    if (r1.count() != r2.count()) throw IllegalArgumentException("r1 $r1 must be the same length as r2 $r2")
    r1.zip(r2).forEach { (i1, i2) -> swap(i1, i2) }
}

fun <T> MutableList<T>.replaceRange(range: IntRange, list: List<T>) {
    if (range.count() != list.size)
        throw IllegalArgumentException("Range $range must have the same number of elements as $list")

    range.zip(list).forEach { (i, elem) -> this[i] = elem }
}

inline fun <T> Iterable<T>.multiplyOf(selector: (T) -> Long): Long {
    var mult = 1L
    for (element in this) {
        mult *= selector(element)
    }
    return mult
}
