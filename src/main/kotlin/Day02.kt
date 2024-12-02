package dev.aceituno

import kotlin.math.abs
import kotlin.math.sign

fun main() = runWithInput(2) { input ->
    val reports = input.lines().filter { it.isNotBlank() }.map { it.split(" ").map { level -> level.toInt() } }

    // Part 1
    val safeReports = reports.count(::isSafeReport)
    println("Part 1: $safeReports")

    // Part 2
    val safeDampened = reports.count { isSafeReport(it) || generateDampenedVersions(it).any(::isSafeReport) }
    println("Part 2: $safeDampened")
}

fun isSafeReport(report: List<Int>): Boolean {
    val sign = (report[1] - report[0]).sign
    for (i in 1 until report.size) {
        val diff = report[i] - report[i - 1]
        if (diff.sign != sign || diff == 0 || abs(diff) > 3) return false
    }
    return true
}

fun generateDampenedVersions(report: List<Int>) = sequence {
    for (i in report.indices) {
        yield(report.filterIndexed { index, _ -> index != i })
    }
}
