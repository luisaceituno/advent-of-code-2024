package dev.aceituno

fun main() = runWithInput(5) { input ->
    val (rules, updates) = input.chunkedByEmptyLine()
    val sorting = mutableMapOf<String, Int>()
    for (rule in rules) {
        val (low, high) = rule.split("|")
        sorting["$low,$high"] = -1
        sorting["$high,$low"] = 1
    }

    var sumOrdered = 0
    var sumUnordered = 0
    for (update in updates) {
        val original = update.split(",")
        val sorted = original.sortedWith { s1, s2 -> sorting["$s1,$s2"] ?: 0 }
        if (sorted == original) {
            sumOrdered += sorted[sorted.size / 2].toInt()
        } else {
            sumUnordered += sorted[sorted.size / 2].toInt()
        }
    }

    println("Part 1: $sumOrdered")
    println("Part 2: $sumUnordered")
}
