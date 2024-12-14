package dev.aceituno

fun main() = runWithInput(11) { input ->
    val startingStones = input.split(" ").nonBlank().map(String::toLong)

    // depth to remember -> stone -> count after exploding
    val shortcuts = mutableMapOf<Int, MutableMap<Long, Long>>()
    fun blinkTimes(stone: Long, depth: Int): Long {
        if (depth == 0) return 1
        val fromShortcut = shortcuts.getOrPut(depth, ::mutableMapOf)[stone]
        if (fromShortcut != null) return fromShortcut

        var count = 0L
        if (stone == 0L) {
            count += blinkTimes(1L, depth - 1)
        } else {
            val asText = "$stone"
            if (asText.length % 2 == 0) {
                asText.chunked(asText.length / 2).forEach {
                    count += blinkTimes(it.toLong(), depth - 1)
                }
            } else {
                count += blinkTimes(stone * 2024, depth - 1)
            }
        }
        shortcuts.getValue(depth)[stone] = count
        return count
    }

    var count25 = 0L
    var count75 = 0L
    for (stone in startingStones) {
        count25 += blinkTimes(stone, 25)
        count75 += blinkTimes(stone, 75)
    }

    println("Part 1: $count25")
    println("Part 2: $count75")
}
