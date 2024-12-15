package dev.aceituno

import org.jetbrains.kotlinx.multik.api.linalg.solve
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.default.linalg.DefaultLinAlg
import org.jetbrains.kotlinx.multik.ndarray.data.get
import kotlin.math.abs
import kotlin.math.roundToLong

fun main() = runWithInput(13) { input ->
    val machines = input.chunkedByEmptyLine().map { (aSpec, bSpec, targetSpec) ->
        val (aX, aY) = aSpec.longs()
        val (bX, bY) = bSpec.longs()
        val (tX, tY) = targetSpec.longs()
        Machine(aY, aX, bY, bX, tY, tX)
    }

    var part1 = 0L
    var part2 = 0L
    for ((aY, aX, bY, bX, targetY, targetX) in machines) {
        part1 += calculateCost(aY, aX, bY, bX, targetY, targetX)
        part2 += calculateCost(aY, aX, bY, bX, targetY + 10000000000000, targetX + 10000000000000)
    }
    println("Part 1: $part1")
    println("Part 2: $part2")
}

data class Machine(
    val aY: Long,
    val aX: Long,
    val bY: Long,
    val bX: Long,
    val targetY: Long,
    val targetX: Long
)

fun calculateCost(aY: Long, aX: Long, bY: Long, bX: Long, targetY: Long, targetX: Long): Long {
    val coefficients = mk.ndarray(mk[mk[aY, bY], mk[aX, bX]])
    val targets = mk.ndarray(mk[targetY, targetX])
    val result = DefaultLinAlg.solve(coefficients, targets)
    val aPressesRaw = result[0]
    val bPressesRaw = result[1]
    val aPresses = aPressesRaw.roundToLong()
    val bPresses = bPressesRaw.roundToLong()
    if (abs(aPressesRaw - aPresses) < 0.01 && abs(bPressesRaw - bPresses) < 0.01) {
        return aPresses * 3 + bPresses
    }
    return 0
}
