package helpers.extensions

inline fun <T> measureTimeMillis(block: () -> T): Pair<Long, T> {
    val start = System.currentTimeMillis()
    val res = block()
    return (System.currentTimeMillis() - start) to res
}
