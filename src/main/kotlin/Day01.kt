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
    left.sort()
    right.sort()
    var sum = 0
    for ((l, r) in left.zip(right)) {
        sum += abs(l - r)
    }
    println(sum)
}
