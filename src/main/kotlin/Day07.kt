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

    fun canBeFulfilled(operands: List<Long>, operations: List<Long.(Long) -> Long>): Boolean {
        val target = operands[0]
        var lastResults = setOf(operands[1])
        for (operand in operands.drop(2)) {
            val nextResults = mutableSetOf<Long>()
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

    val concatOp = { n1: Long, n2: Long -> "$n1$n2".toLong() }
    var resultSumTimes = 0L
    var resultSumTimesConcat = 0L
    for (equation in equations) {
        if (canBeFulfilled(equation, listOf(Long::plus, Long::times))) resultSumTimes += equation[0]
        if (canBeFulfilled(equation, listOf(Long::plus, Long::times, concatOp))) resultSumTimesConcat += equation[0]
    }

    println("Part 1: $resultSumTimes")
    println("Part 2: $resultSumTimesConcat")
}
