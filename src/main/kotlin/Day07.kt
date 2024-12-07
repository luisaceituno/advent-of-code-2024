package dev.aceituno

fun main() = runWithInput(7) { input ->
    val equations = input
        .lines()
        .nonBlank()
        .map { line ->
            line.replace(":", "")
                .split(" ")
                .map { value -> value.toLong() }
        }

    fun canBeFulfilled(operands: List<Long>, operations: List<(Long, Long) -> Long>): Boolean {
        val target = operands[0]
        var lastResults = listOf(operands[1])
        for (operand in operands.drop(2)) {
            val nextResults = mutableListOf<Long>()
            for (lastResult in lastResults) {
                for (operation in operations) {
                    val nextResult = operation(lastResult, operand)
                    if (nextResult <= target) nextResults += nextResult
                }
            }
            lastResults = nextResults
        }
        return target in lastResults
    }

    var resultSumTimes = 0L
    var resultSumTimesConcat = 0L
    for (equation in equations) {
        if (canBeFulfilled(equation, listOf(Long::plus, Long::times))) resultSumTimes += equation[0]
        if (canBeFulfilled(equation, listOf(Long::plus, Long::times, Long::concat))) resultSumTimesConcat += equation[0]
    }

    println("Part 1: $resultSumTimes")
    println("Part 2: $resultSumTimesConcat")
}

fun Long.concat(other: Long): Long = "$this$other".toLong()
