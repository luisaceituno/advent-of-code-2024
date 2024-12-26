package dev.aceituno

fun main() = runWithInput(25) { Day25(it).run() }

data class Day25(val input: String) {

    fun run() {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        for (chunk in input.chunkedByEmptyLine()) {
            if (chunk.first().startsWith('#')) locks.add(parseSchematic(chunk))
            else keys.add(parseSchematic(chunk))
        }

        var pairs = 0
        for (lock in locks) for (key in keys) if (lock.indices.all { lock[it] + key[it] <= 7 }) pairs++

        println("Part 1: $pairs")
//        println("Part 2: ${}")
    }

    fun parseSchematic(chunk: List<String>): List<Int> {
        val result = Array<Int>(chunk[0].length) { 0 }
        for (line in chunk) line.withIndex().filter { it.value == '#' }.forEach {
            result[it.index] += 1
        }
        return result.toList()
    }
}
