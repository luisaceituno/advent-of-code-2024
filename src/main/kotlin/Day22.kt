package dev.aceituno

fun main() = runWithInput(22) { Day22(it).run() }

data class Day22(val input: String) {
    val initialNumbers = input.lines().map(String::toLong)

    fun run() {
        var sum = 0L
        val bananas = mutableMapOf<Sequence, Int>()
        for (number in initialNumbers) {
            val seenSequences = mutableSetOf<Sequence>()
            var curSecret = number
            var curPrice = (curSecret % 10).toInt()
            var curSequence = Sequence(0, 0, 0, 0)
            repeat(2000) { round ->
                val nextSecret = nextSecret(curSecret)
                val nextPrice = (nextSecret % 10).toInt()
                val priceDiff = nextPrice - curPrice
                curSecret = nextSecret
                curPrice = nextPrice
                curSequence = curSequence.shift(priceDiff.toInt())
                if (round >= 3 && curSequence !in seenSequences) {
                    bananas.merge(curSequence, curPrice, Int::plus)
                    seenSequences.add(curSequence)
                }
            }
            sum += curSecret
        }
        println("Part 1: $sum")
        println("Part 2: ${bananas.values.max()}")
    }

    data class Sequence(val d1: Int, val d2: Int, val d3: Int, val d4: Int) {
        fun shift(next: Int) = Sequence(d2, d3, d4, next)
    }

    fun nextSecret(number: Long): Long {
        val step1 = (((number shl 6) xor number) and 16777215)
        val step2 = (((step1 shr 5) xor step1) and 16777215)
        return (((step2 shl 11) xor step2) and 16777215)
    }
}
