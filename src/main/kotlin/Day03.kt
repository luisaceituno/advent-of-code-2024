package dev.aceituno

import java.util.regex.Pattern

fun main() = runWithInput(3) { input ->
    val regex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")

    // Part 1
    val instructions = regex.findAll(input)
    val result = instructions.sumOf {
        val s1 = it.groupValues[1].toInt()
        val s2 = it.groupValues[2].toInt()
        s1 * s2
    }
    println("Part 1: $result")

    // Part 2
    val doPattern = Pattern.compile("do\\(\\)")
    val splitByDont = input.split("don't()").iterator()
    val dos = mutableListOf<String>()
    dos.add(splitByDont.next())
    splitByDont.forEachRemaining {
        it.split(doPattern, 2).getOrNull(1)?.let(dos::add)
    }
    val result2 = dos.sumOf {
        val partInstructions = regex.findAll(it)
        partInstructions.sumOf {
            val s1 = it.groupValues[1].toInt()
            val s2 = it.groupValues[2].toInt()
            s1 * s2
        }
    }
    println("Part 2: $result2")
}
