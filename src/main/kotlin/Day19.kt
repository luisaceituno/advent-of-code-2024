package dev.aceituno

fun main() = runWithInput(19) { Day19(it).run() }

data class Day19(val input: String) {
    val lines = input.lines()
    val towels = lines[0].split(", ")
    val patterns = lines.drop(2)

    fun run() {
        val memory = mutableMapOf<String, Long>()
        fun deepSearch(pattern: String): Long {
            memory[pattern]?.let { return it }
            var possibilities = 0L
            for (towel in towels) {
                if (towel == pattern) possibilities++
                else if (pattern.startsWith(towel)) {
                    possibilities += deepSearch(pattern.drop(towel.length))
                }
            }
            memory[pattern] = possibilities
            return possibilities
        }

        var possibleCombinations = 0L
        for (pattern in patterns) possibleCombinations += deepSearch(pattern)
        val doableCount = patterns.sumOf { if (memory[it] != 0L) 1.toInt() else 0 }

        println("Part 1: $doableCount")
        println("Part 2: $possibleCombinations")
    }
}
