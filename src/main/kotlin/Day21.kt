package dev.aceituno

import kotlin.math.absoluteValue

fun main() = runWithInput(21) { Day21(it).run() }

data class Day21(val input: String) {
    val numpad = "789\n456\n123\nX0A".matrix().eachWithPos().associate { (k, p) -> k to p }
    val dirpad = "X^A\n<v>".matrix().eachWithPos().associate { (k, p) -> k to p }
    val codes = input.lines()

    fun run() {
        val cache = mutableMapOf<KeyJump, List<KeyJump>>()
        var complexity2 = 0L
        var complexity25 = 0L
        for (code in codes) {
            var curJumps = mutableMapOf<KeyJump, Long>()
            "A$code".toList().windowed(2).forEach { (from, to) ->
                val jump = KeyJump(from, to)
                cache.getOrPut(jump) { keyJumpsOnControllingDirpad(numpad, jump) }.forEach { resultJump ->
                    curJumps.merge(resultJump, 1, Long::plus)
                }
            }
            repeat(25) { round ->
                val nextJumps = mutableMapOf<KeyJump, Long>()
                curJumps.forEach { jump, count ->
                    cache.getOrPut(jump) { keyJumpsOnControllingDirpad(dirpad, jump) }.forEach { resultJump ->
                        nextJumps.merge(resultJump, count, Long::plus)
                    }
                }
                curJumps = nextJumps
                if (round == 1) {
                    complexity2 += code.ints().first() * curJumps.values.sum()
                }
            }
            complexity25 += code.ints().first() * curJumps.values.sum()
        }
        println("Part 1: $complexity2")
        println("Part 2: $complexity25")
    }

    data class KeyJump(val from: Char, val to: Char)

    fun keyJumpsOnControllingDirpad(targetPad: Map<Char, Pos>, jump: KeyJump): List<KeyJump> {
        val start = targetPad.getValue(jump.from)
        val diff = start.deltaTo(targetPad.getValue(jump.to))
        val vMoves = (if (diff.y > 0) "v" else "^").repeat(diff.y.absoluteValue)
        val hMoves = (if (diff.x > 0) ">" else "<").repeat(diff.x.absoluteValue)
        val forbidden = targetPad.getValue('X')
        val canStartY = start.add(Pos(diff.y, 0)) != forbidden
        val canStartX = start.add(Pos(0, diff.x)) != forbidden
        val arrowPresses =
            if (diff.x < 0 && canStartX) hMoves + vMoves else
                if (canStartY) vMoves + hMoves else
                    hMoves + vMoves

        return "A${arrowPresses}A".toList().windowed(2).map { (from, to) -> KeyJump(from, to) }
    }
}
