package dev.aceituno

import kotlin.math.abs

fun main() = runWithInput(1) { input ->
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    input.lines().filter { it.isNotBlank() }.forEach { line ->
        val (l, r) = line.split("   ")
        left.add(l.toInt())
        right.add(r.toInt())
    }

    // Part 1
    left.sort()
    right.sort()
    var sum = 0
    for ((l, r) in left.zip(right)) {
        sum += abs(l - r)
    }
    println("Part 1: $sum")

    // Part 2
    val keyCount = mutableMapOf<Int, Int>()
    for (r in right) {
        keyCount.compute(r) { _, count -> (count ?: 0) + 1 }
    }
    var similarity = 0
    for (l in left) {
        similarity += l * (keyCount[l] ?: 0)
    }
    println("Part 2: $similarity")
}
